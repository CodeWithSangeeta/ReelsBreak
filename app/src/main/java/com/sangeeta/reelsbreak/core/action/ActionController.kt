//package com.practice.reelbreak.core.action
//
//import android.accessibilityservice.AccessibilityService
//class ActionController(
//    private val service: AccessibilityService
//) {
//    private val blockController = BlockController(service)
//    fun triggerReelsBlock(packageName: String? = null) {
//        blockController.goBackInsideApp()
//    }
//
//    fun triggerFullAppBlock(packageName: String? = null) {
//        blockController.goHome()
//    }
//    }



package com.practice.reelbreak.core.action

import android.accessibilityservice.AccessibilityService

class ActionController(
    private val service: AccessibilityService
) {
    private val blockController = BlockController(service)

    /**
     * Attempts to navigate backwards within the targeting social application
     * to safely break the mindless loop without kicking the user out of the app.
     */
    fun triggerReelsBlock(packageName: String? = null) {
        blockController.goBackInsideApp(packageName)
    }

    /**
     * Closes the entire application layout space completely by returning to the primary system launcher.
     */
    fun triggerFullAppBlock(packageName: String? = null) {
        blockController.goHome(packageName)
    }
}