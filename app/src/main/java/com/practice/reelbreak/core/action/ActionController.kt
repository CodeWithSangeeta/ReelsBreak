package com.practice.reelbreak.core.action

import android.accessibilityservice.AccessibilityService
import android.util.Log
import com.practice.reelbreak.core.detection.AppDetectorRouter
import com.practice.reelbreak.core.registry.SupportedAppsRegistry

class ActionController(
    private val service: AccessibilityService
) {
    private val blockController = BlockController(service)

    // ← packageName added — all existing callers work via default null
    fun triggerBlock(packageName: String? = null) {
        Log.d("REELSBREAK", "ActionController: triggerBlock for $packageName")
        when (packageName) {
            SupportedAppsRegistry.FACEBOOK -> {
                // Stay inside Facebook — click Home tab instead of closing
                val navigated = blockController.navigateToFacebookHome()
                if (navigated) {
                    // Programmatic click won't fire a typeViewClicked event
                    // so we reset the tab state manually here
                    AppDetectorRouter.resetAll()
                    Log.d("REELSBREAK", "ActionController: Facebook → Home tab ✅ + state reset")
                }
            }
            // All other apps keep existing behaviour — no change
            else -> {
                blockController.closeCurrentApp()
            }
        }
    }
}