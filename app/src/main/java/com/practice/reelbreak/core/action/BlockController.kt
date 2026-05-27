package com.practice.reelbreak.core.action

import android.accessibilityservice.AccessibilityService
import com.practice.reelbreak.core.debug.AccessibilityDebugLogger
class BlockController(
    private val service: AccessibilityService
) {
    fun goBackInsideApp() {
        val pkg = service.rootInActiveWindow?.packageName?.toString()
        val backWorked = service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
        AccessibilityDebugLogger.logAction(
            pkg,
            if (backWorked) "GLOBAL_ACTION_BACK" else "GLOBAL_ACTION_BACK_FAILED",
            "reels_block"
        )
    }

    fun goHome() {
        val pkg = service.rootInActiveWindow?.packageName?.toString()
        val homeWorked = service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME)
        AccessibilityDebugLogger.logAction(
            pkg,
            if (homeWorked) "GLOBAL_ACTION_HOME" else "GLOBAL_ACTION_HOME_FAILED",
            "full_app_block"
        )
    }
}