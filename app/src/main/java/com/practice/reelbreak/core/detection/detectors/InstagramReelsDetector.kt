package com.practice.reelbreak.core.detection.detectors


import android.view.accessibility.AccessibilityNodeInfo
import com.practice.reelbreak.domain.model.DetectionResult

/**
 * Instagram detection strategy:
 * Instagram Reels tab shows a vertical video feed. We detect it by
 * looking for view IDs that only appear in the Reels section:
 * - "clips_viewer_video_container" → the full-screen reel player
 * - "reel_viewer_root"             → root of the reels viewer
 *
 * WHY not use SurfaceView check?
 * Instagram uses its own video pipeline which doesn't always expose
 * standard SurfaceView nodes to the accessibility tree.
 */
class InstagramReelsDetector : AppDetector {

    // View IDs that confirm we're in the Reels section
    private val reelsViewIds = listOf(
        "clips_viewer_video_container",
        "reel_viewer_root",
        "clips_tab_chevron",          // Reels tab indicator
        "reels_tray_container"
    )

    override fun detect(rootNode: AccessibilityNodeInfo?): DetectionResult {
        if (rootNode == null) return DetectionResult.UNKNOWN
        return if (containsReelsNode(rootNode)) {
            DetectionResult.REELS_SCREEN
        } else {
            DetectionResult.NORMAL_SCREEN
        }
    }

    private fun containsReelsNode(node: AccessibilityNodeInfo?): Boolean {
        if (node == null) return false

        val viewId = node.viewIdResourceName ?: ""
        // Check if this node's ID contains any of our reel indicators
        if (reelsViewIds.any { viewId.contains(it) }) return true

        // Recurse into children
        for (i in 0 until node.childCount) {
            if (containsReelsNode(node.getChild(i))) return true
        }
        return false
    }
}