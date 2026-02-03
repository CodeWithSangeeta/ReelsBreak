package com.practice.reelbreak.ui.permission

import androidx.compose.ui.graphics.vector.ImageVector

data class PermissionUiModel(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val isGranted: Boolean
)
