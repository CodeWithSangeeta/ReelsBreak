package com.practice.reelbreak.ui.permission

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



enum class PermissionType {
    ACCESSIBILITY,
    USAGE_ACCESS,
    OVERLAY
}