package com.practice.reelbreak.core.registry

object FocusModeAppsRegistry {
    const val YOUTUBE = "com.google.android.youtube"
    const val INSTAGRAM = "com.instagram.android"
    const val INSTAGRAM_LITE = "com.instagram.lite"
    const val FACEBOOK = "com.facebook.katana"
    const val FACEBOOK_LITE = "com.facebook.lite"
    const val SNAPCHAT = "com.snapchat.android"
    const val TIKTOK = "com.zhiliaoapp.musically"
    const val X = "com.twitter.android"
    const val SHARECHAT = "in.mohalla.sharechat"
    const val MOJ = "in.mohalla.video"

    private val focusBlockableApps = setOf(
        YOUTUBE,
        INSTAGRAM,
        INSTAGRAM_LITE,
        FACEBOOK,
        FACEBOOK_LITE,
        SNAPCHAT,
        TIKTOK,
        X,
        SHARECHAT,
        MOJ
    )

    fun isBlockableInFocusMode(packageName: String?): Boolean {
        return packageName != null && packageName in focusBlockableApps
    }
}