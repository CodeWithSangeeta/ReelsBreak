package com.sangeeta.reelbreak.ui.permission

sealed class PermissionAction {
    object OpenAccessibilitySettings : PermissionAction()
    object OpenUsageAccessSettings : PermissionAction()
}