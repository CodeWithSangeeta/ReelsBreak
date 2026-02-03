package com.practice.reelbreak.core.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.util.Log

class ReelsAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d("ReelsAccessibility", "Service connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Empty for now — logic will come later
    }

    override fun onInterrupt() {
        // Required override
    }
}
