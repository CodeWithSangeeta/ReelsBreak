package com.sangeeta.reelsbreak.core.action

import android.accessibilityservice.AccessibilityService

class ActionController(
    private val service: AccessibilityService
) {
    private val blockController = BlockController(service)

    fun triggerReelsBlock(packageName: String? = null) {
        blockController.goBackInsideApp(packageName)
    }
    fun triggerFullAppBlock(packageName: String? = null) {
        blockController.goHome(packageName)
    }
}