package com.practice.reelbreak.core.action


import android.accessibilityservice.AccessibilityService

class ActionController(
    private val service: AccessibilityService
) {

    private val blockController = BlockController(service)

    fun triggerBlock() {

        blockController.closeCurrentApp()

    }
}