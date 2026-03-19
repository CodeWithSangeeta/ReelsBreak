package com.practice.reelbreak.core.detection.detectors

import android.view.accessibility.AccessibilityNodeInfo
import com.practice.reelbreak.domain.model.DetectionResult

class GenericReelsDetector : AppDetector {

    override fun detect(rootNode: AccessibilityNodeInfo?): DetectionResult {

        if (rootNode == null) {
            return DetectionResult.UNKNOWN
        }

        val videoFound = containsVideoComponent(rootNode)

        return if (videoFound) {
            DetectionResult.REELS_SCREEN
        } else {
            DetectionResult.NORMAL_SCREEN
        }
    }

    private fun containsVideoComponent(node: AccessibilityNodeInfo?): Boolean {

        if (node == null) return false

        val className = node.className?.toString() ?: ""

        if (
            className.contains("SurfaceView") ||
            className.contains("TextureView") ||
            className.contains("VideoView") ||
            className.contains("PlayerView") ||
            className.contains("ExoPlayer")
        ) {
            return true
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            if (containsVideoComponent(child)) {
                return true
            }
        }

        return false
    }
}