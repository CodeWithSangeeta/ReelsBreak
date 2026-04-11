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

/**
 * ReelsDetectionManager — Detects reel screens and triggers blocking.
 *
 * RESPONSIBILITIES:
 * 1. Receive accessibility events from the service
 * 2. Detect if the current screen is a reels/shorts screen
 * 3. Ask BlockingDecisionEngine what to do
 * 4. Execute the decision via ActionController
 *
 * WHY CoroutineScope here?
 * BlockingDecisionEngine.decide() is a suspend function (reads DataStore).
 * We need a scope to launch coroutines from non-suspend code
 * (onAccessibilityEvent is a regular callback, not a coroutine).
 *
 * SupervisorJob: if one coroutine fails, others keep running.
 * Dispatchers.IO: runs on background thread, safe for DataStore reads.
 */
class ReelsDetectionManager(
    private val actionController: ActionController,
    private val engine: BlockingDecisionEngine
) {
    // Coroutine scope for this manager
    // Lives as long as the AccessibilityService lives
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val session = ReelsSession()
    private val MIN_SCROLL_INTERVAL = 1200L

    fun processEvent(event: AccessibilityEvent, rootNode: AccessibilityNodeInfo?) {
        val packageName = event.packageName?.toString() ?: return

        // Ignore unsupported apps
        if (!SupportedAppsRegistry.isSupported(packageName)) {
            resetSession()
            return
        }


        // ── DEBUG TREE DUMP (remove after finding view IDs) ───────────────
        // This prints every view ID visible on screen right now.
        // Open Instagram Reels screen → watch Logcat → find unique IDs.
        // Do the same for Snapchat Spotlight.
        if (packageName == "com.instagram.android" ||
            packageName == "com.snapchat.android"
        ) {
            dumpTree(rootNode, packageName)
        }

        // Detect if this is a shorts/reels screen
        val detector = AppDetectorRouter.getDetector(packageName)
        val result = detector.detect(rootNode)

        Log.d("REELSBREAK", "Detection result: $result for $packageName")

        if (result == DetectionResult.REELS_SCREEN) {
            session.reelsMode = true
            session.currentApp = packageName
            // Ask engine → act on decision
            handleReelsScreen()
        } else {
            session.reelsMode = false
            session.scrollCount = 0
        }
    }

    // ── Tree Dump Helper ──────────────────────────────────────────────────
    /**
     * Prints the entire accessibility view tree to Logcat.
     * Filter by tag "REELSBREAK_TREE" in Logcat.
     *
     * HOW TO READ the output:
     * Each line = one view node on screen
     * [com.instagram.android:id/some_view_name] → this is the view ID
     * text=  → visible text inside that view
     * desc=  → content description (accessibility label)
     *
     * WHAT TO LOOK FOR:
     * Open app normally (home feed) → note which IDs appear
     * Open Reels tab → note which NEW IDs appear
     * Those NEW IDs that only appear on Reels = your detection targets
     */
    private fun dumpTree(
        node: AccessibilityNodeInfo?,
        packageName: String,
        depth: Int = 0
    ) {
        if (node == null) return

        val indent = "  ".repeat(depth)
        val viewId = node.viewIdResourceName ?: "no-id"
        val text = node.text?.toString() ?: ""
        val desc = node.contentDescription?.toString() ?: ""

        // Only print nodes that have a view ID or visible text
        // (skips invisible/empty container nodes to reduce noise)
        if (viewId != "no-id" || text.isNotEmpty()) {
            Log.d(
                "REELSBREAK_TREE",
                "$indent [$viewId] text=$text desc=$desc"
            )
        }

        for (i in 0 until node.childCount) {
            dumpTree(node.getChild(i), packageName, depth + 1)
        }
    }


    /**
     * Called when a reels screen is confirmed.
     * Launches a coroutine to ask BlockingDecisionEngine what to do.
     */
    private fun handleReelsScreen() {
        scope.launch {
            when (engine.decide()) {
                BlockingDecisionEngine.Decision.BLOCK -> {
                    Log.d("REELSBREAK", "Decision: BLOCK → triggering action")
                    actionController.triggerBlock()
                }
                BlockingDecisionEngine.Decision.ALLOW -> {
                    Log.d("REELSBREAK", "Decision: ALLOW → user can watch")
                    // Future: increment counter here
                }
                BlockingDecisionEngine.Decision.SKIP_REEL -> {
                    Log.d("REELSBREAK", "Decision: SKIP_REEL → future curated mode")
                }
            }
        }
    }

    private fun resetSession() {
        session.reelsMode = false
        session.scrollCount = 0
    }
}