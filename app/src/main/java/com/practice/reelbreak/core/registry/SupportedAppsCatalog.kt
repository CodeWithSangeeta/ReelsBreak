package com.practice.reelbreak.core.registry

import androidx.annotation.DrawableRes
import com.practice.reelbreak.R

data class SupportedAppInfo(
    val packageName: String,
    val displayName: String,
    @DrawableRes val iconRes: Int,
    val availableInFocusMode: Boolean = true
)

object SupportedAppsCatalog {

    val apps = listOf(
        SupportedAppInfo(
            packageName = SupportedAppsRegistry.INSTAGRAM,
            displayName = "Instagram",
            iconRes = R.drawable.ic_instagram
        ),
        SupportedAppInfo(
            packageName = SupportedAppsRegistry.YOUTUBE,
            displayName = "YouTube",
            iconRes = R.drawable.ic_youtube
        ),
        SupportedAppInfo(
            packageName = SupportedAppsRegistry.TIKTOK,
            displayName = "TikTok",
            iconRes = R.drawable.ic_tiktok
        ),
        SupportedAppInfo(
            packageName = SupportedAppsRegistry.FACEBOOK,
            displayName = "Facebook",
            iconRes = R.drawable.ic_facebook
        ),
        SupportedAppInfo(
            packageName = SupportedAppsRegistry.SNAPCHAT,
            displayName = "Snapchat",
            iconRes = R.drawable.ic_snapchat
        ),
        SupportedAppInfo(
            packageName = SupportedAppsRegistry.TWITTER,
            displayName = "Twitter",
            iconRes = R.drawable.ic_twitter
        )
    )

    fun findByPackage(packageName: String): SupportedAppInfo? {
        return apps.find { it.packageName == packageName }
    }

    fun displayNameOf(packageName: String): String {
        return findByPackage(packageName)?.displayName
            ?: packageName.substringAfterLast(".")
                .replaceFirstChar { it.uppercase() }
    }

    fun iconOf(packageName: String): Int? {
        return findByPackage(packageName)?.iconRes
    }
}