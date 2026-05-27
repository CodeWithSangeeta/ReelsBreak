//package com.practice.reelbreak.core.detection
//
//import com.practice.reelbreak.core.detection.detectors.AppDetector
//import com.practice.reelbreak.core.detection.detectors.InstagramReelsDetector
//import com.practice.reelbreak.core.detection.detectors.SnapchatReelsDetector
//import com.practice.reelbreak.core.detection.detectors.YouTubeReelsDetector
//import com.practice.reelbreak.core.registry.ReelsDetectionRegistry
//
//object AppDetectorRouter {
//    private val youtubeDetector = YouTubeReelsDetector()
//    private val instagramDetector = InstagramReelsDetector()
//    private val snapchatDetector = SnapchatReelsDetector()
//
//    fun getDetector(packageName: String?): AppDetector? {
//
//        return when (packageName) {
//
//            ReelsDetectionRegistry.YOUTUBE -> {
//                youtubeDetector
//            }
//
//            ReelsDetectionRegistry.INSTAGRAM -> {
//                instagramDetector
//            }
//
//            ReelsDetectionRegistry.SNAPCHAT -> {
//                snapchatDetector
//            }
//
//            else -> {
//              null
//            }
//        }
//    }
//
//
//    fun resetAll() {
//        instagramDetector.reset()
//        snapchatDetector.reset()
//        youtubeDetector.reset()
//    }
//}



package com.practice.reelbreak.core.detection

import android.view.accessibility.AccessibilityNodeInfo
import com.practice.reelbreak.core.registry.ReelsDetectionRegistry

/**
 * Config-driven detection — no per-app detector classes needed.
 * Each app has one stable view ID that confirms the user is on Shorts/Reels/Spotlight.
 * Sourced from Scrolless open-source project (verified stable).
 */
object AppDetectorRouter {

    // View IDs confirmed stable across app updates:
    // YouTube  → "reel_player_page_container"   (Shorts player)
    // Instagram → "clips_viewer_view_pager"      (Reels viewer)
    // Snapchat  → "spotlight_container"           (Spotlight feed)

    private val VIEW_ID_MAP = mapOf(
        ReelsDetectionRegistry.YOUTUBE  to "reel_player_page_container",
        ReelsDetectionRegistry.INSTAGRAM to "clips_viewer_view_pager",
        ReelsDetectionRegistry.SNAPCHAT  to "spotlight_container"
    )

    /**
     * Returns true if the given rootNode contains the blocked content view for this package.
     * Returns false if rootNode is null or package is not in our map.
     */
//    fun isBlockedContentVisible(packageName: String, rootNode: AccessibilityNodeInfo?): Boolean {
//        if (rootNode == null) return false
//        val viewId = VIEW_ID_MAP[packageName] ?: return false
//        val fullViewId = "$packageName:id/$viewId"
//        val nodes = rootNode.findAccessibilityNodeInfosByViewId(fullViewId)
//        val found = nodes.any { isNodeVisibleToUser(it) }
//        // Always recycle found nodes to prevent memory leaks
//        nodes.forEach { it.recycle() }
//        return found
//    }

    /**
     * A node is only considered visible if Android reports it visible
     * AND it has non-zero dimensions on screen.
     */
    private fun isNodeVisibleToUser(node: AccessibilityNodeInfo): Boolean {
        if (!node.isVisibleToUser) return false
        val rect = android.graphics.Rect()
        node.getBoundsInScreen(rect)
        return rect.width() > 0 && rect.height() > 0
    }


    /**
     * Thin wrapper kept for compatibility with ReelsDetectionManager.
     * All detection logic lives in BlockableAppConfig.
     */

        fun isBlockedContentVisible(packageName: String, rootNode: AccessibilityNodeInfo?): Boolean {
            return BlockableAppConfig.isBlockedContentVisible(packageName, rootNode)
        }


    /**
     * Called on service interrupt — kept for compatibility.
     */
    fun resetAll() {
        // No per-detector state to reset in config-driven approach
    }
}