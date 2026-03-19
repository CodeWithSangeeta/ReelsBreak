package com.practice.reelbreak.core.detection


import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.practice.reelbreak.core.action.ActionController
import com.practice.reelbreak.core.registry.SupportedAppsRegistry
import com.practice.reelbreak.domain.model.DetectionResult
import com.practice.reelbreak.domain.model.ReelsSession
import com.practice.reelbreak.domain.model.UserSettings

class ReelsDetectionManager(
    private val actionController: ActionController
){

    private val session = ReelsSession()

    private val settings = UserSettings()

    private val MIN_SCROLL_INTERVAL = 1200L

    fun processEvent(event: AccessibilityEvent, rootNode: AccessibilityNodeInfo?) {

        val packageName = event.packageName?.toString() ?: return
        Log.d("REELSBREAK", "Current App: $packageName")
        // Ignore unsupported apps
        if (!SupportedAppsRegistry.isSupported(packageName)) {
            Log.d("REELSBREAK", "App not supported")
            resetSession()
            return
        }
        Log.d("REELSBREAK", "Running detector")
        // Detect screen type
        val detector = AppDetectorRouter.getDetector(packageName)
        val result = detector.detect(rootNode)
        Log.d("REELSBREAK", "Detection result: $result")

        if (result == DetectionResult.REELS_SCREEN) {
            session.reelsMode = true
            session.currentApp = packageName
        }
//        else {
//            session.reelsMode = false
//            session.scrollCount = 0
//            return
//        }

        if (session.currentApp != packageName) {
            session.currentApp = packageName
            session.reelsMode = false
        }

        if (session.reelsMode) {
            if (
                event.eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED ||
                event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED ||
                event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
            ) {
                handleScroll(rootNode)
            }

        }
    }

    private fun handleScroll(rootNode: AccessibilityNodeInfo?) {

        Log.d("REELSBREAK", "handleScroll() triggered")

        val reelNode = rootNode?.findAccessibilityNodeInfosByViewId(
            "com.google.android.youtube:id/reel_player_page_container"
        )?.firstOrNull()

        val currentHash = reelNode?.hashCode() ?: return

        // Detect new reel
        if (currentHash == session.lastReelHash) {
            Log.d("REELSBREAK", "Same reel → ignore")
            return
        }

        session.lastReelHash = currentHash

        val currentTime = System.currentTimeMillis()

        if (currentTime - session.lastScrollTime < MIN_SCROLL_INTERVAL) {
            Log.d("REELSBREAK", "Ignored due to scroll interval")
            return
        }

        session.lastScrollTime = currentTime

        session.scrollCount++
        Log.d("REELSBREAK", "Scroll count: ${session.scrollCount}")
        checkRules()
    }

    private fun checkRules() {
        Log.d("REELSBREAK", "Checking rules: ${session.scrollCount}")
        if (!settings.breakModeEnabled) return

        if (session.scrollCount >= settings.scrollLimit) {
            Log.d("REELSBREAK", "LIMIT REACHED → TRIGGER BLOCK")
            triggerBlock()

            session.lastBlockTime = System.currentTimeMillis()

            session.scrollCount = 0
        }
    }

    private fun triggerBlock() {
        actionController.triggerBlock()
    }

    private fun resetSession() {
        session.reelsMode = false
        session.scrollCount = 0
    }
}