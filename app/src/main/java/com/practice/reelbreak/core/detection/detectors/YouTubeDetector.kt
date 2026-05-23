package com.practice.reelbreak.core.detection.detectors

import android.view.accessibility.AccessibilityNodeInfo
import com.practice.reelbreak.domain.model.DetectionResult

//class YouTubeReelsDetector : AppDetector {
//
//    override fun detect(rootNode: AccessibilityNodeInfo?): DetectionResult {
//
//        if (rootNode == null) return DetectionResult.UNKNOWN
//
//        val isShorts = containsShortsRecycler(rootNode)
//
//        return if (isShorts) {
//            DetectionResult.REELS_SCREEN
//        } else {
//            DetectionResult.NORMAL_SCREEN
//        }
//    }
//
//    private fun containsShortsRecycler(node: AccessibilityNodeInfo?): Boolean {
//
//        if (node == null) return false
//
//        val viewId = node.viewIdResourceName ?: ""
//
//        if (viewId.contains("reel_recycler")) {
//            return true
//        }
//
//        for (i in 0 until node.childCount) {
//            val child = node.getChild(i)
//            if (containsShortsRecycler(child)) {
//                return true
//            }
//        }
//
//        return false
//    }
//}




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

        // Explicit recycling loop for children
        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            val foundInChild = containsShortsRecycler(child)

            // CRITICAL: Recycle the child node immediately after matching to clear memory hooks
            child.recycle()

            if (foundInChild) {
                return true
            }
        }

        return false
    }
}









//
//package com.practice.reelbreak.core.detection.detectors
//
//import android.view.accessibility.AccessibilityNodeInfo
//import com.practice.reelbreak.domain.model.DetectionResult
//
//class YouTubeReelsDetector : AppDetector {
//
//    override fun detect(rootNode: AccessibilityNodeInfo?): DetectionResult {
//        if (rootNode == null) return DetectionResult.UNKNOWN
//
//        val hasSeekBar = containsSeekBar(rootNode)
//        val hasShortDurationPattern = containsShortDurationDescription(rootNode)
//        val hasScrollableVideoLikeSurface = containsRecyclerOrPager(rootNode)
//        val hasTooMuchBrowseUi = containsBrowseHeavyUi(rootNode)
//
//        val score = listOf(
//            hasSeekBar,
//            hasShortDurationPattern,
//            hasScrollableVideoLikeSurface
//        ).count { it }
//
//        return when {
//            hasTooMuchBrowseUi && score < 3 -> DetectionResult.NORMAL_SCREEN
//            score >= 2 -> DetectionResult.REELS_SCREEN
//            else -> DetectionResult.NORMAL_SCREEN
//        }
//    }
//
//    private fun containsSeekBar(node: AccessibilityNodeInfo?): Boolean {
//        if (node == null) return false
//        val cls = node.className?.toString().orEmpty()
//        if (cls.contains("SeekBar", ignoreCase = true)) return true
//        for (i in 0 until node.childCount) {
//            if (containsSeekBar(node.getChild(i))) return true
//        }
//        return false
//    }
//
//    private fun containsShortDurationDescription(node: AccessibilityNodeInfo?): Boolean {
//        if (node == null) return false
//        val text = node.text?.toString().orEmpty()
//        val desc = node.contentDescription?.toString().orEmpty()
//        val combined = "$text $desc"
//
//        val looksLikeMediaTime =
//            combined.contains("seconds of", ignoreCase = true) ||
//                    combined.contains("minutes", ignoreCase = true)
//
//        if (looksLikeMediaTime) return true
//
//        for (i in 0 until node.childCount) {
//            if (containsShortDurationDescription(node.getChild(i))) return true
//        }
//        return false
//    }
//
//    private fun containsRecyclerOrPager(node: AccessibilityNodeInfo?): Boolean {
//        if (node == null) return false
//        val cls = node.className?.toString().orEmpty()
//        if (
//            cls.contains("RecyclerView", ignoreCase = true) ||
//            cls.contains("ViewPager", ignoreCase = true)
//        ) return true
//
//        for (i in 0 until node.childCount) {
//            if (containsRecyclerOrPager(node.getChild(i))) return true
//        }
//        return false
//    }
//
//    private fun containsBrowseHeavyUi(node: AccessibilityNodeInfo?): Boolean {
//        if (node == null) return false
//        val cls = node.className?.toString().orEmpty()
//
//        if (cls.contains("ScrollView", ignoreCase = true)) return true
//
//        for (i in 0 until node.childCount) {
//            if (containsBrowseHeavyUi(node.getChild(i))) return true
//        }
//        return false
//    }
//}
















//
//package com.practice.reelbreak.core.detection.detectors
//
//import android.view.accessibility.AccessibilityNodeInfo
//import com.practice.reelbreak.domain.model.DetectionResult
//
//class YouTubeReelsDetector : AppDetector {
//
//    override fun detect(rootNode: AccessibilityNodeInfo?): DetectionResult {
//        if (rootNode == null) return DetectionResult.UNKNOWN
//
//        // Traverse the tree to find the shallow Shorts progress bar signature
//        val isShortsActive = verifyShortsStructuralFingerprint(rootNode, currentDepth = 0)
//
//        return if (isShortsActive) {
//            DetectionResult.REELS_SCREEN
//        } else {
//            DetectionResult.NORMAL_SCREEN
//        }
//    }
//
//    /**
//     * Scans for the structural signature of the Shorts playback window
//     * without checking specific Resource view IDs.
//     */
//    private fun verifyShortsStructuralFingerprint(node: AccessibilityNodeInfo?, currentDepth: Int): Boolean {
//        if (node == null) return false
//
//        // Optimization: Stop traversing if we go deeper than the Shorts layout target
//        if (currentDepth > 3) return false
//
//        val className = node.className?.toString() ?: ""
//        val description = node.contentDescription?.toString() ?: ""
//
//        // Target: Look for a SeekBar at shallow depths (typically depth 2)
//        if (className == "android.widget.SeekBar" && currentDepth <= 3) {
//            if (isShortsDuration(description)) {
//                return true
//            }
//        }
//
//        // Recursively inspect children with clean memory recycling mechanics
//        for (i in 0 until node.childCount) {
//            val child = node.getChild(i) ?: continue
//            val matched = verifyShortsStructuralFingerprint(child, currentDepth + 1)
//            if (matched) {
//                child.recycle()
//                return true
//            }
//            child.recycle() // Prevent accessibility node memory leaks
//        }
//
//        return false
//    }
//
//    /**
//     * Parses the system description string to verify if the video length
//     * matches typical short-form specifications (e.g., under 2 minutes).
//     * Log sample: "0 minutes 0 seconds of 0 minutes 54 seconds"
//     */
//    private fun isShortsDuration(description: String): Boolean {
//        if (description.isEmpty()) return false
//
//        // Match both singular and plural duration strings safely
//        return description.contains("of 0 minutes") ||
//                description.contains("of 1 minute") ||
//                description.contains("of 0 minute")
//    }
//}