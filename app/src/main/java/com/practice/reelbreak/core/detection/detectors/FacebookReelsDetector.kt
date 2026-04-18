package com.practice.reelbreak.core.detection.detectors

import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.practice.reelbreak.domain.model.DetectionResult

class FacebookReelsDetector : AppDetector {

    companion object { 
        private const val TAG = "FB_DETECTOR"
    }

    override fun detect(rootNode: AccessibilityNodeInfo?): DetectionResult {
        if (rootNode == null) return DetectionResult.NORMAL_SCREEN

        // ── SAFE SCREEN GUARD ──────────────────────────────────────────────
        // "What's on your mind?" = compose box = pure home text feed (NO reels)
        // ✅ This is content-specific — it only appears in the feed, never in the Reels player
        //
        // ❌ DO NOT USE "Home, tab 1 of 6" — this is the bottom nav tab label
        //    and is ALWAYS present on every screen of Facebook, including Reels!
        if (containsExactText(rootNode, "What's on your mind?")) {
            Log.d(TAG, "NORMAL — home feed compose box found, pure text feed")
            return DetectionResult.NORMAL_SCREEN
        }

        // ── SIGNAL 1: "Are you interested in this reel?" ──────────────────
        // Facebook's reel feedback prompt — exclusive to any Reels surface
        // Appears in both home-feed embedded reels AND dedicated Reels/Shorts tab
        if (containsDescription(rootNode, "Are you interested in this reel?")) {
            Log.d(TAG, "REELS ✅ Signal 1: interested-in-reel prompt")
            return DetectionResult.REELS_SCREEN
        }

        // ── SIGNAL 2: Exact "Reels" label node ────────────────────────────
        // The Reels feed header / section label.
        // Using EXACT match to avoid false positives from reel captions.
        if (containsExactText(rootNode, "Reels")) {
            Log.d(TAG, "REELS ✅ Signal 2: 'Reels' label node")
            return DetectionResult.REELS_SCREEN
        }

        // ── SIGNAL 3: FB_SHORTS_UNIFIED_PLAYER fingerprint ────────────────
        // When the dedicated Shorts player is open, it shows:
        //   - "Follow" button (creator not yet followed)
        //   - Reaction count like "38K reactions" or "X reactions"
        //   - Share count like "Share, X shares"
        // These three together = only possible on full-screen reel player
        val hasFollow     = containsExactText(rootNode, "Follow")
        val hasReactions  = containsDescriptionPattern(rootNode, "reactions")
        val hasShareCount = containsDescriptionPattern(rootNode, "shares")

        if (hasFollow && (hasReactions || hasShareCount)) {
            Log.d(TAG, "REELS ✅ Signal 3: FB_SHORTS_UNIFIED_PLAYER fingerprint " +
                    "(Follow=$hasFollow, reactions=$hasReactions, shares=$hasShareCount)")
            return DetectionResult.REELS_SCREEN
        }

        // ── SIGNAL 4: ViewPager = Reels swipe container ───────────────────
        // Home feed uses RecyclerView. Reels player uses ViewPager for vertical swiping.
        if (hasViewPager(rootNode)) {
            Log.d(TAG, "REELS ✅ Signal 4: ViewPager swipe container found")
            return DetectionResult.REELS_SCREEN
        }

        Log.d(TAG, "NORMAL — no Reels signals matched")
        return DetectionResult.NORMAL_SCREEN
    }

    // ── Helpers ────────────────────────────────────────────────────────────

    private fun containsDescription(node: AccessibilityNodeInfo?, keyword: String): Boolean {
        if (node == null) return false
        val d = node.contentDescription?.toString() ?: ""
        val t = node.text?.toString() ?: ""
        if (d.contains(keyword, true) || t.contains(keyword, true)) return true
        for (i in 0 until node.childCount) {
            if (containsDescription(node.getChild(i), keyword)) return true
        }
        return false
    }

    /** Exact match — prevents partial hits from captions mentioning "reels" */
    private fun containsExactText(node: AccessibilityNodeInfo?, value: String): Boolean {
        if (node == null) return false
        val d = node.contentDescription?.toString()?.trim() ?: ""
        val t = node.text?.toString()?.trim() ?: ""
        if (d.equals(value, true) || t.equals(value, true)) return true
        for (i in 0 until node.childCount) {
            if (containsExactText(node.getChild(i), value)) return true
        }
        return false
    }

    /** Suffix/pattern match — for dynamic text like "38K reactions", "320 shares" */
    private fun containsDescriptionPattern(node: AccessibilityNodeInfo?, suffix: String): Boolean {
        if (node == null) return false
        val d = node.contentDescription?.toString() ?: ""
        val t = node.text?.toString() ?: ""
        if (d.endsWith(suffix, true) || t.endsWith(suffix, true)) return true
        for (i in 0 until node.childCount) {
            if (containsDescriptionPattern(node.getChild(i), suffix)) return true
        }
        return false
    }

    private fun hasViewPager(node: AccessibilityNodeInfo?): Boolean {
        if (node == null) return false
        val cls = node.className?.toString() ?: ""
        if (cls == "androidx.viewpager.widget.ViewPager" ||
            cls == "android.support.v4.view.ViewPager") return true
        for (i in 0 until node.childCount) {
            if (hasViewPager(node.getChild(i))) return true
        }
        return false
    }
}

//
//package com.practice.reelbreak.core.detection.detectors
//
//import android.util.Log
//import android.view.accessibility.AccessibilityEvent
//import android.view.accessibility.AccessibilityNodeInfo
//import com.practice.reelbreak.domain.model.DetectionResult
//
//class FacebookReelsDetector : AppDetector {
//
//    override fun onEvent(event: AccessibilityEvent) = Unit
//    override fun reset() {}
//
//    override fun detect(rootNode: AccessibilityNodeInfo?): DetectionResult {
//        if (rootNode == null) return DetectionResult.NORMAL_SCREEN
//        dumpFullTree(rootNode)
//        return DetectionResult.NORMAL_SCREEN  // OBSERVE ONLY — blocking off
//    }
//
//    private fun dumpFullTree(node: AccessibilityNodeInfo?, depth: Int = 0) {
//        if (node == null) return
//        val cls  = node.className?.toString() ?: ""
//        val id   = node.viewIdResourceName ?: ""
//        val text = node.text?.toString() ?: ""
//        val desc = node.contentDescription?.toString() ?: ""
//        if (cls.isNotEmpty() || id.isNotEmpty() || text.isNotEmpty() || desc.isNotEmpty()) {
//            Log.d("FB_SCAN",
//                "  ".repeat(depth.coerceAtMost(10)) +
//                        "cls=${cls.substringAfterLast('.')} " +
//                        "id=${id.substringAfterLast('/')} " +
//                        "text=$text desc=$desc sel=${node.isSelected}"
//            )
//        }
//        for (i in 0 until node.childCount) dumpFullTree(node.getChild(i), depth + 1)
//    }
//}