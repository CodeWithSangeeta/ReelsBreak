//package com.practice.reelbreak.core.detection
//
//
//import android.graphics.Rect
//import android.view.accessibility.AccessibilityNodeInfo
//
///**
// * Config-driven map of each supported app to its detection method.
// * View IDs verified from Scrolless open-source project (production-stable).
// *
// * How it works:
// *  - YouTube Shorts  → "reel_player_page_container"   present only inside Shorts player
// *  - Instagram Reels → "clips_viewer_view_pager"       present only when Reels video is loaded
// *  - Snapchat        → "spotlight_container"           present only inside Spotlight feed
// */
//object BlockableAppConfig {
//
//    private val DETECTION_MAP: Map<String, DetectionMethod> = mapOf(
//        "com.google.android.youtube"  to DetectionMethod.ViewId("reel_player_page_container"),
//        "com.instagram.android"       to DetectionMethod.ViewId("clips_viewer_view_pager"),
//        "com.snapchat.android"        to DetectionMethod.ViewId("spotlight_container"),
//        // Instagram Lite — same Reels view as main Instagram
//        "com.instagram.lite"          to DetectionMethod.ViewId("clips_viewer_view_pager"),
//
//        // Facebook Lite — simple video player view
//        "com.facebook.lite"           to DetectionMethod.ViewId("videoview"),
//
//        // TikTok — main feed player
//        "com.zhiliaoapp.musically"    to DetectionMethod.ViewId("playerView"),
//
//        // TikTok alternate package variants (same detection)
//        "com.ss.android.ugc.trill"    to DetectionMethod.ViewId("playerView"),
//        "com.ss.android.ugc.aweme"    to DetectionMethod.ViewId("playerView"),
//
//        // Facebook — needs AnyOf because Reels can be opened two ways:
//        // 1. From main feed (has Sticker/GIF content descriptions in composer)
//        // 2. From Reels tab (nav label starts with "Reels, tab")
//        "com.facebook.katana"         to DetectionMethod.AnyOf(
//            listOf(
//                DetectionMethod.ContentDescriptions(
//                    setOf(
//                        "FbShortsComposerAttachmentComponentSpec:STICKER",
//                        "FbShortsComposerAttachmentComponentSpec:GIF"
//                    )
//                ),
//                DetectionMethod.ContentDescriptions(
//                    setOf("Reels, tab")
//                )
//            )
//        )
//    )
//
//    /**
//     * Returns true if the rootNode contains the blocked content view for the given package.
//     * Returns false immediately if rootNode is null or package is not in our config.
//     */
//    fun isBlockedContentVisible(packageName: String, rootNode: AccessibilityNodeInfo?): Boolean {
//        if (rootNode == null) return false
//        val method = DETECTION_MAP[packageName] ?: return false
//        return rootNode.matchesDetectionMethod(packageName, method)
//    }
//
//    private fun AccessibilityNodeInfo.matchesDetectionMethod(
//        packageName: String,
//        method: DetectionMethod
//    ): Boolean = when (method) {
//        is DetectionMethod.ViewId -> {
//            val fullId = "$packageName:id/${method.viewId}"
//            val nodes = findAccessibilityNodeInfosByViewId(fullId)
//            val found = nodes.any { isNodeVisibleToUser(it) }
//            nodes.forEach { it.recycle() }   // always recycle to prevent memory leaks
//            found
//        }
//        is DetectionMethod.ContentDescriptions -> {
//            hasVisibleNodeMatching { node ->
//                node.contentDescription?.toString() in method.contentDescriptions
//            }
//        }
//        is DetectionMethod.AnyOf -> {
//            method.detectionMethods.any { matchesDetectionMethod(packageName, it) }
//        }
//    }
//
//    /**
//     * Walks the visible accessibility tree and returns true
//     * as soon as a node satisfies the predicate.
//     */
//    private fun AccessibilityNodeInfo.hasVisibleNodeMatching(
//        predicate: (AccessibilityNodeInfo) -> Boolean
//    ): Boolean {
//        val queue = ArrayDeque<AccessibilityNodeInfo>()
//        queue.add(this)
//        while (queue.isNotEmpty()) {
//            val node = queue.removeFirst()
//            if (isNodeVisibleToUser(node) && predicate(node)) return true
//            for (i in 0 until node.childCount) {
//                node.getChild(i)?.let { queue.addLast(it) }
//            }
//        }
//        return false
//    }
//
//    /**
//     * A node counts as visible only if Android marks it visible
//     * AND it has non-zero bounds on screen (rules out off-screen layout passes).
//     */
//    private fun isNodeVisibleToUser(node: AccessibilityNodeInfo): Boolean {
//        if (!node.isVisibleToUser) return false
//        val rect = Rect()
//        node.getBoundsInScreen(rect)
//        return rect.width() > 0 && rect.height() > 0
//    }
//}




package com.practice.reelbreak.core.detection

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo

object BlockableAppConfig {

    private val DETECTION_MAP: Map<String, DetectionMethod> = mapOf(
        // YouTube Shorts
        "com.google.android.youtube" to DetectionMethod.ViewId("reel_player_page_container"),
        "com.google.android.apps.youtube.kids" to DetectionMethod.ViewId("reel_player_page_container"),
        "app.revanced.android.youtube" to DetectionMethod.ViewId("reel_player_page_container"),

        // Instagram Reels
        "com.instagram.android" to DetectionMethod.ViewId("clips_viewer_view_pager"),

        // Instagram Lite
      //  "com.instagram.lite" to DetectionMethod.ViewId("clips_viewer_view_pager"),
        "com.instagram.lite" to DetectionMethod.ViewId("horizontalProgressV2"),
        // Snapchat Spotlight
        "com.snapchat.android" to DetectionMethod.ViewId("spotlight_container"),

        // TikTok
        "com.zhiliaoapp.musically" to DetectionMethod.ViewId("playerView"),
        "com.ss.android.ugc.trill" to DetectionMethod.ViewId("playerView"),
        "com.ss.android.ugc.aweme" to DetectionMethod.ViewId("playerView"),
        "com.zhiliaoapp.musically.go" to DetectionMethod.ViewId("playerView"),

        // Facebook
        "com.facebook.katana" to DetectionMethod.AnyOf(
            listOf(
                DetectionMethod.ContentDescriptions(
                    setOf(
                        "FbShortsComposerAttachmentComponentSpec:STICKER",
                        "FbShortsComposerAttachmentComponentSpec:GIF"
                    )
                ),
                DetectionMethod.ContentDescriptionPrefix(
                    prefixes = setOf("Reels, tab"),
                    requireSelected = true,
                    maxTopScreenFraction = 0.2f
                )
            )
        ),

        // Facebook Lite
      //  "com.facebook.lite" to DetectionMethod.ViewId("videoview")

        "com.facebook.lite" to DetectionMethod.ContentDescriptionPrefix(
            prefixes = setOf("reels"),
            requireSelected = false,
            maxTopScreenFraction = 0.2f
        )
    )

    fun isBlockedContentVisible(packageName: String, rootNode: AccessibilityNodeInfo?): Boolean {
        if (rootNode == null) return false
        val method = DETECTION_MAP[packageName] ?: return false

        // 🔍 TEMP DEBUG — remove after finding view IDs
        dumpAllViewIds(packageName, rootNode)

        return rootNode.matchesDetectionMethod(packageName, method)
    }


    // 🔍 TEMP DEBUG FUNCTION — remove after testing
    private fun dumpAllViewIds(packageName: String, rootNode: AccessibilityNodeInfo) {
        val tag = "RB_VIEWID_DUMP"
        android.util.Log.i(tag, "=== VIEW ID DUMP for $packageName ===")
        collectAllNodes(rootNode).forEach { node ->
            val viewId = node.viewIdResourceName
            val desc = node.contentDescription?.toString()?.take(80)
            val text = node.text?.toString()?.take(80)
            val visible = node.isVisibleToUser
            val selected = node.isSelected
            if (viewId != null || desc != null) {
                android.util.Log.i(
                    tag,
                    "viewId=$viewId | desc=$desc | text=$text | visible=$visible | selected=$selected"
                )
            }
        }
        android.util.Log.i(tag, "=== END DUMP for $packageName ===")
    }

    private fun collectAllNodes(root: AccessibilityNodeInfo): List<AccessibilityNodeInfo> {
        val result = mutableListOf<AccessibilityNodeInfo>()
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)
        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            result.add(node)
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }
        }
        return result
    }






    private fun AccessibilityNodeInfo.matchesDetectionMethod(
        packageName: String,
        method: DetectionMethod
    ): Boolean {
        return when (method) {
            is DetectionMethod.ViewId -> {
                val fullViewId = "$packageName:id/${method.viewId}"
                val nodes = findAccessibilityNodeInfosByViewId(fullViewId)
                val found = nodes.any { isNodeVisibleToTheUser(it) }
                nodes.forEach { it.recycle() }
                found
            }

            is DetectionMethod.ContentDescriptions -> {
                hasVisibleContentDescription(method.contentDescriptions)
            }

            is DetectionMethod.ContentDescriptionPrefix -> {
                hasVisibleContentDescriptionPrefix(method)
            }

            is DetectionMethod.AnyOf -> {
                method.detectionMethods.any { matchesDetectionMethod(packageName, it) }
            }
        }
    }

    private fun AccessibilityNodeInfo.hasVisibleContentDescription(
        contentDescriptions: Set<String>
    ): Boolean {
        return hasVisibleNodeMatching { node ->
            node.contentDescription?.toString() in contentDescriptions
        }
    }

    private fun AccessibilityNodeInfo.hasVisibleContentDescriptionPrefix(
        detectionMethod: DetectionMethod.ContentDescriptionPrefix
    ): Boolean {
        val rootBounds = Rect().also { getBoundsInScreen(it) }

        val maxTop = detectionMethod.maxTopScreenFraction?.let { fraction ->
            val clampedFraction = fraction.coerceIn(0f, 1f)
            rootBounds.top + (rootBounds.height() * clampedFraction).toInt()
        }

        return hasVisibleNodeMatching { node ->
            val contentDescription = node.contentDescription?.toString() ?: return@hasVisibleNodeMatching false

            val matchesPrefix = detectionMethod.prefixes.any { prefix ->
                contentDescription.startsWith(prefix)
            }
            if (!matchesPrefix) return@hasVisibleNodeMatching false

            val nodeBounds = Rect().also { node.getBoundsInScreen(it) }
            val matchesSelectedState = !detectionMethod.requireSelected || node.isSelected
            val matchesTopConstraint = maxTop == null || nodeBounds.bottom <= maxTop

            matchesSelectedState && matchesTopConstraint
        }
    }

    private fun AccessibilityNodeInfo.hasVisibleNodeMatching(
        matchesNode: (AccessibilityNodeInfo) -> Boolean
    ): Boolean {
        val nodesToVisit = ArrayDeque<AccessibilityNodeInfo>()
        nodesToVisit.add(this)

        while (nodesToVisit.isNotEmpty()) {
            val node = nodesToVisit.removeFirst()

            if (isNodeVisibleToTheUser(node) && matchesNode(node)) {
                return true
            }

            for (index in 0 until node.childCount) {
                node.getChild(index)?.let(nodesToVisit::addLast)
            }
        }
        return false
    }

    private fun isNodeVisibleToTheUser(node: AccessibilityNodeInfo): Boolean {
        val rect = Rect()
        node.getBoundsInScreen(rect)
        return node.isVisibleToUser && rect.width() > 0 && rect.height() > 0
    }
}