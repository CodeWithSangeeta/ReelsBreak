//package com.practice.reelbreak.core.detection.detectors
//
//import android.util.Log
//import android.view.accessibility.AccessibilityEvent
//import android.view.accessibility.AccessibilityNodeInfo
//import com.practice.reelbreak.domain.model.DetectionResult
//
//
//
//
////class SnapchatReelsDetector : AppDetector {
////
////    private var consecutiveSpotlightHits = 0
////
////    override fun detect(rootNode: AccessibilityNodeInfo?): DetectionResult {
////        if (rootNode == null) {
////            resetHits("root null")
////            return DetectionResult.NORMAL_SCREEN
////        }
////
////        if (hasViewId(rootNode, "com.snapchat.android:id/camera_capture_button")) {
////            resetHits("Camera")
////            Log.d(TAG, "NORMAL - Camera")
////            return DetectionResult.NORMAL_SCREEN
////        }
////
////        if (hasViewId(rootNode, "com.snapchat.android:id/ff_item")) {
////            resetHits("Chat")
////            Log.d(TAG, "NORMAL - Chat")
////            return DetectionResult.NORMAL_SCREEN
////        }
////
////        if (hasViewId(rootNode, "com.snapchat.android:id/friend_card_frame")) {
////            resetHits("Stories")
////            Log.d(TAG, "NORMAL - Stories")
////            return DetectionResult.NORMAL_SCREEN
////        }
////
////        if (isMapScreen(rootNode)) {
////            resetHits("Map")
////            Log.d(TAG, "NORMAL - Map")
////            return DetectionResult.NORMAL_SCREEN
////        }
////
////        val hasSpotlightNav = hasViewId(rootNode, "com.snapchat.android:id/ngs_spotlight_icon_container")
////
////        val isStrictSpotlight = hasSpotlightNav
////
////        if (isStrictSpotlight) {
////            consecutiveSpotlightHits++
////            return if (consecutiveSpotlightHits >= 2) {
////                Log.d(TAG, "REELS - Confirmed Spotlight match")
////                DetectionResult.REELS_SCREEN
////            } else {
////                Log.d(TAG, "NORMAL - Spotlight transition candidate")
////                DetectionResult.NORMAL_SCREEN
////            }
////        }
////
////        resetHits("Not Spotlight")
////        Log.d(TAG, "NORMAL - Not Spotlight")
////        return DetectionResult.NORMAL_SCREEN
////    }
////
////    override fun onEvent(event: AccessibilityEvent) = Unit
////
////    override fun reset() {
////        consecutiveSpotlightHits = 0
////    }
////
////    private fun resetHits(reason: String) {
////        consecutiveSpotlightHits = 0
////    }
////
////    private fun isMapScreen(root: AccessibilityNodeInfo): Boolean {
////        val hasPills =
////            hasViewId(root, "list-picker-pill-Near Me") ||
////                    hasViewId(root, "list-picker-pill-Best Friends") ||
////                    hasViewId(root, "list-picker-pill-My AI") ||
////                    hasViewId(root, "list-picker-pill-Groups")
////
////        return hasPills
////    }
////
////    private fun hasViewId(root: AccessibilityNodeInfo, viewId: String): Boolean {
////        val nodes = root.findAccessibilityNodeInfosByViewId(viewId)
////        val found = nodes.isNotEmpty()
////        nodes.forEach { it.recycle() }
////        return found
////    }
////
////    companion object {
////        private const val TAG = "SNAPCHATDETECTOR"
////    }
////}
//
//
//
//class SnapchatReelsDetector : AppDetector {
//
//    private var consecutiveSpotlightMatches = 0
//
//    override fun detect(rootNode: AccessibilityNodeInfo?): DetectionResult {
//        if (rootNode == null) return DetectionResult.NORMAL_SCREEN
//
//        // GUARD 1 – Camera
//        if (hasViewId(rootNode, "com.snapchat.android:id/camera_capture_button")) {
//            consecutiveSpotlightMatches = 0
//            Log.d(TAG, "NORMAL - Camera")
//            return DetectionResult.NORMAL_SCREEN
//        }
//
//        // GUARD 2 – Chat
//        if (hasViewId(rootNode, "com.snapchat.android:id/ff_item")) {
//            consecutiveSpotlightMatches = 0
//            Log.d(TAG, "NORMAL - Chat")
//            return DetectionResult.NORMAL_SCREEN
//        }
//
//        // GUARD 3 – Map
//        if (isMapScreen(rootNode)) {
//            consecutiveSpotlightMatches = 0
//            Log.d(TAG, "NORMAL - Map")
//            return DetectionResult.NORMAL_SCREEN
//        }
//
//        // GUARD 4 – Stories
//        if (hasViewId(rootNode, "com.snapchat.android:id/friend_card_frame")) {
//            consecutiveSpotlightMatches = 0
//            Log.d(TAG, "NORMAL - Stories")
//            return DetectionResult.NORMAL_SCREEN
//        }
//
//        // POSITIVE Spotlight – require stability
//        val hasSpotlightNav = hasViewId(
//            rootNode,
//            "com.snapchat.android:id/ngs_spotlight_icon_container"
//        )
//
//        if (hasSpotlightNav) {
//            consecutiveSpotlightMatches++
//
//            return if (consecutiveSpotlightMatches >= 2) {
//                Log.d(TAG, "REELS - Confirmed Spotlight match")
//                DetectionResult.REELS_SCREEN
//            } else {
//                Log.d(TAG, "NORMAL - Spotlight transition candidate")
//                DetectionResult.NORMAL_SCREEN
//            }
//        }
//
//        // No known screen; reset Spotlight streak
//        consecutiveSpotlightMatches = 0
//        Log.d(TAG, "NORMAL - Transitional/unknown screen")
//        return DetectionResult.NORMAL_SCREEN
//    }
//
//    override fun onEvent(event: AccessibilityEvent) = Unit
//
//    override fun reset() {
//        consecutiveSpotlightMatches = 0
//    }
//
//    private fun isMapScreen(root: AccessibilityNodeInfo): Boolean {
//        val hasPills =
//            hasViewId(root, "list-picker-pill-Near Me") ||
//                    hasViewId(root, "list-picker-pill-Best Friends") ||
//                    hasViewId(root, "list-picker-pill-My AI") ||
//                    hasViewId(root, "list-picker-pill-Groups")
//
//        val hasChatRows = hasViewId(root, "com.snapchat.android:id/ff_item")
//        return hasPills && !hasChatRows
//    }
//
//    private fun hasViewId(root: AccessibilityNodeInfo, viewId: String): Boolean {
//        return root.findAccessibilityNodeInfosByViewId(viewId).isNotEmpty()
//    }
//
//    companion object {
//        private const val TAG = "SNAPCHATDETECTOR"
//    }
//}