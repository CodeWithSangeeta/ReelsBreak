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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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

    private val _errorMessage = MutableStateFlow<String?>(null)
    private var timerJob: Job? = null

    val uiState: StateFlow<FocusModeUiState> = combine(
        prefs.isFocusActive,
        prefs.blockedPackages,
        prefs.selectedFocusMinutes,
        prefs.focusEndTimestamp,
        _errorMessage
    ) { active, packages, minutes, endTs, errorMsg ->

        // Safely synchronize static memory states side-effect free
        FocusStateHolder.isFocusActive = active
        FocusStateHolder.blockedPackages = packages
        FocusStateHolder.focusEndTimestamp = endTs

        val now = System.currentTimeMillis()
        val remaining = (endTs - now).coerceAtLeast(0L)

        // Evaluate timer task attachments safely outside collection calculation streams
        manageTimerEngineLifecycle(active, endTs, remaining)

        FocusModeUiState(
            isFocusActive = active && remaining > 0L,
            remainingMillis = remaining,
            selectedApps = packages,
            selectedMinutes = minutes,
            sessionDurationMinutes = if (active) minutes else 0,
            errorMessage = errorMsg
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FocusModeUiState()
    )

    private fun manageTimerEngineLifecycle(isActive: Boolean, endTs: Long, remaining: Long) {
        if (isActive && remaining > 0L) {
            if (timerJob == null || timerJob?.isActive == false) {
                startTimerInternal(endTs)
            }
        } else if (endTs > 0L && remaining <= 0L) {
            handleSessionExpiry()
        }
    }

    fun toggleAppSelection(packageName: String) {
        val currentPackages = uiState.value.selectedApps
        val newSet = currentPackages.toMutableSet().apply {
            if (contains(packageName)) remove(packageName) else add(packageName)
        }
        viewModelScope.launch {
            prefs.setBlockedPackages(newSet)
            applyActiveSessionChanges(newSet, uiState.value.selectedMinutes)
        }
    }

    fun setSelectedMinutes(minutes: Int) {
        viewModelScope.launch {
            prefs.setSelectedFocusMinutes(minutes)
            applyActiveSessionChanges(uiState.value.selectedApps, minutes)
        }
    }

    fun startFocusSession() {
        val state = uiState.value
        if (state.selectedApps.isEmpty()) {
            _errorMessage.value = "Please select at least one app to block."
            return
        }
        if (state.selectedMinutes <= 0) {
            _errorMessage.value = "Please select a session duration."
            return
        }

        val endTs = System.currentTimeMillis() + (state.selectedMinutes * 60_000L)
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
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                val now = System.currentTimeMillis()
                if (now >= endTs) {
                    handleSessionExpiry()
                    break
                }
            }
        }
    }

    private fun stopTimerInternal() {
        timerJob?.cancel()
        timerJob = null
    }

    private fun applyActiveSessionChanges(packages: Set<String>, minutes: Int) {
        if (!uiState.value.isFocusActive || packages.isEmpty() || minutes <= 0) return
        val newEndTs = System.currentTimeMillis() + (minutes * 60_000L)
        viewModelScope.launch {
            prefs.startFocusSession(newEndTs)
            prefs.setBlockedPackages(packages)
        }
    }

    fun dismissError() {
        _errorMessage.value = null
    }
}