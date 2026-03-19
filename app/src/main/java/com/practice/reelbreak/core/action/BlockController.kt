package com.practice.reelbreak.core.action


import android.accessibilityservice.AccessibilityService
import android.util.Log

class BlockController(
    private val service: AccessibilityService
) {

    fun closeCurrentApp() {
        Log.d("REELSBREAK", "Closing app")
        service.performGlobalAction(
            AccessibilityService.GLOBAL_ACTION_BACK
        )

    }
}