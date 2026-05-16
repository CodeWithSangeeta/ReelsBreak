package com.practice.reelbreak.core.registry

object SupportedAppsRegistry {

    const val YOUTUBE = "com.google.android.youtube"
    const val INSTAGRAM = "com.instagram.android"
    const val FACEBOOK = "com.facebook.katana"
    const val FACEBOOK_LITE = "com.facebook.lite"
    const val SNAPCHAT = "com.snapchat.android"
    const val TIKTOK = "com.zhiliaoapp.musically"
    const val TWITTER = "com.twitter.android"

    private val supportedApps = setOf(
        YOUTUBE,
        INSTAGRAM,
        FACEBOOK,
        FACEBOOK_LITE,
        SNAPCHAT,
        TIKTOK,
        TWITTER
    )

    fun isSupported(packageName: String?): Boolean {
        return packageName != null && packageName in supportedApps
    }
}


