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
    private var lastTimeTickMs: Long = 0L

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
            val now = System.currentTimeMillis()

            if (!session.reelsMode) {
                // first time entering reels / blocked screen
                session.reelsMode = true
                session.currentApp = packageName
                lastTimeTickMs = now
            } else {
                // already on reels screen → accumulate time
                val delta = now - lastTimeTickMs
                if (delta in 1000L..5000L) {
                    lastTimeTickMs = now
                    scope.launch {
                        engine.onMindfulTimeSpent(delta)
                    }
                }
            }

            Log.d("REELSBREAK", "DETECTED blocked screen for $packageName")

            when (event.eventType) {
                AccessibilityEvent.TYPE_VIEW_SCROLLED -> {
                    val nowScroll = System.currentTimeMillis()
                    if (nowScroll - lastCountedMs >= MIN_MS_BETWEEN_COUNTS) {
                        lastCountedMs = nowScroll
                        hasCheckedBlockThisReel = false
                        triggerReelAction(packageName)
                    }
                }

                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                    if (!hasCheckedBlockThisReel) {
                        hasCheckedBlockThisReel = true
                        val nowWin = System.currentTimeMillis()
                        if (nowWin - lastCountedMs >= MIN_MS_BETWEEN_COUNTS) {
                            lastCountedMs = nowWin
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
        lastTimeTickMs = 0L
    }

    fun cancel() {
        scope.cancel()
    }
}