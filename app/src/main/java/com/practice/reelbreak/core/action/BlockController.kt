package com.practice.reelbreak.core.action

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.practice.reelbreak.core.debug.AccessibilityDebugLogger

//class BlockController(
//    private val service: AccessibilityService
//) {
//    fun closeCurrentApp() {
//        val pkg = service.rootInActiveWindow?.packageName?.toString()
//
//        val backWorked = service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
//
//        if (backWorked) {
//            AccessibilityDebugLogger.logAction(
//                packageName = pkg,
//                action = "GLOBAL_ACTION_BACK",
//                reason = "block_short_video"
//            )
//            Log.d("REELSBREAK", "BlockController: GLOBAL_ACTION_BACK")
//        } else {
//            AccessibilityDebugLogger.logAction(
//                packageName = pkg,
//                action = "GLOBAL_ACTION_HOME",
//                reason = "block_short_video_back_failed"
//            )
//            Log.d("REELSBREAK", "BlockController: BACK failed, fallback HOME")
//            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME)
//        }
//    }
//
//    private fun findNodeByDescription(
//        node: AccessibilityNodeInfo?,
//        description: String
//    ): AccessibilityNodeInfo? {
//        if (node == null) return null
//        if (node.contentDescription?.toString()
//                ?.equals(description, ignoreCase = true) == true) return node
//        for (i in 0 until node.childCount) {
//            val result = findNodeByDescription(node.getChild(i), description)
//            if (result != null) return result
//        }
//        return null
//    }
//}


class BlockController(
    private val service: AccessibilityService
) {
    fun closeCurrentApp() {
        val pkg = service.rootInActiveWindow?.packageName?.toString()
        val backWorked = service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)

        if (backWorked) {
            AccessibilityDebugLogger.logAction(packageName = pkg, action = "GLOBAL_ACTION_BACK", reason = "block_short_video")
        } else {
            AccessibilityDebugLogger.logAction(packageName = pkg, action = "GLOBAL_ACTION_HOME", reason = "block_short_video_back_failed")
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME)
        }
    }

    private fun findNodeByDescription(
        node: AccessibilityNodeInfo?,
        description: String
    ): AccessibilityNodeInfo? {
        if (node == null) return null

        if (node.contentDescription?.toString()?.equals(description, ignoreCase = true) == true) {
            return node
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            val result = findNodeByDescription(child, description)

            if (result != null) {
                // We found a match! Return it up the call stack, but make sure to
                // recycle the child node reference if it wasn't the exact one we are keeping.
                return result
            }

            // CRITICAL: If the child node tree didn't contain our target description,
            // free it from active framework memory immediately.
            child.recycle()
        }
        return null
    }
}