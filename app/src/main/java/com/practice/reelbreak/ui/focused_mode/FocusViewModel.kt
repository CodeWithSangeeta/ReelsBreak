package com.practice.reelbreak.ui.focusedmode

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.practice.reelbreak.data.preferences.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FocusModeUiState(
    val isFocusActive: Boolean = false,
    val remainingMillis: Long = 0L,
    val selectedMinutes: Int = 30,
    val selectedApps: Set<String> = emptySet() // package names
)

@HiltViewModel
class FocusViewModel @Inject constructor(
    private val prefs: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FocusModeUiState())
    val uiState: StateFlow<FocusModeUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            prefs.isFocusActive.collectLatest { active ->
                _uiState.update { it.copy(isFocusActive = active) }
            }
        }
        viewModelScope.launch {
            prefs.focusEndTimestamp.collectLatest { endTs ->
                if (endTs > 0L) startTimerInternal(endTs) else stopTimerInternal()
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
    }

    fun setSelectedMinutes(minutes: Int) {
        _uiState.update { it.copy(selectedMinutes = minutes) }
    }

    fun startFocusSession() {
        val durationMinutes = _uiState.value.selectedMinutes
        val endTs = System.currentTimeMillis() + durationMinutes.toLong() * 60_000L

        viewModelScope.launch {
            prefs.startFocusSession(endTs)
        }
        startTimerInternal(endTs)
    }

    fun stopFocusSession() {
        viewModelScope.launch {
            prefs.stopFocusSession()
        }
        stopTimerInternal()
    }

    private fun startTimerInternal(endTs: Long) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                val now = System.currentTimeMillis()
                val remaining = (endTs - now).coerceAtLeast(0L)
                _uiState.update { it.copy(remainingMillis = remaining, isFocusActive = remaining > 0) }
                if (remaining <= 0L) {
                    prefs.stopFocusSession()
                    break
                }
                delay(1_000L)
            }
        }
    }

    private fun stopTimerInternal() {
        timerJob?.cancel()
        timerJob = null
        _uiState.update { it.copy(remainingMillis = 0L, isFocusActive = false) }
    }
}
