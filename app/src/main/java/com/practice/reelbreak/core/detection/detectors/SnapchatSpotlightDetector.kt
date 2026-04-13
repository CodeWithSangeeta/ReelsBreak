package com.practice.reelbreak.core.detection.detectors


import android.view.accessibility.AccessibilityNodeInfo
import com.practice.reelbreak.domain.model.DetectionResult
import android.util.Log

//class SnapchatSpotlightDetector : AppDetector {
//
//    override fun detect(rootNode: AccessibilityNodeInfo?): DetectionResult {
//        // ✅ ONE null guard here — after this line, rootNode is guaranteed non-null
//        if (rootNode == null) return DetectionResult.NORMAL_SCREEN
//
//        // Signal 1: dflargestory only appears in Spotlight feed
//        val spotlightVideoNodes = rootNode.findAccessibilityNodeInfosByViewId(
//            "com.snapchat.android:id/dflargestory"
//        )
//        if (spotlightVideoNodes.isNotEmpty()) {  // ✅ no error — list is non-null now
//            return DetectionResult.REELS_SCREEN
//        }
//
//        // Signal 2: Spotlight tab selected + not on other screens
//        val spotlightTab = rootNode.findAccessibilityNodeInfosByViewId(
//            "com.snapchat.android:id/ngsspotlighticoncontainer"
//        )
//        val isSpotlightTabSelected: Boolean = spotlightTab.any { node ->  // ✅ Boolean not Boolean?
//            node.isSelected || node.contentDescription?.contains("Spotlight", ignoreCase = true) == true
//                    && !hasCameraButton(rootNode)
//                    && !hasChatItems(rootNode)
//                    && !hasFriendCards(rootNode)
//        }
//
//        if (isSpotlightTabSelected) {  // ✅ no error — plain Boolean
//            return DetectionResult.REELS_SCREEN
//        }
//
//        return DetectionResult.NORMAL_SCREEN
//    }
//
//    private fun hasCameraButton(root: AccessibilityNodeInfo): Boolean =
//        root.findAccessibilityNodeInfosByViewId(
//            "com.snapchat.android:id/camera_capture_button"
//        ).isNotEmpty()
//
//    private fun hasChatItems(root: AccessibilityNodeInfo): Boolean =
//        root.findAccessibilityNodeInfosByViewId(
//            "com.snapchat.android:id/ffitem"
//        ).isNotEmpty()
//
//    private fun hasFriendCards(root: AccessibilityNodeInfo): Boolean =
//        root.findAccessibilityNodeInfosByViewId(
//            "com.snapchat.android:id/friendcardframe"
//        ).isNotEmpty()
//}


class SnapchatSpotlightDetector : AppDetector {

    override fun detect(rootNode: AccessibilityNodeInfo?): DetectionResult {
        if (rootNode == null) return DetectionResult.NORMAL_SCREEN

        // 🔍 DEBUG: Log which key IDs are found on current screen
        val dfLargeStory = rootNode.findAccessibilityNodeInfosByViewId(
            "com.snapchat.android:id/dflargestory"
        )
        val spotlightTab = rootNode.findAccessibilityNodeInfosByViewId(
            "com.snapchat.android:id/ngsspotlighticoncontainer"
        )
        val cameraButton = rootNode.findAccessibilityNodeInfosByViewId(
            "com.snapchat.android:id/camera_capture_button"
        )
        val chatItems = rootNode.findAccessibilityNodeInfosByViewId(
            "com.snapchat.android:id/ffitem"
        )
        val friendCards = rootNode.findAccessibilityNodeInfosByViewId(
            "com.snapchat.android:id/friendcardframe"
        )

        // Log the isSelected state of spotlight tab
        val spotlightSelected = spotlightTab.firstOrNull()?.isSelected
        val spotlightDesc = spotlightTab.firstOrNull()?.contentDescription

        Log.d("SNAPCHAT_DEBUG", "=== SCREEN ANALYSIS ===")
        Log.d("SNAPCHAT_DEBUG", "dflargestory count   : ${dfLargeStory.size}")
        Log.d("SNAPCHAT_DEBUG", "spotlightTab selected: $spotlightSelected")
        Log.d("SNAPCHAT_DEBUG", "spotlightTab desc    : $spotlightDesc")
        Log.d("SNAPCHAT_DEBUG", "cameraButton found   : ${cameraButton.isNotEmpty()}")
        Log.d("SNAPCHAT_DEBUG", "chatItems found      : ${chatItems.isNotEmpty()}")
        Log.d("SNAPCHAT_DEBUG", "friendCards found    : ${friendCards.isNotEmpty()}")
        Log.d("SNAPCHAT_DEBUG", "=======================")

        return DetectionResult.NORMAL_SCREEN // not blocking yet, just observing
    }
}