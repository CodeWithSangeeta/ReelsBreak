package com.practice.reelbreak.core.detection.detectors

import android.view.accessibility.AccessibilityNodeInfo
import com.practice.reelbreak.domain.model.DetectionResult
class YouTubeReelsDetector : AppDetector {

    override fun detect(rootNode: AccessibilityNodeInfo?): DetectionResult {
        if (rootNode == null) return DetectionResult.UNKNOWN
        val isShortsActive = verifyShortsStructuralFingerprint(rootNode, currentDepth = 0)

        return if (isShortsActive) {
            DetectionResult.REELS_SCREEN
        } else {
            DetectionResult.NORMAL_SCREEN
        }
    }

    private fun verifyShortsStructuralFingerprint(node: AccessibilityNodeInfo?, currentDepth: Int): Boolean {
        if (node == null) return false

        if (currentDepth > 6) return false

        val className = node.className?.toString() ?: ""
        val description = node.contentDescription?.toString() ?: ""

        if (className == "android.widget.SeekBar" && currentDepth <= 3) {
            if (isShortsDuration(description)) {
                return true
            }
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            val matched = verifyShortsStructuralFingerprint(child, currentDepth + 1)
            if (matched) {
                child.recycle()
                return true
            }
            child.recycle()
        }

        return false
    }

    private fun isShortsDuration(description: String): Boolean {
        if (description.isEmpty()) return false

        return description.contains("of 0 minutes") ||
                description.contains("of 1 minute") ||
                description.contains("of 0 minute")
    }
}