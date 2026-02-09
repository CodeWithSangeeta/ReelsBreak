package com.practice.reelbreak.domain.model


data class PermissionState(
    val accessibilityGranted: Boolean = false,
    val usageStatsGranted: Boolean = false,
   val overlayGranted: Boolean = false
){
    val requiredGranted: Boolean
        get() = accessibilityGranted && usageStatsGranted

    val allGranted: Boolean
        get() = accessibilityGranted && usageStatsGranted && overlayGranted

}
