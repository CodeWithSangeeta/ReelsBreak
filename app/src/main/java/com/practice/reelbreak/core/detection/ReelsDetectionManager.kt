package com.practice.reelbreak.core.detection

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.practice.reelbreak.core.action.ActionController
import com.practice.reelbreak.core.engine.BlockingDecisionEngine
import com.practice.reelbreak.core.registry.SupportedAppsRegistry
import com.practice.reelbreak.domain.model.DetectionResult
import com.practice.reelbreak.domain.model.ReelsSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class ReelsDetectionManager(
    private val actionController: ActionController,
    private val engine: BlockingDecisionEngine
) {
    val isOnReelsScreen: Boolean
        get() = session.reelsMode
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val session = ReelsSession()

    // ✅ Prevents 20 simultaneous coroutines firing for one Reels visit
    @Volatile private var isBlockingInProgress = false

    fun processEvent(event: AccessibilityEvent, rootNode: AccessibilityNodeInfo?) {
        val packageName = event.packageName?.toString() ?: return

        if (!SupportedAppsRegistry.isSupported(packageName)) {
            resetSession()
            return
        }

        val detector = AppDetectorRouter.getDetector(packageName)
        detector.onEvent(event)

        val result = detector.detect(rootNode)
        Log.d("REELSBREAK", "Detection result: $result for $packageName")

//        if (result == DetectionResult.REELS_SCREEN) {
//            session.reelsMode = true
//            session.currentApp = packageName
//
//            // ✅ Only trigger if not already blocking
//            if (!isBlockingInProgress) {
//                handleReelsScreen()
//            }
//        } else {
//            session.reelsMode = false
//            session.scrollCount = 0
//            isBlockingInProgress = false  // ✅ Reset flag when leaving Reels
//        }


        if (result == DetectionResult.REELS_SCREEN) {
            session.reelsMode = true
            session.currentApp = packageName
            if (!isBlockingInProgress) {
                handleReelsScreen(rootNode, packageName)
            }
        } else {
            resetSession()
        }
    }



//    private fun handleReelsScreen(detectedCreator: String? = null) {
//        val currentPackage = session.currentApp
//        isBlockingInProgress = true  // ✅ Lock immediately — before coroutine
//
//        scope.launch {
//            try {
//                when (engine.decide(detectedCreator)) {
//                    BlockingDecisionEngine.Decision.BLOCK -> {
//                        Log.d("REELSBREAK", "Decision: BLOCK → $currentPackage")
//                        actionController.triggerBlock(currentPackage)
//                    }
//                    BlockingDecisionEngine.Decision.ALLOW -> {
//                        Log.d("REELSBREAK", "Decision: ALLOW → not blocking")
//                        engine.onReelAllowed()
//                        isBlockingInProgress = false  // ✅ Unlock on ALLOW
//                    }
//                    BlockingDecisionEngine.Decision.SKIP_REEL -> {
//                        isBlockingInProgress = false
//                    }
//                }
//            } catch (e: Exception) {
//                Log.e("REELSBREAK", "Error in handleReelsScreen: ${e.message}")
//                isBlockingInProgress = false  // ✅ Always unlock on error
//            }
//        }
//    }


//    private fun handleReelsScreen(rootNode: AccessibilityNodeInfo?, packageName: String?) {
//        val currentPackage = session.currentApp
//        isBlockingInProgress = true
//
//        // --- NEW: compute a simple fingerprint for the current reel screen ---
//        val reelFingerprint = computeReelFingerprint(rootNode, packageName)
//        val isSameReelAsLast = reelFingerprint != 0 && reelFingerprint == session.lastReelHash
//
//        scope.launch {
//            try {
//                when (engine.decide()) {
//                    BlockingDecisionEngine.Decision.BLOCK -> {
//                        Log.d("REELSBREAK", "Decision BLOCK $currentPackage")
//                        actionController.triggerBlock(currentPackage)
//                    }
//                    BlockingDecisionEngine.Decision.ALLOW -> {
//                        Log.d(
//                            "REELSBREAK",
//                            "Decision ALLOW (sameReel=$isSameReelAsLast) for $currentPackage"
//                        )
//
//                        // Only count when reel actually changed
//                        if (!isSameReelAsLast) {
//                            engine.onReelAllowed()
//                            session.lastReelHash = reelFingerprint
//                        }
//
//                        isBlockingInProgress = false
//                    }
//                    BlockingDecisionEngine.Decision.SKIP_REEL -> {
//                        isBlockingInProgress = false
//                    }
//                }
//            } catch (e: Exception) {
//                Log.e("REELSBREAK", "Error in handleReelsScreen ${e.message}")
//                isBlockingInProgress = false
//            }
//        }
//    }


    private fun handleReelsScreen(rootNode: AccessibilityNodeInfo?, packageName: String?) {
        val currentPackage = session.currentApp
        isBlockingInProgress = true

        val reelFingerprint = computeReelFingerprint(rootNode, packageName)
        val hasValidFingerprint = reelFingerprint != 0
        val isSameReelAsLast = hasValidFingerprint && reelFingerprint == session.lastReelHash

        // Same reel as last time — do nothing, stay blocked
        if (isSameReelAsLast) return

        scope.launch {
            try {
                when (engine.decide()) {
                    BlockingDecisionEngine.Decision.BLOCK -> {
                        Log.d("REELSBREAK", "Decision BLOCK $currentPackage")
                        actionController.triggerBlock(currentPackage)
                        isBlockingInProgress = false
                    }

                    BlockingDecisionEngine.Decision.ALLOW -> {
                        if (hasValidFingerprint) {
                            Log.d("REELSBREAK", "New reel counted hash=$reelFingerprint for $currentPackage")
                            engine.onReelAllowed()
                            session.lastReelHash = reelFingerprint
                            // ✅ Stay blocked — don't set isBlockingInProgress = false
                            // It only resets in resetSession() when user leaves reels
                        } else {
                            Log.d("REELSBREAK", "ALLOW but no valid fingerprint — skip count")
                            isBlockingInProgress = false
                        }
                    }

                    BlockingDecisionEngine.Decision.SKIP_REEL -> {
                        Log.d("REELSBREAK", "SKIP_REEL $currentPackage")
                        isBlockingInProgress = false
                    }
                }
            } catch (e: Exception) {
                Log.e("REELSBREAK", "Error in handleReelsScreen ${e.message}", e)
                isBlockingInProgress = false
            }
        }
    }

    private fun computeReelFingerprint(
        rootNode: AccessibilityNodeInfo?,
        packageName: String?
    ): Int {
        if (rootNode == null || packageName == null) return 0

        // Very simple fingerprint: combine app package and root hash
        // Later you can refine this to use specific child node text/ids.
        return 31 * packageName.hashCode() + rootNode.hashCode()
    }

    private fun resetSession() {
        session.reelsMode = false
        session.currentApp = null
        session.scrollCount = 0
        session.lastReelHash = 0   //add this
        isBlockingInProgress = false
    }
}