package com.practice.reelbreak.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.reelbreak.core.permission.AccessibilityPermissionChecker
import com.practice.reelbreak.core.permission.UsagePermissionChecker
import com.practice.reelbreak.domain.model.PermissionState
import com.practice.reelbreak.ui.permission.PermissionAction
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import com.practice.reelbreak.ui.permission.PermissionStatus
import com.practice.reelbreak.ui.permission.PermissionType
import com.practice.reelbreak.ui.permission.PermissionUiModel
import com.practice.reelbreak.ui.permission.PermissionUiState
import kotlinx.coroutines.flow.asStateFlow

class PermissionsViewModel : ViewModel(){

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


private fun buildPermissionCards(state: PermissionState): List<PermissionUiModel> {
    return listOf(
        //Accessibility
        PermissionUiModel(
            id = PermissionType.ACCESSIBILITY,
            title = "Accessibility Access",
            icon = Icons.Default.Visibility,
            description = "Used only to detect when you scroll reels and trigger mindful breaks.",
            status = if (state.accessibilityGranted) PermissionStatus.Granted else PermissionStatus.NotGranted,
            buttonTextGranted = "Enabled ✓",
            buttonTextNotGranted = "Enable",
            buttonColorGranted = Color(0xFF8E44AD),
            buttonColorNotGranted = Color(0xFF8E44AD),
            bulletPoints = listOf(
                "Detect reel scrolling event",
                "No reading messages",
                "No screen recording",
                "No personal content access"
            )
        ),

        //Usage Access
        PermissionUiModel(
            id = PermissionType.USAGE_ACCESS,
            title = "Usage Access",
            icon = Icons.Default.BarChart,
            description = "Used to calculate daily time spent on Instagram, YouTube Shorts, TikTok, etc.",
            status = if (state.usageStatsGranted) PermissionStatus.Granted else PermissionStatus.NotGranted,
            buttonTextGranted = "Enabled ✓",
            buttonTextNotGranted = "Enable",
            buttonColorGranted = Color(0xFF3498DB),
            buttonColorNotGranted = Color(0xFF3498DB),
            bulletPoints = listOf(
                "Measure time spent per app",
                "Daily usage summary",
                "No browsing history collected"
            )
        ),
        PermissionUiModel(
            id = PermissionType.OVERLAY,
            title = "Overlay Permission",
            icon = Icons.Default.Fullscreen,
            description = "Optional. Helps show reminders above other apps.",
            status = if (state.overlayGranted) PermissionStatus.Granted else PermissionStatus.NotGranted,
            isOptional = true,
            buttonTextGranted = "Enabled ✓",
            buttonTextNotGranted = "Allow Overlay",
            buttonColorGranted = Color(0xFF3498DB),
            buttonColorNotGranted = Color(0xFF3498DB),
            bulletPoints = listOf(
                "Why is this useful?",
            "Track reels in real time with small counter bubble",
            "ReelsGuard does not record screen or collect data"
            )
        )
    )
}

    fun onCardExpansionToggled(cardId: PermissionType) {
        _uiState.value = _uiState.value.copy(
            expandedCardId = if (_uiState.value.expandedCardId == cardId) null else cardId
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
