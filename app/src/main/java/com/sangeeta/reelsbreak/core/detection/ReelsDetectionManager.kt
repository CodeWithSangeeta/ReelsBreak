package com.practice.reelbreak.core.detection

import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.practice.reelbreak.core.action.ActionController
import com.practice.reelbreak.core.engine.BlockingDecisionEngine
import com.practice.reelbreak.core.registry.ReelsDetectionRegistry
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

    private  val MIN_MS_BETWEEN_COUNTS = 1200L
    private  val BLOCK_COOLDOWN_MS = 900L

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

        val isOnBlockedScreen = AppDetectorRouter.isBlockedContentVisible(packageName, rootNode)

        if (isOnBlockedScreen) {
            val now = System.currentTimeMillis()

            if (!session.reelsMode) {
                session.reelsMode = true
                session.currentApp = packageName
                lastTimeTickMs = now
            } else {
                val delta = now - lastTimeTickMs
                if (delta in 1000L..5000L) {
                    lastTimeTickMs = now
                    scope.launch {
                        engine.onMindfulTimeSpent(delta)
                    }
                }
            }

            when (event.eventType) {
                AccessibilityEvent.TYPE_VIEW_SCROLLED -> {
                    val nowScroll = System.currentTimeMillis()
                    if (nowScroll - lastCountedMs >= MIN_MS_BETWEEN_COUNTS) {
                        lastCountedMs = nowScroll
                        hasCheckedBlockThisReel = false
                        triggerReelAction()
                    }
                }
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                    if (!hasCheckedBlockThisReel) {
                        hasCheckedBlockThisReel = true
                        val nowWin = System.currentTimeMillis()
                        if (nowWin - lastCountedMs >= MIN_MS_BETWEEN_COUNTS) {
                            lastCountedMs = nowWin
                            triggerReelAction()
                        }
                    }
                }
                else -> {
                    if (!hasCheckedBlockThisReel) {
                        hasCheckedBlockThisReel = true
                        checkBlockOnly()
                    }
                }
            }
        } else {
            resetSession()
        }
    }

    private fun triggerReelAction() {
        val currentPackage = session.currentApp ?: return
        val now = System.currentTimeMillis()
        if (now - lastBlockAttemptMs < BLOCK_COOLDOWN_MS) return
        lastBlockAttemptMs = now

        scope.launch {
            try {
                when (engine.decide()) {
                    BlockingDecisionEngine.Decision.BLOCK -> {
                        actionController.triggerReelsBlock(currentPackage)
                    }
                    BlockingDecisionEngine.Decision.ALLOW -> {
                        engine.onReelAllowed()
                    }
                    else -> {}
                }
            } catch (_: Exception) {}
        }
    }

    private fun checkBlockOnly() {
        val currentPackage = session.currentApp ?: return
        val now = System.currentTimeMillis()
        if (now - lastBlockAttemptMs < BLOCK_COOLDOWN_MS) return
        lastBlockAttemptMs = now

        scope.launch {
            try {
                if (engine.decide() == BlockingDecisionEngine.Decision.BLOCK) {
                    actionController.triggerReelsBlock(currentPackage)
                }
            } catch (_: Exception) {}
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