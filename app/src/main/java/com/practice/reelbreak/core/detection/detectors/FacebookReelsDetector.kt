package com.practice.reelbreak.core.detection.detectors


import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.practice.reelbreak.domain.model.DetectionResult

class FacebookReelsDetector : AppDetector {

    // Persists between events — this is why AppDetectorRouter must use singletons
    private var isOnReelsTab = false

//    override fun onEvent(event: AccessibilityEvent) {
//        val pkg = event.packageName?.toString() ?: return
//        if (pkg != "com.facebook.katana") return
//
//        val desc = event.contentDescription?.toString() ?: ""
//        val text = event.text?.joinToString(" ") ?: ""
//        val combined = "$desc $text"
//
//        Log.d(TAG, "onEvent → type=${event.eventType} | desc=$desc | text=$text")
//
//        when {
//            // Reels tab tapped
//            combined.contains("Reels, tab 2", ignoreCase = true) -> {
//                Log.d(TAG, "📲 Reels tab SELECTED → REELS_SCREEN")
//                isOnReelsTab = true
//            }
//            // Any other tab tapped → reset
//            combined.contains("tab 1 of 6", ignoreCase = true) ||
//                    combined.contains("tab 3 of 6", ignoreCase = true) ||
//                    combined.contains("tab 4 of 6", ignoreCase = true) ||
//                    combined.contains("tab 5 of 6", ignoreCase = true) ||
//                    combined.contains("tab 6 of 6", ignoreCase = true) -> {
//                Log.d(TAG, "📲 Other tab SELECTED → reset NORMAL_SCREEN")
//                isOnReelsTab = false
//            }
//        }
//    }
//
//    override fun detect(rootNode: AccessibilityNodeInfo?): DetectionResult {
//        // Facebook Reels renders in SurfaceView — rootNode NEVER shows Reels content
//        // Detection is purely state-based from onEvent() tab click tracking
//        return if (isOnReelsTab) {
//            Log.d(TAG, "✅ REELS_SCREEN — Reels tab is active")
//            DetectionResult.REELS_SCREEN
//        } else {
//            Log.d(TAG, "NORMAL_SCREEN")
//            DetectionResult.NORMAL_SCREEN
//        }
//    }



    // FacebookReelsDetector.kt — replace onEvent entirely
    override fun onEvent(event: AccessibilityEvent) {
        // LOG A — does onEvent get called at all?
        Log.d("FB_FINAL", "A: onEvent called | type=${event.eventType} | pkg=${event.packageName}")

        val pkg = event.packageName?.toString() ?: return

        // LOG B — is it being filtered out?
        if (pkg != "com.facebook.katana") {
            Log.d("FB_FINAL", "B: FILTERED OUT — pkg was '$pkg'")
            return
        }

        val desc = event.contentDescription?.toString() ?: ""
        val text = event.text?.joinToString(" ") ?: ""
        val combined = "$desc $text"

        // LOG C — what is the actual combined string?
        Log.d("FB_FINAL", "C: combined = '$combined'")

        when {
            combined.contains("Reels, tab 2", ignoreCase = true) -> {
                isOnReelsTab = true
                Log.d("FB_FINAL", "D: ✅ isOnReelsTab = TRUE")
            }
            combined.contains("tab 1 of 6", ignoreCase = true) ||
                    combined.contains("tab 3 of 6", ignoreCase = true) ||
                    combined.contains("tab 4 of 6", ignoreCase = true) ||
                    combined.contains("tab 5 of 6", ignoreCase = true) ||
                    combined.contains("tab 6 of 6", ignoreCase = true) -> {
                isOnReelsTab = false
                Log.d("FB_FINAL", "D: isOnReelsTab = FALSE (other tab)")
            }
            else -> {
                // LOG E — event arrived but matched nothing
                Log.d("FB_FINAL", "E: NO MATCH — combined was '$combined'")
            }
        }
    }

    override fun detect(rootNode: AccessibilityNodeInfo?): DetectionResult {
        // LOG F — what is state when detect is called?
        Log.d("FB_FINAL", "F: detect() | isOnReelsTab=$isOnReelsTab")
        return if (isOnReelsTab) DetectionResult.REELS_SCREEN else DetectionResult.NORMAL_SCREEN
    }




    fun reset() {
        isOnReelsTab = false
        Log.d(TAG, "State reset")
    }

    companion object {
        private const val TAG = "FACEBOOK_DETECTOR"
    }
}