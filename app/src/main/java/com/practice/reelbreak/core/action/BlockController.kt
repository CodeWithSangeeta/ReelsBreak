package com.practice.reelbreak.core.action

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.practice.reelbreak.core.debug.AccessibilityDebugLogger

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
                return result
            }
        }
        return null
    }
}
