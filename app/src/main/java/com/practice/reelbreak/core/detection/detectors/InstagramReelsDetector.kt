package com.practice.reelbreak.core.detection.detectors

import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.practice.reelbreak.domain.model.DetectionResult
class InstagramReelsDetector : AppDetector {

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

//         🔧 CRITICAL FIX: We extracted hooks into a list. Now that we have our
//         boolean answer, we MUST iterate through the list and recycle every single one.
//        clipsTab.forEach { node ->
//            node.recycle()
//        }

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