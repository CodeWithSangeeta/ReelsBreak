package com.practice.reelbreak.core.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.util.Log

class ReelsAccessibilityService : AccessibilityService() {

    private var scrollCount = 0
    private var lastPackage: String? = null
    private var lastScrollTime = 0L

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.e("ReelsService", "SERVICE CONNECTED SUCCESSFULLY")
    }


    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        val packageName = event.packageName?.toString() ?: return
        // Ignore apps that are not reels apps
        if (!SupportedApps.reelApps.contains(packageName)) return

        when (event.eventType) {

            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                if (packageName != lastPackage) {
                    lastPackage = packageName
                    Log.d("ReelsService", "Entered app: $packageName")
                }
            }

            AccessibilityEvent.TYPE_VIEW_SCROLLED -> {

                val now = System.currentTimeMillis()

                if (now - lastScrollTime < 600) {
                    scrollCount++
                } else {
                    scrollCount = 1
                }

                lastScrollTime = now

                Log.d("ReelsService", "Scroll burst count = $scrollCount in $packageName")

                if (scrollCount >= 2) {
                    Log.d("ReelsService", "Reel swipe detected in $packageName")
                    scrollCount = 0
                }

//                val currentTime = System.currentTimeMillis()
//
//                // Ignore fake scroll spam (cooldown)
//                if (currentTime - lastScrollTime < 250) {
//                    return
//                }
//
//                lastScrollTime = currentTime
//                Log.d("ReelsService", "REAL Reel scroll detected in $packageName")
            }
        }
    }

    override fun onInterrupt() {
        Log.d("ReelsService", "Accessibility Service Interrupted")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ReelsService", "Accessibility Service Destroyed")
    }
}