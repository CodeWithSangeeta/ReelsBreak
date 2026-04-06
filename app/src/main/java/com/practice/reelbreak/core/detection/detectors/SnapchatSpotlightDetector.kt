package com.practice.reelbreak.core.detection.detectors


import android.view.accessibility.AccessibilityNodeInfo
import com.practice.reelbreak.domain.model.DetectionResult

/**
 * Snapchat detection strategy:
 * Snapchat Spotlight is the short-video feed. We check for:
 * - "spotlight" in view IDs → Spotlight feed containers
 * - "story_player" → Story/video player views
 * - Screen description check → Snapchat labels Spotlight screens
 */
class SnapchatSpotlightDetector : AppDetector {

    private val spotlightViewIds = listOf(
        "spotlight",
        "story_player",
        "video_player_surface",
        "snap_story"
    )

    override fun detect(rootNode: AccessibilityNodeInfo?): DetectionResult {
        if (rootNode == null) return DetectionResult.UNKNOWN
        return if (containsSpotlightNode(rootNode)) {
            DetectionResult.REELS_SCREEN
        } else {
            DetectionResult.NORMAL_SCREEN
        }
    }

    private fun containsSpotlightNode(node: AccessibilityNodeInfo?): Boolean {
        if (node == null) return false

        val viewId = node.viewIdResourceName ?: ""
        val contentDesc = node.contentDescription?.toString() ?: ""

        if (spotlightViewIds.any { viewId.contains(it, ignoreCase = true) }) return true
        if (contentDesc.contains("Spotlight", ignoreCase = true)) return true

        for (i in 0 until node.childCount) {
            if (containsSpotlightNode(node.getChild(i))) return true
        }
        return false
    }
}