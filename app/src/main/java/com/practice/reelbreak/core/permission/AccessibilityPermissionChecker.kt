package com.practice.reelbreak.core.permission


import android.content.Context
import android.provider.Settings

object AccessibilityPermissionChecker {

    fun isAccessibilityEnabled(context: Context): Boolean {
        val enabledServices =
            Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            ) ?: return false

        return enabledServices.contains(context.packageName)
    }

    fun openAccessibilitySettings(context: Context) {
        context.startActivity(
            android.content.Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
    }
}
