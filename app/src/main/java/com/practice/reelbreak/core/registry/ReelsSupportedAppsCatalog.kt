package com.practice.reelbreak.core.registry

import androidx.annotation.DrawableRes
import com.practice.reelbreak.R

data class ReelsSupportAppInfo(
    val packageName: String,
    val displayName: String,
    @DrawableRes val iconRes: Int,
)
object ReelsSupportedAppsCatalog {

    val apps = listOf(
        ReelsSupportAppInfo(
            packageName = ReelsDetectionRegistry.YOUTUBE,
            displayName = "YouTube",
            iconRes = R.drawable.ic_youtube
        ),
        ReelsSupportAppInfo(
            packageName = ReelsDetectionRegistry.INSTAGRAM,
            displayName = "Instagram",
            iconRes = R.drawable.ic_instagram
        ),
        ReelsSupportAppInfo(
            packageName = ReelsDetectionRegistry.SNAPCHAT,
            displayName = "Snapchat",
            iconRes = R.drawable.ic_snapchat
        )
    )

    fun findByPackage(packageName: String): ReelsSupportAppInfo? {
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