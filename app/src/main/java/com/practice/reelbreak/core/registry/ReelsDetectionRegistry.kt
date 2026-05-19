package com.practice.reelbreak.core.registry

object ReelsDetectionRegistry {
    const val YOUTUBE = "com.google.android.youtube"
    const val INSTAGRAM = "com.instagram.android"
    const val SNAPCHAT = "com.snapchat.android"

    private val detectionApps = setOf(
        YOUTUBE,
        INSTAGRAM,
        SNAPCHAT,
    )

    fun isDetectionSupported(packageName: String?): Boolean {
        return packageName != null && packageName in detectionApps
    }
}


