package com.practice.reelbreak.core.detection.detectors

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