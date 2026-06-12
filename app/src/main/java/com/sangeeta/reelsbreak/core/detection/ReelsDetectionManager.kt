package com.sangeeta.reelsbreak.core.detection

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.sangeeta.reelsbreak.core.action.ActionController
import com.sangeeta.reelsbreak.core.engine.BlockingDecisionEngine
import com.sangeeta.reelsbreak.core.registry.ReelsDetectionRegistry
import com.sangeeta.reelsbreak.domain.model.ReelsSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ReelsDetectionManager(
    private val actionController: ActionController,
    private val engine: BlockingDecisionEngine
) {
    val isOnReelsScreen: Boolean
        get() = session.reelsMode

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val session = ReelsSession()
    private val atomicEvaluationMutex = Mutex()

    private val MIN_MS_BETWEEN_COUNTS = 1200L
    private val BLOCK_COOLDOWN_MS = 80L

    private val FLOW_BLOCK_RETRY_MS = 180L
    private val FLOW_GUARD_WINDOW_MS = 700L
    private val EXIT_STABLE_MS = 400L

    private var lastCountedMs: Long = 0L
    private var lastBlockAttemptMs: Long = 0L
    private var lastTimeTickMs: Long = 0L
    private var lastReelsSeenMs: Long = 0L

    private var blockingInProgress = false
    private var lastBlockedPackage: String? = null
    private var blockGuardUntilMs: Long = 0L

    @Volatile
    private var hasCheckedBlockThisReel = false

    fun processEvent(event: AccessibilityEvent, rootNode: AccessibilityNodeInfo?) {
        val packageName = event.packageName?.toString() ?: return

        if (!ReelsDetectionRegistry.isDetectionSupported(packageName)) {
            resetSession()
            return
        }

        scope.launch(Dispatchers.Main) {
            atomicEvaluationMutex.withLock {
                runDetectionPipeline(event, packageName, rootNode)
            }
        }
    }

    private suspend fun runDetectionPipeline(
        event: AccessibilityEvent,
        packageName: String,
        rootNode: AccessibilityNodeInfo?
    ) {
        val isOnBlockedScreen = AppDetectorRouter.isBlockedContentVisible(packageName, rootNode)

        Log.d(
            "RB_EVENT",
            "event=${event.eventType} " +
                    "pkg=$packageName " +
                    "detected=$isOnBlockedScreen"
        )

        val previousDetected = session.reelsMode

        if (previousDetected && !isOnBlockedScreen) {
            Log.d(
                "RB_SNAPSHOT",
                "Lost detection! " +
                        "pkg=$packageName " +
                        "event=${event.eventType}"
            )
        }

        if (isOnBlockedScreen) {
            val now = System.currentTimeMillis()
            lastReelsSeenMs = now

            if (!session.reelsMode) {
                session.reelsMode = true
                session.currentApp = packageName
                lastTimeTickMs = now
            } else {
                val delta = now - lastTimeTickMs
                if (delta in 1000L..5000L) {
                    lastTimeTickMs = now
                    engine.onMindfulTimeSpent(delta)
                }
            }

            when (event.eventType) {
                AccessibilityEvent.TYPE_VIEW_SCROLLED -> {
                    val nowScroll = System.currentTimeMillis()
                    if (nowScroll - lastCountedMs >= MIN_MS_BETWEEN_COUNTS) {
                        lastCountedMs = nowScroll
                        hasCheckedBlockThisReel = false
                        triggerReelAction()
                    } else {
                        checkBlockOnly()
                    }
                }

                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                    if (!hasCheckedBlockThisReel) {
                        hasCheckedBlockThisReel = true
                        lastCountedMs = System.currentTimeMillis()
                        triggerReelAction()
                    } else {
                        checkBlockOnly()
                    }
                }

                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED,
                AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                    checkBlockOnly()
                }

                else -> {
                    if (!hasCheckedBlockThisReel) {
                        hasCheckedBlockThisReel = true
                        checkBlockOnly()
                    }
                }
            }
        } else {
            val now = System.currentTimeMillis()

            if (blockingInProgress && now - lastReelsSeenMs < EXIT_STABLE_MS) {
                return
            }
            Log.d(
                "RB_RESET",
                "detected=false " +
                        "delta=${System.currentTimeMillis()-lastReelsSeenMs}"
            )
            resetSession()
        }
    }

    private suspend fun triggerReelAction() {
        when (engine.decide()) {
            BlockingDecisionEngine.Decision.BLOCK -> {
                enforceFlowBlock()
            }

            BlockingDecisionEngine.Decision.ALLOW -> {
                val now = System.currentTimeMillis()
                if (now - lastBlockAttemptMs < BLOCK_COOLDOWN_MS) return
                lastBlockAttemptMs = now
                engine.onReelAllowed()
            }
        }
    }

    private suspend fun checkBlockOnly() {

        if (engine.decide() == BlockingDecisionEngine.Decision.BLOCK) {
            enforceFlowBlock()
        }
    }

    private suspend fun enforceFlowBlock() {
        val currentPackage = session.currentApp ?: return
        val now = System.currentTimeMillis()

        val samePackageGuard = lastBlockedPackage == currentPackage
        val guardActive = blockingInProgress && samePackageGuard && now < blockGuardUntilMs
        val enoughTimePassed = now - lastBlockAttemptMs >= FLOW_BLOCK_RETRY_MS

        Log.d(
            "RB_FLOW",
            "pkg=$currentPackage " +
                    "blocking=$blockingInProgress " +
                    "guard=$guardActive " +
                    "enough=$enoughTimePassed"
        )

        if ((!blockingInProgress || guardActive) && enoughTimePassed) {
            lastBlockAttemptMs = now
            blockingInProgress = true
            lastBlockedPackage = currentPackage
            blockGuardUntilMs = now + FLOW_GUARD_WINDOW_MS
            actionController.triggerReelsBlock(currentPackage)
            Log.d(
                "RB_FLOW",
                "BACK_TRIGGERED pkg=$currentPackage"
            )
        }
    }

    private fun resetSession() {
        Log.d(
            "RB_RESET",
            "Session reset"
        )
        session.reelsMode = false
        session.currentApp = null
        session.scrollCount = 0
        session.lastReelHash = 0

        hasCheckedBlockThisReel = false
        lastCountedMs = 0L
        lastBlockAttemptMs = 0L
        lastTimeTickMs = 0L
        lastReelsSeenMs = 0L

        blockingInProgress = false
        lastBlockedPackage = null
        blockGuardUntilMs = 0L
    }

    fun cancel() {
        scope.cancel()
    }
}