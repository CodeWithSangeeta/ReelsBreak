package com.sangeeta.reelbreak.viewmodel


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    private val _isOnboardingCompleted = MutableStateFlow(false)
    val isOnboardingCompleted: StateFlow<Boolean> = _isOnboardingCompleted

    // Temporary (we’ll replace with DataStore later)
    fun setOnboardingCompleted(completed: Boolean) {
        _isOnboardingCompleted.value = completed
    }
}
