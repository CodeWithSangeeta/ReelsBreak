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


//class ReelsDetectionManager(
//    private val actionController: ActionController,
//    private val engine: BlockingDecisionEngine
//) {
//    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
//    private val session = ReelsSession()
//
//    fun processEvent(event: AccessibilityEvent, rootNode: AccessibilityNodeInfo?) {
//        val packageName = event.packageName?.toString() ?: return
//
//        if (!SupportedAppsRegistry.isSupported(packageName)) {
//            resetSession()
//            return
//        }
//
//        // ✅ STEP 1: Feed raw event to detector FIRST
//        // This updates internal state (e.g. Facebook tab tracking)
//        // MUST happen before detect() is called
//        val detector = AppDetectorRouter.getDetector(packageName)
//        detector.onEvent(event) // ← THE KEY LINE — was missing before
//
//        // Debug tree dump (remove once detection is confirmed working)
//        if (packageName == "com.facebook.katana" ||
//            packageName == "com.instagram.android" ||
//            packageName == "com.snapchat.android") {
//            dumpTree(rootNode, packageName)
//        }
//
//        // ✅ STEP 2: Now ask for detection result (reads updated state)
//        val result = detector.detect(rootNode)
//
//        Log.d("REELSBREAK", "Detection result: $result for $packageName")
//
//        if (result == DetectionResult.REELS_SCREEN) {
//            session.reelsMode = true
//            session.currentApp = packageName
//            handleReelsScreen()
//        } else {
//            session.reelsMode = false
//            session.scrollCount = 0
//        }
//    }
//
//    private fun dumpTree(node: AccessibilityNodeInfo?, packageName: String, depth: Int = 0) {
//        if (node == null) return
//        val viewId = node.viewIdResourceName ?: "no-id"
//        val text   = node.text?.toString() ?: ""
//        val desc   = node.contentDescription?.toString() ?: ""
//        if (viewId != "no-id" || text.isNotEmpty()) {
//            Log.d("REELSBREAK_TREE", "${"  ".repeat(depth)}[$viewId] text=$text desc=$desc")
//        }
//        for (i in 0 until node.childCount) {
//            dumpTree(node.getChild(i), packageName, depth + 1)
//        }
//    }
//
//    private fun handleReelsScreen() {
//        val currentPackage = session.currentApp
//        scope.launch {
//            when (engine.decide()) {
//                BlockingDecisionEngine.Decision.BLOCK -> {
//                    Log.d("REELSBREAK", "Decision: BLOCK → $currentPackage")
//                    actionController.triggerBlock(currentPackage)
//                }
//                BlockingDecisionEngine.Decision.ALLOW -> {
//                    Log.d("REELSBREAK", "Decision: ALLOW → user can watch")
//                }
//                BlockingDecisionEngine.Decision.SKIP_REEL -> {
//                    Log.d("REELSBREAK", "Decision: SKIP_REEL → future curated mode")
//                }
//            }
//        }
//    }
//
//    private fun resetSession() {
//        session.reelsMode = false
//        session.scrollCount = 0
//    }
//}


class ReelsDetectionManager(
    private val actionController: ActionController,
    private val engine: BlockingDecisionEngine
) {
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

        if (result == DetectionResult.REELS_SCREEN) {
            session.reelsMode = true
            session.currentApp = packageName

            // ✅ Only trigger if not already blocking
            if (!isBlockingInProgress) {
                handleReelsScreen()
            }
        } else {
            session.reelsMode = false
            session.scrollCount = 0
            isBlockingInProgress = false  // ✅ Reset flag when leaving Reels
        }
    }

    private fun handleReelsScreen() {
        val currentPackage = session.currentApp
        isBlockingInProgress = true  // ✅ Lock immediately — before coroutine

        scope.launch {
            try {
                when (engine.decide()) {
                    BlockingDecisionEngine.Decision.BLOCK -> {
                        Log.d("REELSBREAK", "Decision: BLOCK → $currentPackage")
                        actionController.triggerBlock(currentPackage)
                    }
                    BlockingDecisionEngine.Decision.ALLOW -> {
                        Log.d("REELSBREAK", "Decision: ALLOW → not blocking")
                        isBlockingInProgress = false  // ✅ Unlock on ALLOW
                    }
                    BlockingDecisionEngine.Decision.SKIP_REEL -> {
                        isBlockingInProgress = false
                    }
                }
            } catch (e: Exception) {
                Log.e("REELSBREAK", "Error in handleReelsScreen: ${e.message}")
                isBlockingInProgress = false  // ✅ Always unlock on error
            }
        }
    }

    private fun resetSession() {
        session.reelsMode = false
        session.scrollCount = 0
        isBlockingInProgress = false
    }
}