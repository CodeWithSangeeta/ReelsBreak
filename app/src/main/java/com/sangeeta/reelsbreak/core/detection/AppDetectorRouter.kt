package com.sangeeta.reelsbreak.core.detection

import android.view.accessibility.AccessibilityNodeInfo

object AppDetectorRouter {

    fun isBlockedContentVisible(packageName: String, rootNode: AccessibilityNodeInfo?): Boolean {
        if (rootNode == null) return false

        return when (packageName) {
            "com.google.android.youtube" -> {
//                checkViewId(rootNode, "$packageName:id/reel_player_page_container") ||
//                        hasClassNameMatch(rootNode, "ReelPlayerPageContainer")

                checkViewId(rootNode, "$packageName:id/reel_player_page_container") ||
                        checkViewId(rootNode, "$packageName:id/reel_time_bar") ||
                        hasClassNameMatch(rootNode, "ReelPlayerPageContainer")
            }

            "com.instagram.android" -> {
                checkViewId(rootNode, "$packageName:id/clips_viewer_view_pager") ||
                        checkViewId(rootNode, "$packageName:id/reels_viewer_container")
            }

            "com.instagram.lite" -> {
                checkViewId(rootNode, "$packageName:id/carbon_sound_image_view") ||
                        checkViewId(rootNode, "$packageName:id/horizontalProgressV2")
            }

            "com.snapchat.android" -> {
                checkViewId(rootNode, "$packageName:id/spotlight_container") ||
                        checkViewId(rootNode, "$packageName:id/vertical_view_pager")
            }

            "com.facebook.katana" -> {
                // Captures both dedicated video tab layouts and feed overlay click popups
                checkViewId(rootNode, "$packageName:id/reels_video_view_pager") ||
                        checkViewId(rootNode, "$packageName:id/reels_fullscreen_container") ||
                        checkViewId(rootNode, "$packageName:id/reels_viewer_activity_container")
            }

            "com.facebook.lite" -> {
                // FIXED: Targets the structural signature discovered in your logs (SeekBar visibility inside list components)
                hasSeekBarSignature(rootNode)
            }

            else -> false
        }
    }

    private fun checkViewId(rootNode: AccessibilityNodeInfo, fullViewId: String): Boolean {
        val nodes = rootNode.findAccessibilityNodeInfosByViewId(fullViewId)
        val matches = nodes.orEmpty().any { it.isVisibleToUser }
        nodes?.forEach { it.recycle() }
        return matches
    }

    private fun hasClassNameMatch(rootNode: AccessibilityNodeInfo, targetClassName: String): Boolean {
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(rootNode)
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (current.className?.toString()?.contains(targetClassName, ignoreCase = true) == true) {
                while (queue.isNotEmpty()) queue.removeFirst().recycle()
                return true
            }
            for (i in 0 until current.childCount) {
                current.getChild(i)?.let { queue.addLast(it) }
            }
            if (current != rootNode) current.recycle()
        }
        return false
    }

    private fun hasSeekBarSignature(rootNode: AccessibilityNodeInfo): Boolean {
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(rootNode)
        var foundSeekBar = false

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()

            if (current.isVisibleToUser && current.className?.toString()?.contains("SeekBar", ignoreCase = true) == true) {
                foundSeekBar = true
                while (queue.isNotEmpty()) queue.removeFirst().recycle()
                break
            }

            for (i in 0 until current.childCount) {
                current.getChild(i)?.let { queue.addLast(it) }
            }
            if (current != rootNode) current.recycle()
        }
        return foundSeekBar
    }

    private fun hasFootprintMatch(rootNode: AccessibilityNodeInfo, className: String, token: String): Boolean {
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(rootNode)

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (current.isVisibleToUser) {
                val clsMatches = current.className?.toString()?.contains(className, ignoreCase = true) == true
                val descMatches = current.contentDescription?.toString()?.contains(token, ignoreCase = true) == true
                if (clsMatches && descMatches) {
                    while (queue.isNotEmpty()) queue.removeFirst().recycle()
                    return true
                }
            }
            for (i in 0 until current.childCount) {
                current.getChild(i)?.let { queue.addLast(it) }
            }
            if (current != rootNode) current.recycle()
        }
        return false
    }
}
