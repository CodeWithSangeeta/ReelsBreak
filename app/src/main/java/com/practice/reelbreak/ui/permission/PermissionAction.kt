package com.practice.reelbreak.ui.permission

sealed class PermissionAction {
    object OpenAccessibilitySettings : PermissionAction()
    object OpenUsageAccessSettings : PermissionAction()
}