package com.practice.reelbreak.core.accessibility

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.practice.reelbreak.ReelBreakApplication
import com.practice.reelbreak.core.action.ActionController
import com.practice.reelbreak.core.detection.ReelsDetectionManager
import com.practice.reelbreak.core.engine.BlockingDecisionEngine

class ReelsAccessibilityService : AccessibilityService() {

    private lateinit var detectionManager: ReelsDetectionManager

    override fun onServiceConnected() {
        super.onServiceConnected()

        // Get the singleton repository from Application class
        val repository = (applicationContext as ReelBreakApplication).repository

        // Build the chain: Engine → DetectionManager
        val engine = BlockingDecisionEngine(repository)
        val actionController = ActionController(this)
        detectionManager = ReelsDetectionManager(actionController, engine)

        Log.d("REELSBREAK", "Service connected ✅")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        val rootNode: AccessibilityNodeInfo? = rootInActiveWindow
        detectionManager.processEvent(event, rootNode)
    }

    override fun onInterrupt() {
        Log.d("REELSBREAK", "Service interrupted")
    }
}