package com.practice.reelbreak.core.registry

import androidx.annotation.DrawableRes
import com.practice.reelbreak.R

data class FocusModeSupportAppInfo(
    val packageName: String,
    val displayName: String,
    @DrawableRes val iconRes: Int,
    val availableInFocusMode: Boolean = true
)

object FocusModeSupportedAppsCatalog {

    val apps = listOf(
        FocusModeSupportAppInfo(
            packageName = FocusModeAppsRegistry.YOUTUBE,
            displayName = "YouTube",
            iconRes = R.drawable.ic_youtube
        ),
        FocusModeSupportAppInfo(
            packageName = FocusModeAppsRegistry.INSTAGRAM,
            displayName = "Instagram",
            iconRes = R.drawable.ic_instagram
        ),
        FocusModeSupportAppInfo(
            packageName = FocusModeAppsRegistry.INSTAGRAM_LITE,
            displayName = "Instagram lite",
            iconRes = R.drawable.ic_instagram
        ),

        FocusModeSupportAppInfo(
            packageName = FocusModeAppsRegistry.FACEBOOK,
            displayName = "Facebook",
            iconRes = R.drawable.ic_facebook
        ),
        FocusModeSupportAppInfo(
            packageName = FocusModeAppsRegistry.FACEBOOK_LITE,
            displayName = "Facebook lite",
            iconRes = R.drawable.ic_facebook
        ),
        FocusModeSupportAppInfo(
            packageName = FocusModeAppsRegistry.SNAPCHAT,
            displayName = "Snapchat",
            iconRes = R.drawable.ic_snapchat
        ),
        FocusModeSupportAppInfo(
            packageName = FocusModeAppsRegistry.TIKTOK,
            displayName = "TikTok",
            iconRes = R.drawable.ic_tiktok
        ),
        FocusModeSupportAppInfo(
            packageName = FocusModeAppsRegistry.X,
            displayName = "X",
            iconRes = R.drawable.ic_twitter
        ),
        FocusModeSupportAppInfo(
            packageName = FocusModeAppsRegistry.SHARECHAT,
            displayName = "ShareChat",
            iconRes = R.drawable.ic_twitter
        ),
    FocusModeSupportAppInfo(
            packageName = FocusModeAppsRegistry.MOJ,
            displayName = "Moj",
            iconRes = R.drawable.ic_twitter
        )
    )

    fun findByPackage(packageName: String): FocusModeSupportAppInfo? {
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