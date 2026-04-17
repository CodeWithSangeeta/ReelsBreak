package com.practice.reelbreak.core.detection.detectors

import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.practice.reelbreak.domain.model.DetectionResult

interface AppDetector {

    /**
     * Analyze the current UI tree and decide
     * whether this screen is a reels/shorts screen.
     */
    fun detect(rootNode: AccessibilityNodeInfo?): DetectionResult
    fun onEvent(event: AccessibilityEvent) {}
    fun reset() {}
}