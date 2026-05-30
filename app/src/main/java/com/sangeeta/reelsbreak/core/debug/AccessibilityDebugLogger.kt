package com.practice.reelbreak.core.debug

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

object AccessibilityDebugLogger {

    private const val TAG = "RB_DEBUG"

    private val targetPackages = setOf(
        "com.google.android.youtube",
        "com.instagram.android",
        "com.facebook.katana",
        "com.facebook.lite",
        "com.snapchat.android",
        "com.zhiliaoapp.musically"
    )

    fun shouldLog(packageName: String?): Boolean {
        return packageName != null && targetPackages.contains(packageName)
    }

    fun logEvent(
        event: AccessibilityEvent,
        rootNodeRequested: Boolean,
        rootNode: AccessibilityNodeInfo?,
        detectorState: String? = null
    ) {
        val pkg = event.packageName?.toString()
        if (!shouldLog(pkg)) return

        val eventTypeName = eventTypeToString(event.eventType)

        val eventText = event.text
            ?.joinToString(" | ")
            ?.take(300)
            ?.replace("\n", " ")

        val contentDesc = event.contentDescription
            ?.toString()
            ?.take(200)
            ?.replace("\n", " ")

        val className = event.className?.toString()
        val paneTitle = try {
            event.javaClass.getMethod("getPaneTitle").invoke(event)?.toString()
        } catch (_: Exception) {
            null
        }

        Log.d(
            TAG,
            buildString {
                append("EVENT")
                append(" pkg=").append(pkg)
                append(" type=").append(eventTypeName)
                append(" class=").append(className)
                append(" contentDesc=").append(contentDesc ?: "null")
                append(" text=").append(eventText ?: "null")
                append(" rootRequested=").append(rootNodeRequested)
                append(" rootPresent=").append(rootNode != null)
                append(" detectorState=").append(detectorState ?: "null")
                if (!paneTitle.isNullOrBlank()) {
                    append(" paneTitle=").append(paneTitle.take(120))
                }
            }
        )

        if (rootNode != null) {
            logNodeSummary(rootNode)
        }
    }

    private fun logNodeSummary(root: AccessibilityNodeInfo) {
        val lines = mutableListOf<String>()
        collectInterestingNodes(root, 0, lines, maxLines = 25)

        Log.d(TAG, "NODE_SUMMARY start ----------------")
        lines.forEach { Log.d(TAG, it) }
        Log.d(TAG, "NODE_SUMMARY end ------------------")
    }

    private fun collectInterestingNodes(
        node: AccessibilityNodeInfo?,
        depth: Int,
        out: MutableList<String>,
        maxLines: Int
    ) {
        if (node == null || out.size >= maxLines || depth > 5) return

        val cls = node.className?.toString()
        val text = node.text?.toString()?.trim()
        val desc = node.contentDescription?.toString()?.trim()
        val viewId = try { node.viewIdResourceName } catch (_: Exception) { null }

        val isInteresting =
            !text.isNullOrBlank() ||
                    !desc.isNullOrBlank() ||
                    !viewId.isNullOrBlank()

        if (isInteresting) {
            out += buildString {
                append("depth=").append(depth)
                append(" class=").append(cls ?: "null")
                append(" text=").append(text?.take(120) ?: "null")
                append(" desc=").append(desc?.take(120) ?: "null")
                append(" viewId=").append(viewId ?: "null")
                append(" clickable=").append(node.isClickable)
                append(" scrollable=").append(node.isScrollable)
                append(" selected=").append(node.isSelected)
            }
        }

        for (i in 0 until node.childCount) {
            if (out.size >= maxLines) break
            collectInterestingNodes(node.getChild(i), depth + 1, out, maxLines)
        }
    }

    fun logDetectionDecision(
        packageName: String?,
        detectorName: String,
        phase: String,
        matched: Boolean,
        reason: String
    ) {
        if (!shouldLog(packageName)) return

        Log.d(
            TAG,
            "DETECTOR pkg=$packageName detector=$detectorName phase=$phase matched=$matched reason=$reason"
        )
    }

    fun logAction(
        packageName: String?,
        action: String,
        reason: String
    ) {
        if (!shouldLog(packageName)) return
        Log.d(TAG, "ACTION pkg=$packageName action=$action reason=$reason")
    }

    private fun eventTypeToString(type: Int): String {
        return when (type) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> "TYPE_WINDOW_STATE_CHANGED"
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> "TYPE_WINDOW_CONTENT_CHANGED"
            AccessibilityEvent.TYPE_VIEW_SCROLLED -> "TYPE_VIEW_SCROLLED"
            AccessibilityEvent.TYPE_VIEW_CLICKED -> "TYPE_VIEW_CLICKED"
            AccessibilityEvent.TYPE_VIEW_SELECTED -> "TYPE_VIEW_SELECTED"
            AccessibilityEvent.TYPE_WINDOWS_CHANGED -> "TYPE_WINDOWS_CHANGED"
            else -> "TYPE_$type"
        }
    }
}