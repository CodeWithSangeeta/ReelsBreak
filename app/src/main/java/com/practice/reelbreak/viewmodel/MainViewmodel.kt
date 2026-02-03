package com.practice.reelbreak.viewmodel


import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.practice.reelbreak.core.permission.AccessibilityPermissionChecker
import com.practice.reelbreak.core.permission.UsagePermissionChecker
import com.practice.reelbreak.data.datastore.OnboardingPreferences
import com.practice.reelbreak.domain.model.PermissionState
import com.practice.reelbreak.ui.permission.PermissionAction
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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



    //Connect Permission Checkers
    fun refreshPermissionState(context: Context) {
        val accessibilityGranted =
            AccessibilityPermissionChecker.isAccessibilityEnabled(context)

        val usageGranted =
            UsagePermissionChecker.isUsageAccessGranted(context)

        Log.d("PERMISSION", "Accessibility=$accessibilityGranted Usage=$usageGranted")

        _permissionState.value = PermissionState(
            accessibilityGranted = accessibilityGranted,
            usageStatsGranted = usageGranted,
            overlayGranted = false
        )
    }


    //Opening System Settings
    private val _permissionAction = MutableSharedFlow<PermissionAction>()
    val permissionAction = _permissionAction.asSharedFlow()
    fun requestAccessibilityPermission() {
        viewModelScope.launch {
            _permissionAction.emit(PermissionAction.OpenAccessibilitySettings)
        }
    }

    fun requestUsagePermission() {
        viewModelScope.launch {
            _permissionAction.emit(PermissionAction.OpenUsageAccessSettings)
        }
    }
}




