package com.sangeeta.reelbreak.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangeeta.reelbreak.data.datastore.OnboardingPreferences
import com.sangeeta.reelbreak.domain.model.PermissionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _isOnboardingCompleted = MutableStateFlow(false)
    val isOnboardingCompleted: StateFlow<Boolean> = _isOnboardingCompleted


    //Permission state
    private val _permissionState =
        MutableStateFlow(PermissionState())

    val permissionState: StateFlow<PermissionState> =
        _permissionState




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
