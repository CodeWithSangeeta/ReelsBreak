package com.practice.reelbreak.core.registry

object ReelsDetectionRegistry {
    const val YOUTUBE = "com.google.android.youtube"
     const val INSTAGRAM = "com.instagram.android"
    const val INSTAGRAM_LITE = "com.instagram.lite"

    const val SNAPCHAT = "com.snapchat.android"

    const val FACEBOOK = "com.facebook.katana"
    const val FACEBOOK_LITE = "com.facebook.lite"
    const val TIKTOK = "com.zhiliaoapp.musically"


    private val supportedApps = setOf(
        YOUTUBE,
        INSTAGRAM,
        INSTAGRAM_LITE,
        SNAPCHAT,
        FACEBOOK,
        FACEBOOK_LITE,
        TIKTOK,
    )

    fun isDetectionSupported(packageName: String?): Boolean {
        return packageName != null && packageName in supportedApps
    }
}