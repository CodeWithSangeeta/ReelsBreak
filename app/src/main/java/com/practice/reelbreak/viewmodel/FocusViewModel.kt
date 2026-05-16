package com.practice.reelbreak.ui.focusedmode

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.practice.reelbreak.data.FocusStateHolder
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
    val selectedMinutes: Int = 0,
    val selectedApps: Set<String> = emptySet(),
    val errorMessage: String? = null   // package names
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
                FocusStateHolder.isFocusActive = active
            }
        }
        viewModelScope.launch {
            prefs.focusEndTimestamp.collectLatest { endTs ->
                FocusStateHolder.focusEndTimestamp = endTs
                val now = System.currentTimeMillis()
                when {
                    // ✅ Valid active session — start countdown
                    endTs > 0L && endTs > now -> {
                        startTimerInternal(endTs)
                    }
                    // ✅ Session expired while app was closed — auto-clean
                    endTs > 0L && endTs <= now -> {
                        FocusStateHolder.isFocusActive = false
                        FocusStateHolder.blockedPackages = emptySet()
                        FocusStateHolder.focusEndTimestamp = 0L
                        prefs.stopFocusSession()   // clear stale DataStore value
                        stopTimerInternal()
                    }
                    // ✅ No session — do nothing
                    else -> stopTimerInternal()
                }
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
        val state = _uiState.value

        // ── Validation ──────────────────────────────────────────────────
        if (state.selectedApps.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Please select at least one app to block.") }
            return
        }
        if (state.selectedMinutes <= 0) {
            _uiState.update { it.copy(errorMessage = "Please select a session duration.") }
            return
        }

        val durationMinutes = state.selectedMinutes
        val endTs = System.currentTimeMillis() + durationMinutes.toLong() * 60_000L

        // ── Sync to singleton so AccessibilityService can read it ───────
        FocusStateHolder.isFocusActive = true
        FocusStateHolder.blockedPackages = state.selectedApps
        FocusStateHolder.focusEndTimestamp = endTs

        viewModelScope.launch {
            prefs.startFocusSession(endTs)
        }
        startTimerInternal(endTs)
    }

    fun stopFocusSession() {
        // Clear singleton
        FocusStateHolder.isFocusActive = false
        FocusStateHolder.blockedPackages = emptySet()
        FocusStateHolder.focusEndTimestamp = 0L

        viewModelScope.launch { prefs.stopFocusSession() }
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
        // Clear persisted end timestamp so it doesn't re-trigger on next launch
        viewModelScope.launch {
            prefs.stopFocusSession()
        }
    }


    fun dismissError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
