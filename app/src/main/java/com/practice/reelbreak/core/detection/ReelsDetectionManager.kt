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

    val isOnReelsScreen: Boolean get() = session.reelsMode

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val session = ReelsSession()

    // Minimum milliseconds between two reel counts (prevents multi-count per swipe)
    private val MIN_MS_BETWEEN_COUNTS = 1200L

    // Timestamp of last reel counted
    private var lastCountedMs: Long = 0L

    // Block check is one-shot per reel entry — reset by scroll
    @Volatile private var hasCheckedBlockThisReel = false

    fun processEvent(event: AccessibilityEvent, rootNode: AccessibilityNodeInfo?) {
        val packageName = event.packageName?.toString() ?: return

        if (!SupportedAppsRegistry.isSupported(packageName)) {
            resetSession()
            return
        }

        val detector = AppDetectorRouter.getDetector(packageName)
        if (detector == null) {
            resetSession()
            return
        }

        detector.onEvent(event)
        val result = detector.detect(rootNode)

        if (result == DetectionResult.REELS_SCREEN) {
            session.reelsMode = true
            session.currentApp = packageName

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
            resetSession()
        }
    }

//    fun processEvent(event: AccessibilityEvent, rootNode: AccessibilityNodeInfo?) {
//        val packageName = event.packageName?.toString() ?: return
//
//        if (!SupportedAppsRegistry.isSupported(packageName)) {
//            resetSession()
//            return
//        }
//
//        val detector = AppDetectorRouter.getDetector(packageName)
//        detector.onEvent(event)
//        val result = detector.detect(rootNode)
//
//
//        if (result == DetectionResult.REELS_SCREEN) {
//            session.reelsMode = true
//            session.currentApp = packageName
//
//            when (event.eventType) {
//
//                // ── User swiped to a new reel ─────────────────────────
//                AccessibilityEvent.TYPE_VIEW_SCROLLED -> {
//                    val now = System.currentTimeMillis()
//                    if (now - lastCountedMs >= MIN_MS_BETWEEN_COUNTS) {
//                        lastCountedMs = now
//                        hasCheckedBlockThisReel = false // reset so block is re-evaluated
//                        Log.d("REELSBREAK", "SCROLL detected → counting + block check")
//                        triggerReelAction(packageName)
//                    } else {
//                        Log.d("REELSBREAK", "SCROLL too soon (${now - lastCountedMs}ms) → skipped")
//                    }
//                }
//
//                // ── First entry into reels screen ─────────────────────
//                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
//                    if (!hasCheckedBlockThisReel) {
//                        hasCheckedBlockThisReel = true
//                        val now = System.currentTimeMillis()
//                        if (now - lastCountedMs >= MIN_MS_BETWEEN_COUNTS) {
//                            lastCountedMs = now
//                            Log.d("REELSBREAK", "WINDOW_STATE_CHANGED → first reel entry")
//                            triggerReelAction(packageName)
//                        }
//                    }
//                }
//
//                // ── All other events: only check block, never count ───
//                else -> {
//                    if (!hasCheckedBlockThisReel) {
//                        hasCheckedBlockThisReel = true
//                        checkBlockOnly(packageName)
//                    }
//                }
//            }
//
//        } else {
//            resetSession()
//        }
//    }

    // Count + block check
    private fun triggerReelAction(packageName: String?) {
        val currentPackage = session.currentApp
        scope.launch {
            try {
                when (engine.decide()) {
                    BlockingDecisionEngine.Decision.BLOCK -> {
                        Log.d("REELSBREAK", "Decision=BLOCK for $currentPackage")
                        actionController.triggerBlock(currentPackage)
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

    // Only check block (no count) — for first event on same reel
    private fun checkBlockOnly(packageName: String?) {
        val currentPackage = session.currentApp
        scope.launch {
            try {
                if (engine.decide() == BlockingDecisionEngine.Decision.BLOCK) {
                    Log.d("REELSBREAK", "Decision=BLOCK (checkOnly) for $currentPackage")
                    actionController.triggerBlock(currentPackage)
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
    }
}