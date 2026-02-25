//package com.practice.reelbreak.ui.focused_mode
//
//import androidx.compose.runtime.mutableStateOf
//import androidx.lifecycle.ViewModel
//
//// FocusViewModel.kt - MVVM ViewModel
//class FocusViewModel : ViewModel() {
//    private val _selectedTime = mutableStateOf(15 * 60 * 1000L) // Default 15m in ms
//    val selectedTime: State<Long> = _selectedTime
//
//    private val _blockedApps = mutableStateOf(
//        setOf("Instagram", "YouTube", "Facebook", "TikTok")
//    )
//    val blockedApps: State<Set<String>> = _blockedApps
//
//    fun selectTime(minutes: Int) {
//        _selectedTime.value = minutes * 60 * 1000L
//    }
//
//    fun toggleApp(app: String) {
//        val current = _blockedApps.value
//        _blockedApps.value = if (current.contains(app)) {
//            current - app
//        } else {
//            current + app
//        }
//    }
//
//    fun startFocus() {
//        // Navigate or start service
//        println("Starting focus with ${selectedTime.value / 1000 / 60}m, blocking ${_blockedApps.value}")
//    }
//}
