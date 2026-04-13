package com.practice.reelbreak.core.detection.detectors

import android.view.accessibility.AccessibilityNodeInfo
import com.practice.reelbreak.domain.model.DetectionResult

class YouTubeReelsDetector : AppDetector {

    override fun detect(rootNode: AccessibilityNodeInfo?): DetectionResult {

        if (rootNode == null) return DetectionResult.UNKNOWN

        val isShorts = containsShortsRecycler(rootNode)

        return if (isShorts) {
            DetectionResult.REELS_SCREEN
        } else {
            DetectionResult.NORMAL_SCREEN
        }
    }

    private fun containsShortsRecycler(node: AccessibilityNodeInfo?): Boolean {

        if (node == null) return false

        val viewId = node.viewIdResourceName ?: ""

        if (viewId.contains("reel_recycler")) {
            return true
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            if (containsShortsRecycler(child)) {
                return true
            }
        }

        return false
    }
}