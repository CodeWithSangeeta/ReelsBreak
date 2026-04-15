package com.practice.reelbreak.core.action


//import android.accessibilityservice.AccessibilityService
//import android.util.Log
//
//class BlockController(
//    private val service: AccessibilityService
//) {
//
//    fun closeCurrentApp() {
//        Log.d("REELSBREAK", "Closing app")
//        service.performGlobalAction(
//            AccessibilityService.GLOBAL_ACTION_BACK
//        )
//
//    }
//}


import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo

class BlockController(
    private val service: AccessibilityService
) {
    // Used by YouTube, Snapchat, TikTok, Instagram — unchanged behaviour
    fun closeCurrentApp() {
        Log.d("REELSBREAK", "BlockController: GLOBAL_ACTION_BACK")
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
    }

    // Used ONLY by Facebook — navigates to Home tab, stays inside app
    fun navigateToFacebookHome(): Boolean {
        val rootNode = service.rootInActiveWindow
        if (rootNode == null) {
            Log.d("REELSBREAK", "BlockController: rootNode null → fallback BACK")
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
            return false
        }

        val homeTabNode = findNodeByDescription(rootNode, "Home, tab 1 of 6")
        return if (homeTabNode != null) {
            val clicked = homeTabNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            homeTabNode.recycle()
            Log.d("REELSBREAK", "BlockController: Facebook Home tab clicked → success=$clicked")
            clicked
        } else {
            // Home tab node not found — use back as fallback
            Log.d("REELSBREAK", "BlockController: Home tab not found → GLOBAL_ACTION_BACK")
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
            false
        }
    }

    private fun findNodeByDescription(
        node: AccessibilityNodeInfo?,
        description: String
    ): AccessibilityNodeInfo? {
        if (node == null) return null
        if (node.contentDescription?.toString()
                ?.equals(description, ignoreCase = true) == true) return node
        for (i in 0 until node.childCount) {
            val result = findNodeByDescription(node.getChild(i), description)
            if (result != null) return result
        }
        return null
    }
}