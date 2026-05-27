//package com.practice.reelbreak.core.detection.detectors
//
//import android.util.Log
//import android.view.accessibility.AccessibilityNodeInfo
//import com.practice.reelbreak.domain.model.DetectionResult
//
////class InstagramReelsDetector : AppDetector {
////
////    override fun detect(rootNode: AccessibilityNodeInfo?): DetectionResult {
////        if (rootNode == null) return DetectionResult.UNKNOWN
////        val tabNodes = rootNode.findAccessibilityNodeInfosByViewId("com.instagram.android:id/clips_tab")
////        val viewerNodes = rootNode.findAccessibilityNodeInfosByViewId("com.instagram.android:id/clips_viewer_view_pager")
////
////        Log.d("IG_DETECT", "clips_tab count=${tabNodes.size}, viewer count=${viewerNodes.size}")
////
//////        val isReelsTabSelected = hasVisibleViewId(rootNode, "com.instagram.android:id/clips_tab") {
//////            it.isSelected
//////        }
//////
//////        val isViewerVisible = viewerNodes.any { node ->
//////            try { node.isVisibleToUser } finally { node.recycle() }
//////        }
////
////        val isReelsTabSelected = tabNodes.any { node ->
////            try {
////                val rect = android.graphics.Rect()
////                node.getBoundsInScreen(rect)
////                Log.d("IG_DETECT", "clips_tab bounds=$rect selected=${node.isSelected}")
////                rect.width() > 0 && rect.height() > 0  // just check it has real bounds
////            } finally { node.recycle() }
////        }
////
////        val isViewerVisible = viewerNodes.any { node ->
////            try {
////                val rect = android.graphics.Rect()
////                node.getBoundsInScreen(rect)
////                Log.d("IG_DETECT", "viewer bounds=$rect visible=${node.isVisibleToUser}")
////                rect.width() > 0 && rect.height() > 0  // just check it has real bounds
////            } finally { node.recycle() }
////        }
////
////        val isOnReelsViewer = hasVisibleViewId(rootNode, "com.instagram.android:id/clips_viewer_view_pager")
////        Log.d("IG_DETECT", "tabSelected=$isReelsTabSelected viewerVisible=$isViewerVisible")
////        return if (isReelsTabSelected || isOnReelsViewer) {
////            DetectionResult.REELS_SCREEN
////        } else {
////            DetectionResult.NORMAL_SCREEN
////        }
////    }
////
////    private fun hasVisibleViewId(
////        root: AccessibilityNodeInfo,
////        viewId: String,
////        extraCheck: ((AccessibilityNodeInfo) -> Boolean)? = null
////    ): Boolean {
////        val matches = root.findAccessibilityNodeInfosByViewId(viewId)
////        return matches.any { node ->
////            try {
////                isNodeVisible(node) && (extraCheck?.invoke(node) ?: true)
////            } finally {
////                node.recycle()
////            }
////        }
////    }
////
////    private fun isNodeVisible(node: AccessibilityNodeInfo): Boolean {
////        val rect = android.graphics.Rect()
////        node.getBoundsInScreen(rect)
////        return node.isVisibleToUser && rect.width() > 0 && rect.height() > 0
////    }
////}
//
//
//
//
//import android.graphics.Rect
//
//
//class InstagramReelsDetector : AppDetector {
//
//    override fun detect(rootNode: AccessibilityNodeInfo?): DetectionResult {
//        if (rootNode == null) return DetectionResult.NORMAL_SCREEN
//
//        val clipsTabNodes =
//            rootNode.findAccessibilityNodeInfosByViewId("com.instagram.android:id/clips_tab")
//        val viewerNodes =
//            rootNode.findAccessibilityNodeInfosByViewId("com.instagram.android:id/clips_viewer_view_pager")
//
//        val hasClipsTabOnScreen = clipsTabNodes.any { node ->
//            try { hasRealBounds(node) } finally { node.recycle() }
//        }
//
//        val hasViewerOnScreen = viewerNodes.any { node ->
//            try { hasRealBounds(node) } finally { node.recycle() }
//        }
//
//        Log.d("IG_DETECT", "clipsTabOnScreen=$hasClipsTabOnScreen viewerOnScreen=$hasViewerOnScreen")
//
//        return if (hasClipsTabOnScreen || hasViewerOnScreen) {
//            DetectionResult.REELS_SCREEN
//        } else {
//            DetectionResult.NORMAL_SCREEN
//        }
//    }
//
//    override fun onEvent(event: android.view.accessibility.AccessibilityEvent) = Unit
//
//    override fun reset() = Unit
//
//    private fun hasRealBounds(node: AccessibilityNodeInfo): Boolean {
//        val rect = Rect()
//        node.getBoundsInScreen(rect)
//        return rect.width() > 0 && rect.height() > 0
//    }
//}