package com.practice.reelbreak.core.detection

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.practice.reelbreak.core.action.ActionController
import com.practice.reelbreak.core.debug.AccessibilityDebugLogger
import com.practice.reelbreak.core.engine.BlockingDecisionEngine
import com.practice.reelbreak.core.registry.ReelsDetectionRegistry
import com.practice.reelbreak.domain.model.DetectionResult
import com.practice.reelbreak.domain.model.ReelsSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


//class ReelsDetectionManager(
//    private val actionController: ActionController,
//    private val engine: BlockingDecisionEngine
//) {
//    val isOnReelsScreen: Boolean get() = session.reelsMode
//
//    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
//    private val session = ReelsSession()
//
//    // Minimum milliseconds between two reel counts (prevents multi-count per swipe)
//    private val MIN_MS_BETWEEN_COUNTS = 1200L
//    private val BLOCK_COOLDOWN_MS = 700L
//
//    // Timestamp of last reel counted
//    private var lastCountedMs: Long = 0L
//    private var lastBlockAttemptMs: Long = 0L
//    // Block check is one-shot per reel entry — reset by scroll
//    @Volatile private var hasCheckedBlockThisReel = false
//
//    fun processEvent(event: AccessibilityEvent, rootNode: AccessibilityNodeInfo?) {
//        val packageName = event.packageName?.toString() ?: return
//
//        if (!ReelsDetectionRegistry.isDetectionSupported(packageName)) {
//            resetSession()
//            return
//        }
//
//        val detector = AppDetectorRouter.getDetector(packageName)
//        if (detector == null) {
//            resetSession()
//            return
//        }
//
//        detector.onEvent(event)
//        val result = detector.detect(rootNode)
//
//        if (result == DetectionResult.REELS_SCREEN) {
//            session.reelsMode = true
//            session.currentApp = packageName
//
//            when (event.eventType) {
//                AccessibilityEvent.TYPE_VIEW_SCROLLED -> {
//                    val now = System.currentTimeMillis()
//                    if (now - lastCountedMs >= MIN_MS_BETWEEN_COUNTS) {
//                        lastCountedMs = now
//                        hasCheckedBlockThisReel = false
//                        Log.d("DETECT_MANAGER", "SCROLL detected → triggerReelAction (will COUNT)")
//                        triggerReelAction(packageName)
//                    }
//                }
//
//                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
//                    if (!hasCheckedBlockThisReel) {
//                        hasCheckedBlockThisReel = true
//                        Log.d("DETECT_MANAGER", "WINDOW_STATE entry → checkBlockOnly (NO count)")
//                        val now = System.currentTimeMillis()
//                        if (now - lastCountedMs >= MIN_MS_BETWEEN_COUNTS) {
//                            lastCountedMs = now
//                            triggerReelAction(packageName)
//                        }
//                    }
//                }
//
//                else -> {
//                    if (!hasCheckedBlockThisReel) {
//                        hasCheckedBlockThisReel = true
//                        checkBlockOnly(packageName)
//                    }
//                }
//            }
//        } else {
//            resetSession()
//        }
//    }
//
//
//    // Count + block check
//    // In ReelsDetectionManager.kt
//
//    private fun triggerReelAction(packageName: String?) {
//        val currentPackage = session.currentApp ?: return
//        scope.launch {
//            try {
//                // Ask your decision engine what to do
//                when (engine.decide()) {
//                    BlockingDecisionEngine.Decision.BLOCK -> {
//                        Log.d("REELSBREAK", "Decision=BLOCK for $currentPackage. Redirecting natively.")
//
//                        // ✅ Fire your actionController directly!
//                        // It cleanly triggers blockController.closeCurrentApp() which calls GLOBAL_ACTION_BACK
//                        actionController.triggerReelsBlock(currentPackage)
//                    }
//                    BlockingDecisionEngine.Decision.ALLOW -> {
//                        Log.d("REELSBREAK", "Decision=ALLOW → counting reel chunk")
//                        engine.onReelAllowed()
//                    }
//                    BlockingDecisionEngine.Decision.SKIP_REEL -> {
//                        Log.d("REELSBREAK", "Decision=SKIP for $currentPackage")
//                    }
//                }
//            } catch (e: Exception) {
//                Log.e("REELSBREAK", "Error in triggerReelAction: ${e.message}", e)
//            }
//        }
//    }
//
//    // Only check block (no count) — for first event on same reel
//    private fun checkBlockOnly(packageName: String?) {
//        val currentPackage = session.currentApp
//        scope.launch {
//            try {
//                if (engine.decide() == BlockingDecisionEngine.Decision.BLOCK) {
//                    Log.d("REELSBREAK", "Decision=BLOCK (checkOnly) for $currentPackage")
//                    actionController.triggerReelsBlock(currentPackage)
//                }
//            } catch (e: Exception) {
//                Log.e("REELSBREAK", "Error in checkBlockOnly: ${e.message}", e)
//            }
//        }
//    }
//
//    private fun resetSession() {
//        session.reelsMode = false
//        session.currentApp = null
//        session.scrollCount = 0
//        session.lastReelHash = 0
//        hasCheckedBlockThisReel = false
//        lastCountedMs = 0L
//    }
//
//    fun cancel() {
//        scope.cancel()
//    }
//}



//class ReelsDetectionManager(
//    private val actionController: ActionController,
//    private val engine: BlockingDecisionEngine
//) {
//    val isOnReelsScreen: Boolean get() = session.reelsMode
//
//    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
//    private val session = ReelsSession()
//
//    private val MIN_MS_BETWEEN_COUNTS = 1200L
//    private val BLOCK_COOLDOWN_MS = 700L
//
//    private var lastCountedMs: Long = 0L
//    private var lastBlockAttemptMs: Long = 0L
//
//    @Volatile
//    private var hasCheckedBlockThisSurface = false
//
//    fun processEvent(event: AccessibilityEvent, rootNode: AccessibilityNodeInfo?) {
//        val packageName = event.packageName?.toString() ?: return
//
//        if (!ReelsDetectionRegistry.isDetectionSupported(packageName)) {
//            resetSession()
//            return
//        }
//
//        val detector = AppDetectorRouter.getDetector(packageName) ?: run {
//            resetSession()
//            return
//        }
//
//        detector.onEvent(event)
//        val result = detector.detect(rootNode)
//
//        AccessibilityDebugLogger.logDetectionDecision(
//            packageName = packageName,
//            detectorName = detector.javaClass.simpleName,
//            phase = AccessibilityEvent.eventTypeToString(event.eventType),
//            matched = result == DetectionResult.REELS_SCREEN,
//            reason = "result=$result hasRoot=${rootNode != null}"
//        )
//
//        if (result != DetectionResult.REELS_SCREEN) {
//            Log.d("REELSBREAK", "Resetting session: not on reels for $packageName")
//            resetSession()
//            return
//        }
//
//        session.reelsMode = true
//        session.currentApp = packageName
//
//        val now = System.currentTimeMillis()
//
//        when (event.eventType) {
//            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
//            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED,
//            AccessibilityEvent.TYPE_VIEW_SCROLLED -> {
//                if (now - lastBlockAttemptMs < BLOCK_COOLDOWN_MS) {
//                    return
//                }
//
//                if (!hasCheckedBlockThisSurface) {
//                    hasCheckedBlockThisSurface = true
//                    lastBlockAttemptMs = now
//                    triggerBlockOrAllow(packageName, countIfAllowed = event.eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED)
//                }
//            }
//        }
//    }
//
//    private fun triggerBlockOrAllow(packageName: String, countIfAllowed: Boolean) {
//        scope.launch {
//            try {
//                when (engine.decide()) {
//                    BlockingDecisionEngine.Decision.BLOCK -> {
//                        Log.d("REELSBREAK", "Decision=BLOCK for $packageName")
//                        actionController.triggerReelsBlock(packageName)
//                    }
//
//                    BlockingDecisionEngine.Decision.ALLOW -> {
//                        if (countIfAllowed) {
//                            val now = System.currentTimeMillis()
//                            if (now - lastCountedMs >= MIN_MS_BETWEEN_COUNTS) {
//                                lastCountedMs = now
//                                engine.onReelAllowed()
//                            }
//                        }
//                    }
//
//                    BlockingDecisionEngine.Decision.SKIP_REEL -> {
//                        Log.d("REELSBREAK", "Decision=SKIP for $packageName")
//                    }
//                }
//            } catch (e: Exception) {
//                Log.e("REELSBREAK", "Error in triggerBlockOrAllow: ${e.message}", e)
//            }
//        }
//    }
//
//    private fun resetSession() {
//        session.reelsMode = false
//        session.currentApp = null
//        session.scrollCount = 0
//        session.lastReelHash = 0
//        hasCheckedBlockThisSurface = false
//        lastCountedMs = 0L
//        lastBlockAttemptMs = 0L
//    }
//
//    fun cancel() {
//        scope.cancel()
//    }
//}




class ReelsDetectionManager(
    private val actionController: ActionController,
    private val engine: BlockingDecisionEngine
) {
    val isOnReelsScreen: Boolean get() = session.reelsMode

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val session = ReelsSession()

    private val MIN_MS_BETWEEN_COUNTS = 1200L
    private val BLOCK_COOLDOWN_MS = 900L

    private var lastCountedMs: Long = 0L
    private var lastBlockAttemptMs: Long = 0L

    @Volatile
    private var hasCheckedBlockThisReel = false

    fun processEvent(event: AccessibilityEvent, rootNode: AccessibilityNodeInfo?) {
        val packageName = event.packageName?.toString() ?: return

        if (!ReelsDetectionRegistry.isDetectionSupported(packageName)) {
            resetSession()
            return
        }

        // ── CORE CHANGE: one generic call replaces all per-app detectors ──
        val isOnBlockedScreen = AppDetectorRouter.isBlockedContentVisible(packageName, rootNode)

        if (isOnBlockedScreen) {
            session.reelsMode = true
            session.currentApp = packageName
            Log.d("REELSBREAK", "DETECTED blocked screen for $packageName")

            when (event.eventType) {
                AccessibilityEvent.TYPE_VIEW_SCROLLED -> {
                    val now = System.currentTimeMillis()
                    if (now - lastCountedMs >= MIN_MS_BETWEEN_COUNTS) {
                        lastCountedMs = now
                        hasCheckedBlockThisReel = false
                        triggerReelAction(packageName)
                    }
                }

                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                    if (!hasCheckedBlockThisReel) {
                        hasCheckedBlockThisReel = true
                        val now = System.currentTimeMillis()
                        if (now - lastCountedMs >= MIN_MS_BETWEEN_COUNTS) {
                            lastCountedMs = now
                            triggerReelAction(packageName)
                        }
                    }
                }

                else -> {
                    if (!hasCheckedBlockThisReel) {
                        hasCheckedBlockThisReel = true
                        checkBlockOnly(packageName)
                    }
                }
            }
        } else {
            Log.d("REELSBREAK", "Not on blocked screen for $packageName — resetting session")
            resetSession()
        }
    }

    private fun triggerReelAction(packageName: String?) {
        val currentPackage = session.currentApp ?: return
        val now = System.currentTimeMillis()
        if (now - lastBlockAttemptMs < BLOCK_COOLDOWN_MS) return
        lastBlockAttemptMs = now

        scope.launch {
            try {
                when (engine.decide()) {
                    BlockingDecisionEngine.Decision.BLOCK -> {
                        Log.d("REELSBREAK", "Decision=BLOCK for $currentPackage")
                        actionController.triggerReelsBlock(currentPackage)
                    }
                    BlockingDecisionEngine.Decision.ALLOW -> {
                        Log.d("REELSBREAK", "Decision=ALLOW → counting reel")
                        engine.onReelAllowed()
                    }
                    BlockingDecisionEngine.Decision.SKIP_REEL -> {
                        Log.d("REELSBREAK", "Decision=SKIP for $currentPackage")
                    }
                }
            } catch (e: Exception) {
                Log.e("REELSBREAK", "Error in triggerReelAction: ${e.message}", e)
            }
        }
    }

    private fun checkBlockOnly(packageName: String?) {
        val currentPackage = session.currentApp ?: return
        val now = System.currentTimeMillis()
        if (now - lastBlockAttemptMs < BLOCK_COOLDOWN_MS) return
        lastBlockAttemptMs = now

        scope.launch {
            try {
                if (engine.decide() == BlockingDecisionEngine.Decision.BLOCK) {
                    Log.d("REELSBREAK", "Decision=BLOCK (checkOnly) for $currentPackage")
                    actionController.triggerReelsBlock(currentPackage)
                }
            } catch (e: Exception) {
                Log.e("REELSBREAK", "Error in checkBlockOnly: ${e.message}", e)
            }
        }
    }

    private fun resetSession() {
        session.reelsMode = false
        session.currentApp = null
        session.scrollCount = 0
        session.lastReelHash = 0
        hasCheckedBlockThisReel = false
        lastCountedMs = 0L
        // NOTE: lastBlockAttemptMs is intentionally NOT reset here
        // so the cooldown persists across rapid screen transitions
    }

    fun cancel() {
        scope.cancel()
    }
}