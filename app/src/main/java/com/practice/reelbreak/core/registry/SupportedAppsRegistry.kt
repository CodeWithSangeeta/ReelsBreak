package com.practice.reelbreak.core.registry

object SupportedAppsRegistry {

    const val YOUTUBE = "com.google.android.youtube"
    const val INSTAGRAM = "com.instagram.android"
    const val SNAPCHAT = "com.snapchat.android"
    const val TIKTOK = "com.zhiliaoapp.musically"
    const val FACEBOOK = "com.facebook.katana"

    private val supportedApps = setOf(
        YOUTUBE,
        INSTAGRAM,
        SNAPCHAT,
        TIKTOK,
        FACEBOOK
    )

    fun isSupported(packageName: String?): Boolean {
        if (packageName == null) return false
        return supportedApps.contains(packageName)
    }
}