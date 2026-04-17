package com.practice.reelbreak.core.detection.detectors



//import android.util.Log
//import android.view.accessibility.AccessibilityNodeInfo
//import com.practice.reelbreak.domain.model.DetectionResult
//
//class SnapchatSpotlightDetector : AppDetector {
//
//    override fun detect(rootNode: AccessibilityNodeInfo?): DetectionResult {
//        if (rootNode == null) return DetectionResult.NORMAL_SCREEN
//
//        // ── PHASE 1: DISCOVERY ─────────────────────────────────────────────
//        // Run this first. Go to each Snapchat screen and filter Logcat by
//        // "SNAPCHAT_TREE" to discover the real IDs on YOUR Snapchat version.
//        // Once you find the correct IDs, fill them in Phase 2 and remove this call.
//        logTree(rootNode)
//
//        // ── PHASE 2: DETECTION ─────────────────────────────────────────────
//        // SIGNAL 1 — Spotlight video container node (most reliable)
//        // Replace "df_large_story" with the actual ID you find from the tree dump.
//        val signal1IDs = listOf(
//            "com.snapchat.android:id/df_large_story",
//            "com.snapchat.android:id/spotlight_video_container",
//            "com.snapchat.android:id/spotlight_feed_item",
//            "com.snapchat.android:id/video_player_container"
//        )
//        for (id in signal1IDs) {
//            val nodes = rootNode.findAccessibilityNodeInfosByViewId(id)
//            if (nodes.isNotEmpty()) {
//                Log.d("SNAPCHAT_DEBUG", "✅ REELS via Signal 1: $id (count=${nodes.size})")
//                return DetectionResult.REELS_SCREEN
//            }
//        }
//
//        // SIGNAL 2 — Spotlight tab is selected + not on camera/chat/friends
//        val spotlightTabIDs = listOf(
//            "com.snapchat.android:id/ngs_spotlight_icon_container",
//            "com.snapchat.android:id/spotlight_tab",
//            "com.snapchat.android:id/bottom_nav_spotlight"
//        )
//        for (tabId in spotlightTabIDs) {
//            val tabNodes = rootNode.findAccessibilityNodeInfosByViewId(tabId)
//            val isTabSelected = tabNodes.any { node ->
//                node.isSelected ||
//                        node.contentDescription?.contains("Spotlight", ignoreCase = true) == true
//            }
//            if (isTabSelected) {
//                val isSafeScreen = hasCameraButton(rootNode)
//                        || hasChatItems(rootNode)
//                        || hasFriendCards(rootNode)
//                if (!isSafeScreen) {
//                    Log.d("SNAPCHAT_DEBUG", "✅ REELS via Signal 2: tab=$tabId selected, no safe screen")
//                    return DetectionResult.REELS_SCREEN
//                } else {
//                    Log.d("SNAPCHAT_DEBUG", "⏭ Tab selected but safe-screen guard hit, skipping")
//                }
//            }
//        }
//
//        // SIGNAL 3 — Content description fallback
//        // Snapchat sometimes labels Spotlight nodes with accessibility descriptions
//        val hasSpotlightDesc = containsDescriptionKeyword(rootNode, "Spotlight")
//        val isSafeScreen = hasCameraButton(rootNode) || hasChatItems(rootNode) || hasFriendCards(rootNode)
//        if (hasSpotlightDesc && !isSafeScreen) {
//            Log.d("SNAPCHAT_DEBUG", "✅ REELS via Signal 3: content desc contains 'Spotlight'")
//            return DetectionResult.REELS_SCREEN
//        }
//
//        Log.d("SNAPCHAT_DEBUG", "✅ NORMAL SCREEN — no signals matched")
//        return DetectionResult.NORMAL_SCREEN
//    }
//
//    // ── Safe-screen guards ─────────────────────────────────────────────────
//
//    private fun hasCameraButton(root: AccessibilityNodeInfo): Boolean =
//        listOf(
//            "com.snapchat.android:id/camera_capture_button",
//            "com.snapchat.android:id/capture_button"
//        ).any { root.findAccessibilityNodeInfosByViewId(it).isNotEmpty() }
//
//    private fun hasChatItems(root: AccessibilityNodeInfo): Boolean =
//        listOf(
//            "com.snapchat.android:id/ff_item",
//            "com.snapchat.android:id/chat_feed_item",
//            "com.snapchat.android:id/conversation_item"
//        ).any { root.findAccessibilityNodeInfosByViewId(it).isNotEmpty() }
//
//    private fun hasFriendCards(root: AccessibilityNodeInfo): Boolean =
//        listOf(
//            "com.snapchat.android:id/friend_card_frame",
//            "com.snapchat.android:id/friend_card"
//        ).any { root.findAccessibilityNodeInfosByViewId(it).isNotEmpty() }
//
//    private fun containsDescriptionKeyword(
//        node: AccessibilityNodeInfo?,
//        keyword: String
//    ): Boolean {
//        if (node == null) return false
//        val desc = node.contentDescription?.toString() ?: ""
//        if (desc.contains(keyword, ignoreCase = true)) return true
//        for (i in 0 until node.childCount) {
//            if (containsDescriptionKeyword(node.getChild(i), keyword)) return true
//        }
//        return false
//    }
//
//    // ── Tree logger ────────────────────────────────────────────────────────
//    // Filter Logcat: tag = "SNAPCHAT_TREE"
//    // Open each screen in Snapchat and look for IDs that are UNIQUE to Spotlight.
//    private fun logTree(node: AccessibilityNodeInfo?, depth: Int = 0) {
//        if (node == null) return
//        val id   = node.viewIdResourceName ?: ""
//        val text = node.text?.toString() ?: ""
//        val desc = node.contentDescription?.toString() ?: ""
//        val sel  = node.isSelected
//        if (id.isNotEmpty() || text.isNotEmpty() || desc.isNotEmpty()) {
//            Log.d("SNAPCHAT_TREE",
//                "  ".repeat(depth) + "id=$id | text=$text | desc=$desc | selected=$sel"
//            )
//        }
//        for (i in 0 until node.childCount) logTree(node.getChild(i), depth + 1)
//    }
//}




import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.practice.reelbreak.domain.model.DetectionResult

/**
 * Screen fingerprints (ALL confirmed from Logcat):
 *
 * Camera   → camera_capture_button ✅ (unique)
 * Chat     → ff_item rows ✅ (unique)
 * Map      → list-picker pills WITHOUT ff_item ✅ (unique)
 * Stories  → friend_card_frame ✅ (story circles, unique to Stories tab)
 * Spotlight→ none of the above → BLOCK ✅
 *
 * NOTE: ngs_spotlight_icon_container appears on ALL 5 screens (nav bar),
 * so it is only usable as a positive signal AFTER all 4 guards pass.
 *
 * NOTE: df_large_story appears on Stories DISCOVER section, NOT on Spotlight.
 * Do NOT use it as a Spotlight signal.
 */
class SnapchatSpotlightDetector : AppDetector {

    override fun detect(rootNode: AccessibilityNodeInfo?): DetectionResult {
        if (rootNode == null) return DetectionResult.NORMAL_SCREEN

        // ── GUARD 1: Camera ────────────────────────────────────────────────
        if (hasViewId(rootNode, "com.snapchat.android:id/camera_capture_button")) {
            Log.d(TAG, "NORMAL → Camera (camera_capture_button present)")
            return DetectionResult.NORMAL_SCREEN
        }

        // ── GUARD 2: Chat ──────────────────────────────────────────────────
        // ff_item = friend feed row (Received/Delivered/Streak etc.)
        if (hasViewId(rootNode, "com.snapchat.android:id/ff_item")) {
            Log.d(TAG, "NORMAL → Chat (ff_item present)")
            return DetectionResult.NORMAL_SCREEN
        }

        // ── GUARD 3: Map (Snap Map) ────────────────────────────────────────
        // Filter pills appear on BOTH Chat AND Map.
        // Distinguisher: pills present + NO ff_item = Map screen.
        if (isMapScreen(rootNode)) {
            Log.d(TAG, "NORMAL → Map (filter pills, no ff_item)")
            return DetectionResult.NORMAL_SCREEN
        }

        // ── GUARD 4: Stories ───────────────────────────────────────────────
        // friend_card_frame = friend story circles (ring icons at top of Stories tab)
        // Confirmed from Logcat: ONLY appears on Stories tab.
        // This was the missing guard causing Stories to be wrongly blocked!
        if (hasViewId(rootNode, "com.snapchat.android:id/friend_card_frame")) {
            Log.d(TAG, "NORMAL → Stories (friend_card_frame present)")
            return DetectionResult.NORMAL_SCREEN
        }

        // ── POSITIVE: Spotlight ────────────────────────────────────────────
        // All 4 safe screens have been ruled out.
        // ngs_spotlight_icon_container is in the nav bar on every screen,
        // but after passing all guards above, only Spotlight remains.
        val onSpotlight = hasViewId(
            rootNode,
            "com.snapchat.android:id/ngs_spotlight_icon_container"
        )

        return if (onSpotlight) {
            Log.d(TAG, "REELS → Spotlight ✅ (all guards passed)")
            DetectionResult.REELS_SCREEN
        } else {
            Log.d(TAG, "NORMAL → Transitional/unknown screen")
            DetectionResult.NORMAL_SCREEN
        }
    }

    // ── Map: pills present, chat rows absent ──────────────────────────────
    private fun isMapScreen(root: AccessibilityNodeInfo): Boolean {
        val hasPills = hasViewId(root, "list-picker-pill-Near Me")
                || hasViewId(root, "list-picker-pill-Best Friends")
                || hasViewId(root, "list-picker-pill-My AI")
                || hasViewId(root, "list-picker-pill-Groups")
        val hasChatRows = hasViewId(root, "com.snapchat.android:id/ff_item")
        return hasPills && !hasChatRows
    }

    private fun hasViewId(root: AccessibilityNodeInfo, viewId: String): Boolean =
        root.findAccessibilityNodeInfosByViewId(viewId).isNotEmpty()

    companion object {
        private const val TAG = "SNAPCHAT_DETECTOR"
    }
}