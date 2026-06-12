package com.sangeeta.reelsbreak.core.debug

import android.graphics.Rect
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import java.util.Locale

object AccessibilityDebugLogger {

    private const val TAG = "RB_DEBUG"

    private const val PKG_YOUTUBE = "com.google.android.youtube"
    private const val PKG_INSTAGRAM = "com.instagram.android"
    private const val PKG_FACEBOOK = "com.facebook.katana"
    private const val PKG_FACEBOOK_LITE = "com.facebook.lite"
    private const val PKG_INSTAGRAM_LITE = "com.instagram.lite"
    private const val PKG_SNAPCHAT = "com.snapchat.android"
    private const val PKG_TIKTOK = "com.zhiliaoapp.musically"

    private val targetPackages = setOf(
        PKG_YOUTUBE,
        PKG_INSTAGRAM,
        PKG_FACEBOOK,
        PKG_FACEBOOK_LITE,
        PKG_INSTAGRAM_LITE,
        PKG_SNAPCHAT,
        PKG_TIKTOK
    )

    private val facebookViewIdHints = listOf(
        "reels_video_view_pager",
        "reels_fullscreen_container",
        "reels_viewer_activity_container",
        "video_channel",
        "watch_feed",
        "fullscreen"
    )

    private val facebookTextHints = listOf(
        "view reel by",
        "tap to show video controls",
        "reels",
        "short video"
    )

    private val instagramLiteViewIdHints = listOf(
        "reel_viewer_fragment",
        "clips_viewer_fragment",
        "media_viewer",
        "reels_viewer",
        "video_viewer",
        "carbon_sound_image_view",
        "horizontalProgressV2"
    )

    private val instagramLiteTextHints = listOf(
        "reels",
        "watch more reels",
        "views",
        "liked by",
        "send message",
        "follow",
        "more"
    )

    private val youtubeViewIdHints = listOf(
        "reel_player_page_container",
        "shorts",
        "reel",
        "player",
        "pivot_bar",
        "watch"
    )

    private val youtubeTextHints = listOf(
        "shorts",
        "subscribe",
        "comments",
        "dislike",
        "like",
        "share"
    )

    private val snapchatViewIdHints = listOf(
        "spotlight_container",
        "vertical_view_pager",
        "spotlight",
        "viewer",
        "pager"
    )

    private val snapchatTextHints = listOf(
        "spotlight",
        "subscribe",
        "send a chat",
        "stories",
        "for you"
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

            when (pkg) {
                PKG_FACEBOOK -> logFocusedDetectorDump(
                    packageName = pkg,
                    root = rootNode,
                    viewIdHints = facebookViewIdHints,
                    textHints = facebookTextHints
                )

                PKG_INSTAGRAM_LITE -> logFocusedDetectorDump(
                    packageName = pkg,
                    root = rootNode,
                    viewIdHints = instagramLiteViewIdHints,
                    textHints = instagramLiteTextHints
                )
            }
        }
    }

    fun logDetectionSnapshot(
        packageName: String?,
        rootNode: AccessibilityNodeInfo?,
        isDetected: Boolean
    ) {
        if (!shouldLog(packageName) || rootNode == null || packageName == null) return

        when (packageName) {
            PKG_YOUTUBE -> {
                Log.d(
                    TAG,
                    "SNAPSHOT pkg=$packageName detected=$isDetected mode=${if (isDetected) "STATE_A_OR_MATCH" else "STATE_B_OR_MISS"}"
                )

                logFocusedDetectorDump(
                    packageName = packageName,
                    root = rootNode,
                    viewIdHints = youtubeViewIdHints,
                    textHints = youtubeTextHints
                )

                if (!isDetected) {
                    logDeepNodeTree(
                        packageName = packageName,
                        root = rootNode,
                        maxDepth = 8,
                        maxLines = 120
                    )
                }
            }

            PKG_SNAPCHAT -> {
                Log.d(
                    TAG,
                    "SNAPSHOT pkg=$packageName detected=$isDetected mode=${if (isDetected) "STATE_A_OR_MATCH" else "STATE_B_OR_MISS"}"
                )

                logFocusedDetectorDump(
                    packageName = packageName,
                    root = rootNode,
                    viewIdHints = snapchatViewIdHints,
                    textHints = snapchatTextHints
                )

                if (!isDetected) {
                    logDeepNodeTree(
                        packageName = packageName,
                        root = rootNode,
                        maxDepth = 8,
                        maxLines = 120
                    )
                }
            }
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
        val viewId = safeViewId(node)

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

    private fun logFocusedDetectorDump(
        packageName: String,
        root: AccessibilityNodeInfo,
        viewIdHints: List<String>,
        textHints: List<String>
    ) {
        val matches = mutableListOf<String>()
        collectDetectorMatches(
            node = root,
            depth = 0,
            out = matches,
            maxLines = 40,
            viewIdHints = viewIdHints,
            textHints = textHints
        )

        Log.d(TAG, "DETECTOR_DUMP start pkg=$packageName ----------------")
        if (matches.isEmpty()) {
            Log.d(TAG, "DETECTOR_DUMP pkg=$packageName no_match")
        } else {
            matches.forEach { Log.d(TAG, it) }
        }
        Log.d(TAG, "DETECTOR_DUMP end pkg=$packageName ------------------")
    }

    private fun collectDetectorMatches(
        node: AccessibilityNodeInfo?,
        depth: Int,
        out: MutableList<String>,
        maxLines: Int,
        viewIdHints: List<String>,
        textHints: List<String>
    ) {
        if (node == null || out.size >= maxLines || depth > 7) return

        val viewId = safeViewId(node)
        val text = node.text?.toString()?.trim()
        val desc = node.contentDescription?.toString()?.trim()
        val cls = node.className?.toString()
        val haystack = listOfNotNull(text, desc, viewId)
            .joinToString(" | ")
            .lowercase(Locale.ROOT)

        val viewIdHit = viewIdHints.firstOrNull { hint ->
            viewId?.lowercase(Locale.ROOT)?.contains(hint.lowercase(Locale.ROOT)) == true
        }

        val textHit = textHints.firstOrNull { hint ->
            haystack.contains(hint.lowercase(Locale.ROOT))
        }

        if (viewIdHit != null || textHit != null) {
            val rect = Rect().also { node.getBoundsInScreen(it) }
            out += buildString {
                append("matchDepth=").append(depth)
                append(" class=").append(cls ?: "null")
                append(" viewId=").append(viewId ?: "null")
                append(" text=").append(text?.take(120) ?: "null")
                append(" desc=").append(desc?.take(120) ?: "null")
                append(" clickable=").append(node.isClickable)
                append(" scrollable=").append(node.isScrollable)
                append(" selected=").append(node.isSelected)
                append(" visible=").append(node.isVisibleToUser)
                append(" bounds=").append(rect.flattenToString())
                append(" viewIdHit=").append(viewIdHit ?: "null")
                append(" textHit=").append(textHit ?: "null")
            }

            logNearbyChildren(node, out, maxLines)
        }

        for (i in 0 until node.childCount) {
            if (out.size >= maxLines) break
            collectDetectorMatches(
                node = node.getChild(i),
                depth = depth + 1,
                out = out,
                maxLines = maxLines,
                viewIdHints = viewIdHints,
                textHints = textHints
            )
        }
    }

    private fun logNearbyChildren(
        node: AccessibilityNodeInfo,
        out: MutableList<String>,
        maxLines: Int
    ) {
        for (i in 0 until node.childCount) {
            if (out.size >= maxLines) break
            val child = node.getChild(i) ?: continue
            out += buildCompactNodeLine(prefix = "  child[$i]", node = child)
        }
    }

    private fun buildCompactNodeLine(prefix: String, node: AccessibilityNodeInfo): String {
        val rect = Rect().also { node.getBoundsInScreen(it) }
        return buildString {
            append(prefix)
            append(" class=").append(node.className?.toString() ?: "null")
            append(" viewId=").append(safeViewId(node) ?: "null")
            append(" text=").append(node.text?.toString()?.trim()?.take(80) ?: "null")
            append(" desc=").append(node.contentDescription?.toString()?.trim()?.take(80) ?: "null")
            append(" clickable=").append(node.isClickable)
            append(" scrollable=").append(node.isScrollable)
            append(" selected=").append(node.isSelected)
            append(" visible=").append(node.isVisibleToUser)
            append(" bounds=").append(rect.flattenToString())
        }
    }

    private fun logDeepNodeTree(
        packageName: String,
        root: AccessibilityNodeInfo,
        maxDepth: Int,
        maxLines: Int
    ) {
        val lines = mutableListOf<String>()
        collectAllNodes(
            node = root,
            depth = 0,
            out = lines,
            maxDepth = maxDepth,
            maxLines = maxLines
        )

        Log.d(TAG, "FULLTREE start pkg=$packageName ----------------")
        lines.forEach { Log.d(TAG, it) }
        Log.d(TAG, "FULLTREE end pkg=$packageName ------------------")
    }

    private fun collectAllNodes(
        node: AccessibilityNodeInfo?,
        depth: Int,
        out: MutableList<String>,
        maxDepth: Int,
        maxLines: Int
    ) {
        if (node == null || depth > maxDepth || out.size >= maxLines) return

        val rect = Rect().also { node.getBoundsInScreen(it) }

        out += buildString {
            append("depth=").append(depth)
            append(" class=").append(node.className?.toString() ?: "null")
            append(" viewId=").append(safeViewId(node) ?: "null")
            append(" text=").append(node.text?.toString()?.trim()?.take(120) ?: "null")
            append(" desc=").append(node.contentDescription?.toString()?.trim()?.take(120) ?: "null")
            append(" clickable=").append(node.isClickable)
            append(" scrollable=").append(node.isScrollable)
            append(" selected=").append(node.isSelected)
            append(" visible=").append(node.isVisibleToUser)
            append(" bounds=").append(rect.flattenToString())
        }

        for (i in 0 until node.childCount) {
            if (out.size >= maxLines) break
            collectAllNodes(
                node = node.getChild(i),
                depth = depth + 1,
                out = out,
                maxDepth = maxDepth,
                maxLines = maxLines
            )
        }
    }

    private fun safeViewId(node: AccessibilityNodeInfo): String? {
        return try {
            node.viewIdResourceName
        } catch (_: Exception) {
            null
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