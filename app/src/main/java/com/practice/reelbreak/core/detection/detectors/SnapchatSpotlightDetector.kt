package com.practice.reelbreak.core.detection.detectors

import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.practice.reelbreak.domain.model.DetectionResult

class SnapchatReelsDetector : AppDetector {

    override fun detect(rootNode: AccessibilityNodeInfo?): DetectionResult {
        if (rootNode == null) return DetectionResult.UNKNOWN
        dumpTree(rootNode, 0)
        val isOnSpotlight = hasVisibleViewId(
            rootNode,
            "com.snapchat.android:id/spotlight_container"
        )
        val spotlightNodes = rootNode.findAccessibilityNodeInfosByViewId("com.snapchat.android:id/spotlight_container")
        Log.d("SC_DETECT", "spotlight count=${spotlightNodes.size}")

        val isSpotlightVisible = spotlightNodes.any { node ->
            try { node.isVisibleToUser } finally { node.recycle() }
        }
        Log.d("SC_DETECT", "spotlightVisible=$isSpotlightVisible")
        return if (isOnSpotlight) {
            DetectionResult.REELS_SCREEN
        } else {
            DetectionResult.NORMAL_SCREEN
        }
    }

    private fun dumpTree(node: AccessibilityNodeInfo?, depth: Int) {
        if (node == null || depth > 5) return
        val indent = "  ".repeat(depth)
        Log.d("SC_TREE", "${indent}cls=${node.className} id=${node.viewIdResourceName} desc=${node.contentDescription} visible=${node.isVisibleToUser}")
        for (i in 0 until node.childCount) {
            dumpTree(node.getChild(i), depth + 1)
        }
    }

    private fun hasVisibleViewId(
        root: AccessibilityNodeInfo,
        viewId: String
    ): Boolean {
        val matches = root.findAccessibilityNodeInfosByViewId(viewId)
        return matches.any { node ->
            try {
                isNodeVisible(node)
            } finally {
                node.recycle()
            }
        }
    }

    private fun isNodeVisible(node: AccessibilityNodeInfo): Boolean {
        val rect = android.graphics.Rect()
        node.getBoundsInScreen(rect)
        return node.isVisibleToUser && rect.width() > 0 && rect.height() > 0
    }
}