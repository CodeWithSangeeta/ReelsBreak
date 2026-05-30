//package com.practice.reelbreak.core.action
//
//import android.accessibilityservice.AccessibilityService
//import com.practice.reelbreak.core.debug.AccessibilityDebugLogger
//class BlockController(
//    private val service: AccessibilityService
//) {
//    fun goBackInsideApp() {
//        val pkg = service.rootInActiveWindow?.packageName?.toString()
//        val backWorked = service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
//        AccessibilityDebugLogger.logAction(
//            pkg,
//            if (backWorked) "GLOBAL_ACTION_BACK" else "GLOBAL_ACTION_BACK_FAILED",
//            "reels_block"
//        )
//    }
//
//    fun goHome() {
//        val pkg = service.rootInActiveWindow?.packageName?.toString()
//        val homeWorked = service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME)
//        AccessibilityDebugLogger.logAction(
//            pkg,
//            if (homeWorked) "GLOBAL_ACTION_HOME" else "GLOBAL_ACTION_HOME_FAILED",
//            "full_app_block"
//        )
//    }
//}




package com.practice.reelbreak.core.action

import android.accessibilityservice.AccessibilityService
import android.util.Log
import com.practice.reelbreak.core.debug.AccessibilityDebugLogger

class BlockController(
    private val service: AccessibilityService
) {
    private val TAG = "RB_BLOCK_CTRL"

    /**
     * Safely executes a back navigation gesture only if the service actively targets the specified app space.
     */
    fun goBackInsideApp(expectedPackage: String?) {
        val root = service.rootInActiveWindow
        val activePackage = root?.packageName?.toString()

        // Safety guard: If the user has navigated away from the target app, abort immediately
        if (expectedPackage != null && activePackage != expectedPackage) {
            Log.w(TAG, "Block aborted: Active package ($activePackage) doesn't match target ($expectedPackage)")
            root?.recycle()
            return
        }
        root?.recycle()

        val backWorked = service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)

        AccessibilityDebugLogger.logAction(
            activePackage,
            if (backWorked) "GLOBAL_ACTION_BACK" else "GLOBAL_ACTION_BACK_FAILED",
            "reels_block"
        )
    }

    /**
     * Forces system navigation safely back to the home launcher environment.
     */
    fun goHome(expectedPackage: String?) {
        val root = service.rootInActiveWindow
        val activePackage = root?.packageName?.toString()
        root?.recycle()

        // Execute safety reset option to system launcher
        val homeWorked = service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME)

        AccessibilityDebugLogger.logAction(
            activePackage,
            if (homeWorked) "GLOBAL_ACTION_HOME" else "GLOBAL_ACTION_HOME_FAILED",
            "full_app_block"
        )
    }
}