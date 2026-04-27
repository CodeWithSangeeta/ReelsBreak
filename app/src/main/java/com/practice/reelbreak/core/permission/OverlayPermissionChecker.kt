package com.practice.reelbreak.core.permission


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

object OverlayPermissionChecker {

    fun isOverlayEnabled(context: Context): Boolean {
        return Settings.canDrawOverlays(context)
    }

    fun openOverlaySettings(context: Context) {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${context.packageName}")
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}
