package com.practice.reelbreak.core.accessibility

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.practice.reelbreak.core.action.ActionController
import com.practice.reelbreak.core.detection.ReelsDetectionManager

class ReelsAccessibilityService : AccessibilityService() {

    //temporary debugging 1
    private fun dumpNode(node: AccessibilityNodeInfo?, depth: Int = 0) {
        if (node == null) return

        val indent = " ".repeat(depth * 2)
        val className = node.className
        val text = node.text
        val viewId = node.viewIdResourceName

        android.util.Log.d(
            "REELSBREAK_TREE",
            "$indent Class=$className Text=$text ViewId=$viewId"
        )

        for (i in 0 until node.childCount) {
            dumpNode(node.getChild(i), depth + 1)
        }
    }


    private lateinit var detectionManager: ReelsDetectionManager

    override fun onServiceConnected() {
        super.onServiceConnected()

        val actionController = ActionController(this)

        detectionManager = ReelsDetectionManager(
            actionController
        )
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        if (event == null) return
        Log.d("REELSBREAK", "Event received: ${event.eventType} from ${event.packageName}")

        val rootNode: AccessibilityNodeInfo? = rootInActiveWindow

        //temporary debugging 2
        if (event.packageName == "com.snapchat.android") {
            dumpNode(rootNode)
        }

        detectionManager.processEvent(
            event = event,
            rootNode = rootNode
        )


    }

    override fun onInterrupt() {
        // Required override — called when accessibility service is interrupted
    }
}