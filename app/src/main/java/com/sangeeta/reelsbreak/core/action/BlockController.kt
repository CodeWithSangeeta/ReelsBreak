package com.sangeeta.reelsbreak.core.action

import android.accessibilityservice.AccessibilityService

class BlockController(
    private val service: AccessibilityService
) {
    fun goBackInsideApp(expectedPackage: String?) {
        val root = service.rootInActiveWindow
        val activePackage = root?.packageName?.toString()

        if (expectedPackage != null && activePackage != expectedPackage) {
            root?.recycle()
            return
        }
        root?.recycle()

    }

    fun goHome(expectedPackage: String?) {
        val root = service.rootInActiveWindow
        val activePackage = root?.packageName?.toString()
        root?.recycle()
    }
}