package com.practice.reelbreak.core.detection.detectors

import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.practice.reelbreak.domain.model.DetectionResult


//class InstagramReelsDetector : AppDetector {
//
//    override fun detect(rootNode: AccessibilityNodeInfo?): DetectionResult {
//        if (rootNode == null) return DetectionResult.NORMAL_SCREEN
//
//        // STRATEGY 1 (Most Reliable):
//        // "Reel by XYZ. Double tap to play or pause." only appears when
//        // a Reel is actually playing in the foreground viewport.
//        val clipsVideoContainers = rootNode.findAccessibilityNodeInfosByViewId(
//            "com.instagram.android:id/clips_video_container"
//        )
//        val hasActiveReel = clipsVideoContainers.any { node ->
//            val desc = node.contentDescription?.toString() ?: ""
//            desc.contains("Reel by", ignoreCase = true) ||
//                    desc.contains("Sponsored Reel by", ignoreCase = true)
//        }
//        if (hasActiveReel) {
//            Log.d("REELSBREAK", "Instagram: clips_video_container has 'Reel by' → REELS_SCREEN")
//            return DetectionResult.REELS_SCREEN
//        }
//
//        // STRATEGY 2 (Backup):
//        // action_bar_tab_layout with "Reels"/"Friends" tabs only exists
//        // when the Reels screen is the foreground tab.
//        val actionBarTabLayout = rootNode.findAccessibilityNodeInfosByViewId(
//            "com.instagram.android:id/action_bar_tab_layout"
//        )
//        if (actionBarTabLayout.isNotEmpty()) {
//            // Check for the "Reels" or "Friends" tab text labels
//            val hasReelsTab = findNodeWithText(rootNode, "Reels") != null ||
//                    findNodeWithText(rootNode, "Friends") != null
//            if (hasReelsTab) {
//                Log.d("REELSBREAK", "Instagram: action_bar_tab_layout with Reels/Friends → REELS_SCREEN")
//                return DetectionResult.REELS_SCREEN
//            }
//        }
//
//        // STRATEGY 3 (Additional backup):
//        // clips_expanded_touch_view is the full-screen touch overlay —
//        // ONLY exists when Reels is in the foreground.
//        val expandedTouchView = rootNode.findAccessibilityNodeInfosByViewId(
//            "com.instagram.android:id/clips_expanded_touch_view"
//        )
//        if (expandedTouchView.isNotEmpty()) {
//            Log.d("REELSBREAK", "Instagram: clips_expanded_touch_view found → REELS_SCREEN")
//            return DetectionResult.REELS_SCREEN
//        }
//
//        return DetectionResult.NORMAL_SCREEN
//    }
//
//    // Helper: search entire tree for a node with specific text
//    private fun findNodeWithText(
//        node: AccessibilityNodeInfo?,
//        text: String
//    ): AccessibilityNodeInfo? {
//        if (node == null) return null
//        if (node.text?.toString() == text) return node
//        for (i in 0 until node.childCount) {
//            val result = findNodeWithText(node.getChild(i), text)
//            if (result != null) return result
//        }
//        return null
//    }
//}


class InstagramReelsDetector : AppDetector {

//    override fun detect(rootNode: AccessibilityNodeInfo?): DetectionResult {
//        if (rootNode == null) return DetectionResult.NORMAL_SCREEN
//
//        // ── GATE 1: Is the Reels tab actually selected? ───────────────────
//        // This is the ONLY reliable signal. isSelected is set by the Android
//        // tab navigation system — NOT by ViewPager memory pre-loading.
//        // If clips_tab is NOT selected, we are NOT on Reels. Period.
//        val clipsTab = rootNode.findAccessibilityNodeInfosByViewId(
//            "com.instagram.android:id/clips_tab"
//        )
//        val isReelsTabSelected = clipsTab.any { it.isSelected }
//
//        if (!isReelsTabSelected) {
//            // We are on Home, Search, Messages, or Profile — never block these
//            Log.d("REELSBREAK", "Instagram: clips_tab NOT selected → safe screen")
//            return DetectionResult.NORMAL_SCREEN
//        }
//
//        // ── GATE 2: Is there actually a Reel playing? ────────────────────
//        // Double-confirm: clips_tab is selected AND a reel is actively loaded.
//        // Prevents false positives during tab transition animations.
//        val clipsVideoContainers = rootNode.findAccessibilityNodeInfosByViewId(
//            "com.instagram.android:id/clips_video_container"
//        )
//        val hasActiveReel = clipsVideoContainers.any { node ->
//            val desc = node.contentDescription?.toString() ?: ""
//            desc.contains("Reel by", ignoreCase = true) ||
//                    desc.contains("Sponsored Reel by", ignoreCase = true)
//        }
//
//        return if (hasActiveReel) {
//            Log.d("REELSBREAK", "Instagram: clips_tab SELECTED + Reel active → REELS_SCREEN ✅")
//            DetectionResult.REELS_SCREEN
//        } else {
//            // Tab is selected but reel hasn't loaded yet (transition frame)
//            Log.d("REELSBREAK", "Instagram: clips_tab SELECTED but no reel yet → waiting")
//            DetectionResult.NORMAL_SCREEN
//        }
//    }


    override fun detect(rootNode: AccessibilityNodeInfo?): DetectionResult {
        if (rootNode == null) return DetectionResult.NORMAL_SCREEN

        // ── GATE 1: Is Reels tab selected? ───────────────────────────────
        // clips_tab.isSelected is set by Android navigation system.
        // This is 100% reliable — if this is true, user IS on Reels tab.
        // We block on this alone — do NOT wait for video to load.
        val clipsTab = rootNode.findAccessibilityNodeInfosByViewId(
            "com.instagram.android:id/clips_tab"
        )
        val isReelsTabSelected = clipsTab.any { it.isSelected }

        if (!isReelsTabSelected) {
            return DetectionResult.NORMAL_SCREEN
        }

        // clips_tab IS selected → user is on Reels tab → block immediately
        // (We don't wait for clips_video_container to load — that caused
        //  the "one free reel" bug where first reel was always visible)
        Log.d("REELSBREAK", "Instagram: clips_tab SELECTED → REELS_SCREEN ✅")
        return DetectionResult.REELS_SCREEN
    }
}