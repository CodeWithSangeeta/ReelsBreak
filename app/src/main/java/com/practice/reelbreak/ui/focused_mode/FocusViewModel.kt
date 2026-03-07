package com.practice.reelbreak.ui.focusedmode

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FocusViewModel : ViewModel() {
    private val _selectedTime = MutableStateFlow(15 * 60 * 1000L) // 15 minutes in ms
    val selectedTime: StateFlow<Long> = _selectedTime.asStateFlow()

    private val _blockedApps = MutableStateFlow(setOf("Instagram", "YouTube", "Facebook", "TikTok"))
    val blockedApps: StateFlow<Set<String>> = _blockedApps.asStateFlow()

    private val _isFocusActive = MutableStateFlow(false)
    val isFocusActive: StateFlow<Boolean> = _isFocusActive.asStateFlow()

    private val _remainingTime = MutableStateFlow(15 * 60 * 1000L)
    val remainingTime: StateFlow<Long> = _remainingTime.asStateFlow()

    fun selectTime(minutes: Int) {
        _selectedTime.value = minutes * 60 * 1000L
        if (!_isFocusActive.value) {
            _remainingTime.value = minutes * 60 * 1000L
        }
    }

    fun toggleApp(app: String) {
        val currentApps = _blockedApps.value
        _blockedApps.value = if (currentApps.contains(app)) {
            currentApps - app
        } else {
            currentApps + app
        }
    }

    fun startFocus() {
        _isFocusActive.value = true
        // TODO: Start actual blocking service here
    }

    fun stopFocus() {
        _isFocusActive.value = false
    }
}
