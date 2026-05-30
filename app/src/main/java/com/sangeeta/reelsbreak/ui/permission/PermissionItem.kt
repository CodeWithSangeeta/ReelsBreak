package com.sangeeta.reelsbreak.ui.permission

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.sangeeta.reelsbreak.domain.model.PermissionState

data class PermissionUiModel(
    val title: String,
    val icon: ImageVector,
    val description: String,
    val isOptional: Boolean = false,
    val bulletPoints: List<String> = emptyList(),
    val buttonTextGranted: String = "Enabled ",
    val buttonTextNotGranted: String,
    val buttonColorGranted: Color = Color.Black,
    val buttonColorNotGranted: Color,
)


data class PermissionUiState(
    val permissionState: PermissionState = PermissionState(),
    val permissionCards: List<PermissionUiModel> = emptyList(),
    val isContinueEnabled: Boolean = false,
)



