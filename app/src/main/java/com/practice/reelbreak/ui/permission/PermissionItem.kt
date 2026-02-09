package com.practice.reelbreak.ui.permission

import com.practice.reelbreak.domain.model.PermissionState

sealed class PermissionStatus {
    object Granted : PermissionStatus()
    object NotGranted : PermissionStatus()
}


data class PermissionUiModel(
    val id: PermissionType,
    val title: String,
    val description: String,
    val status: PermissionStatus,
    val isOptional: Boolean = false,
    val canExpand: Boolean = true,
    val bulletPoints: List<String> = emptyList()
)


data class PermissionUiState(
    val permissionState: PermissionState = PermissionState(),
    val permissionCards: List<PermissionUiModel> = emptyList(),
    val isContinueEnabled: Boolean = false
)

enum class PermissionType {
    ACCESSIBILITY,
    USAGE_ACCESS,
    OVERLAY
}