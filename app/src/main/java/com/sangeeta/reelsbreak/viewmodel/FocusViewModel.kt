package com.sangeeta.reelsbreak.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangeeta.reelsbreak.data.FocusStateHolder
import com.sangeeta.reelsbreak.data.preferences.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FocusModeUiState(
    val isFocusActive: Boolean = false,
    val remainingMillis: Long = 0L,
    val selectedMinutes: Int = 0,
    val sessionDurationMinutes: Int = 0,
    val selectedApps: Set<String> = emptySet(),
    val errorMessage: String? = null
)

@HiltViewModel
class FocusViewModel @Inject constructor(
    private val prefs: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FocusModeUiState())
    val uiState: StateFlow<FocusModeUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        // Combining reactive preferences inside a clean layout matrix
        viewModelScope.launch {
            combine(
                prefs.isFocusActive,
                prefs.blockedPackages,
                prefs.selectedFocusMinutes,
                prefs.focusEndTimestamp
            ) { active, packages, minutes, endTs ->
                FocusStateHolder.isFocusActive = active
                FocusStateHolder.blockedPackages = packages
                FocusStateHolder.focusEndTimestamp = endTs

                val now = System.currentTimeMillis()
                if (endTs > 0L && endTs > now) {
                    startTimerInternal(endTs)
                } else if (endTs > 0L && endTs <= now) {
                    handleSessionExpiry()
                }

                _uiState.value.copy(
                    isFocusActive = active,
                    selectedApps = packages,
                    selectedMinutes = minutes
                )
            }.collect { state ->
                _uiState.update { state }
            }
        }
    }

    fun toggleAppSelection(packageName: String) {
        _uiState.update { state ->
            val newSet = state.selectedApps.toMutableSet().apply {
                if (contains(packageName)) remove(packageName) else add(packageName)
            }
            state.copy(selectedApps = newSet)
        }
        applyActiveSessionChanges()
    }

    fun setSelectedMinutes(minutes: Int) {
        _uiState.update { it.copy(selectedMinutes = minutes) }
        viewModelScope.launch {
            prefs.setSelectedFocusMinutes(minutes)
        }
        applyActiveSessionChanges()
    }

    fun startFocusSession() {
        val state = _uiState.value
        if (state.selectedApps.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Please select at least one app to block.") }
            return
        }
        if (state.selectedMinutes <= 0) {
            _uiState.update { it.copy(errorMessage = "Please select a session duration.") }
            return
        }

        val endTs = System.currentTimeMillis() + (state.selectedMinutes * 60_000L)
        _uiState.update { it.copy(sessionDurationMinutes = state.selectedMinutes) }

        viewModelScope.launch {
            prefs.startFocusSession(endTs)
            prefs.setBlockedPackages(state.selectedApps)
        }
    }

    fun stopFocusSession() {
        viewModelScope.launch {
            prefs.stopFocusSession()
            prefs.setSelectedFocusMinutes(0)
        }
        stopTimerInternal()
    }

    private fun handleSessionExpiry() {
        viewModelScope.launch {
            prefs.stopFocusSession()
        }
        stopTimerInternal()
    }

    private fun startTimerInternal(endTs: Long) {
        if (timerJob?.isActive == true) return
        timerJob = viewModelScope.launch {
            while (true) {
                val now = System.currentTimeMillis()
                val remaining = (endTs - now).coerceAtLeast(0L)
                _uiState.update { it.copy(remainingMillis = remaining, isFocusActive = remaining > 0) }

                if (remaining <= 0L) {
                    handleSessionExpiry()
                    break
                }
                delay(1000L)
            }
        }
    }

    private fun stopTimerInternal() {
        timerJob?.cancel()
        timerJob = null
        _uiState.update { it.copy(remainingMillis = 0L, isFocusActive = false) }
    }

    private fun applyActiveSessionChanges() {
        val state = _uiState.value
        if (!state.isFocusActive || state.selectedApps.isEmpty() || state.selectedMinutes <= 0) return

        val newEndTs = System.currentTimeMillis() + (state.selectedMinutes * 60_000L)
        viewModelScope.launch {
            prefs.startFocusSession(newEndTs)
            prefs.setBlockedPackages(state.selectedApps)
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
