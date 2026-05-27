package com.practice.reelbreak.core.registry

object ReelsDetectionRegistry {
    const val YOUTUBE = "com.google.android.youtube"
    const val YOUTUBE_KIDS = "com.google.android.apps.youtube.kids"
    const val YOUTUBE_REVANCED = "app.revanced.android.youtube"

    const val INSTAGRAM = "com.instagram.android"
    const val INSTAGRAM_LITE = "com.instagram.lite"

    const val SNAPCHAT = "com.snapchat.android"

    const val FACEBOOK = "com.facebook.katana"
    const val FACEBOOK_LITE = "com.facebook.lite"

    const val TIKTOK = "com.zhiliaoapp.musically"
    const val TIKTOK_TRILL = "com.ss.android.ugc.trill"
    const val TIKTOK_AWEME = "com.ss.android.ugc.aweme"
    const val TIKTOK_GO = "com.zhiliaoapp.musically.go"

    private val supportedApps = setOf(
        YOUTUBE,
        YOUTUBE_KIDS,
        YOUTUBE_REVANCED,
        INSTAGRAM,
        INSTAGRAM_LITE,
        SNAPCHAT,
        FACEBOOK,
        FACEBOOK_LITE,
        TIKTOK,
        TIKTOK_TRILL,
        TIKTOK_AWEME,
        TIKTOK_GO
    )

    fun isDetectionSupported(packageName: String?): Boolean {
        return packageName != null && packageName in supportedApps
    }
}