package com.practice.reelbreak.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.reelbreak.core.permission.AccessibilityPermissionChecker
import com.practice.reelbreak.core.permission.UsagePermissionChecker
import com.practice.reelbreak.domain.model.PermissionState
import com.practice.reelbreak.ui.permission.PermissionAction
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.practice.reelbreak.ui.permission.PermissionStatus
import com.practice.reelbreak.ui.permission.PermissionType
import com.practice.reelbreak.ui.permission.PermissionUiModel
import com.practice.reelbreak.ui.permission.PermissionUiState
import kotlinx.coroutines.flow.asStateFlow

class PermissionsViewModel : ViewModel(){


//    //Permission state
//    private val _permissionState = MutableStateFlow(PermissionState())
//
//    val permissionState: StateFlow<PermissionState> =
//        _permissionState

    private val _uiState = MutableStateFlow(PermissionUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<PermissionAction>()
    val events = _events.asSharedFlow()

    //Connect Permission Checkers
    fun refreshPermissionState(context: Context) {
        val accessibilityGranted =
            AccessibilityPermissionChecker.isAccessibilityEnabled(context)

        val usageGranted =
            UsagePermissionChecker.isUsageAccessGranted(context)

        val overlayGranted = false // later you will implement real checker

        Log.d("PERMISSION", "Accessibility=$accessibilityGranted Usage=$usageGranted")

        val state = PermissionState(
            accessibilityGranted = accessibilityGranted,
            usageStatsGranted = usageGranted,
            overlayGranted = overlayGranted
        )

        _uiState.value = PermissionUiState(
            permissionState = state,
            permissionCards = buildPermissionCards(state),
            isContinueEnabled = state.requiredGranted
        )
    }

//    //Opening System Settings
//    private val _permissionAction = MutableSharedFlow<PermissionAction>()
//    val permissionAction = _permissionAction.asSharedFlow()
//    fun requestAccessibilityPermission() {
//        viewModelScope.launch {
//            _permissionAction.emit(PermissionAction.OpenAccessibilitySettings)
//        }
//    }
//
//    fun requestUsagePermission() {
//        viewModelScope.launch {
//            _permissionAction.emit(PermissionAction.OpenUsageAccessSettings)
//        }
//    }



private fun buildPermissionCards(state: PermissionState): List<PermissionUiModel> {
    return listOf(
        PermissionUiModel(
            id = PermissionType.ACCESSIBILITY,
            title = "Accessibility Access",
            description = "Required to detect reels and show reminders.",
            status = if (state.accessibilityGranted) PermissionStatus.Granted else PermissionStatus.NotGranted,
            bulletPoints = listOf(
                "Detect reel scrolling",
                "Show break reminder overlay"
            )
        ),
        PermissionUiModel(
            id = PermissionType.USAGE_ACCESS,
            title = "Usage Access",
            description = "Required to track time spent on reels.",
            status = if (state.usageStatsGranted) PermissionStatus.Granted else PermissionStatus.NotGranted,
            bulletPoints = listOf(
                "Measure screen time",
                "Detect short-form app usage"
            )
        ),
        PermissionUiModel(
            id = PermissionType.OVERLAY,
            title = "Overlay Permission",
            description = "Optional. Helps show reminders above other apps.",
            status = if (state.overlayGranted) PermissionStatus.Granted else PermissionStatus.NotGranted,
            isOptional = true,
            bulletPoints = listOf(
                "Show reminder popups on top",
                "Improves user experience"
            )
        )
    )
}


fun onPermissionEnableClicked(type: PermissionType) {
    viewModelScope.launch {
        when (type) {
            PermissionType.ACCESSIBILITY ->
                _events.emit(PermissionAction.OpenAccessibilitySettings)

            PermissionType.USAGE_ACCESS ->
                _events.emit(PermissionAction.OpenUsageAccessSettings)

            PermissionType.OVERLAY ->
                _events.emit(PermissionAction.OpenOverlaySettings)
        }
    }
}


}
