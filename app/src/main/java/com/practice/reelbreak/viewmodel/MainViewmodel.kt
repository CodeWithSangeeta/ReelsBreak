package com.practice.reelbreak.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.practice.reelbreak.data.datastore.OnboardingPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _isOnboardingCompleted = MutableStateFlow(false)
    val isOnboardingCompleted: StateFlow<Boolean> = _isOnboardingCompleted

    init {
        observeOnboardingState()
    }

    private fun observeOnboardingState() {
        viewModelScope.launch {
            OnboardingPreferences
                .isOnboardingCompleted(getApplication())
                .collect { completed ->
                    _isOnboardingCompleted.value = completed
                }
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            OnboardingPreferences.setOnboardingCompleted(getApplication())
        }
    }

}




