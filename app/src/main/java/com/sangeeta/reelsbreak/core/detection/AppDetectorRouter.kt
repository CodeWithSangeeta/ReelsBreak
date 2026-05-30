package com.practice.reelbreak.core.detection

import android.view.accessibility.AccessibilityNodeInfo
import com.practice.reelbreak.core.registry.ReelsDetectionRegistry


object AppDetectorRouter {


//    private val VIEW_ID_MAP = mapOf(
//        ReelsDetectionRegistry.YOUTUBE  to "reel_player_page_container",
//        ReelsDetectionRegistry.INSTAGRAM to "clips_viewer_view_pager",
//        ReelsDetectionRegistry.SNAPCHAT  to "spotlight_container"
//    )
//
//    private fun isNodeVisibleToUser(node: AccessibilityNodeInfo): Boolean {
//        if (!node.isVisibleToUser) return false
//        val rect = android.graphics.Rect()
//        node.getBoundsInScreen(rect)
//        return rect.width() > 0 && rect.height() > 0
//    }

        fun isBlockedContentVisible(packageName: String, rootNode: AccessibilityNodeInfo?): Boolean {
            return BlockableAppConfig.isBlockedContentVisible(packageName, rootNode)
        }


//    fun resetAll() {
//    }
}