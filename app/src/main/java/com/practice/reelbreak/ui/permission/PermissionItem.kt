package com.practice.reelbreak.ui.permission

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.practice.reelbreak.domain.model.PermissionState

sealed class PermissionStatus {
    object Granted : PermissionStatus()
    object NotGranted : PermissionStatus()
}


data class PermissionUiModel(
    val id: PermissionType,
    val title: String,
    val icon: ImageVector,
    val description: String,
    val status: PermissionStatus,
    val isOptional: Boolean = false,
    val bulletPoints: List<String> = emptyList(),
    val buttonTextGranted: String = "Enabled ✓",
    val buttonTextNotGranted: String,
    val buttonColorGranted: Color = Color.Black,
    val buttonColorNotGranted: Color
)


data class PermissionUiState(
    val permissionState: PermissionState = PermissionState(),
    val permissionCards: List<PermissionUiModel> = emptyList(),
    val isContinueEnabled: Boolean = false,
    val expandedCardId: PermissionType? = null
)

enum class PermissionType {
    ACCESSIBILITY,
    USAGE_ACCESS,
    OVERLAY
}


enum class BulletIconType {
    Check, Cross, Question
}
