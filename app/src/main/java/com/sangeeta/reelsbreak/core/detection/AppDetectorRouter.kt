package com.sangeeta.reelsbreak.core.detection

import android.view.accessibility.AccessibilityNodeInfo


object AppDetectorRouter {
        fun isBlockedContentVisible(packageName: String, rootNode: AccessibilityNodeInfo?): Boolean {
            return BlockableAppConfig.isBlockedContentVisible(packageName, rootNode)
        }

}