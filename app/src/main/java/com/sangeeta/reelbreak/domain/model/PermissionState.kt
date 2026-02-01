package com.sangeeta.reelbreak.domain.model


data class PermissionState(
    val accessibilityGranted: Boolean = false,
    val usageStatsGranted: Boolean = false,
    val overlayGranted: Boolean = false
)
