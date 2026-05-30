package com.sangeeta.reelsbreak.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sangeeta.reelsbreak.ui.onboarding.OnboardingPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _isOnboardingCompleted = MutableStateFlow<Boolean?>(null)
    val isOnboardingCompleted: StateFlow<Boolean?> = _isOnboardingCompleted.asStateFlow()
    suspend fun preloadOnboardingStatus(context: Context) {
        val completed = OnboardingPreferences.isOnboardingCompleted(context).first()
        _isOnboardingCompleted.value = completed
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            OnboardingPreferences.setOnboardingCompleted(getApplication())
            _isOnboardingCompleted.value = true
        }
    }

}




