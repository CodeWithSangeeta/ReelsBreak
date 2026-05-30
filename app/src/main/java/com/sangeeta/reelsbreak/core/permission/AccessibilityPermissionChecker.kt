package com.sangeeta.reelsbreak.core.permission

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.sangeeta.reelsbreak.core.accessibility.ReelsAccessibilityService

object AccessibilityPermissionChecker {

    fun isAccessibilityEnabled(context: Context): Boolean {
        val expectedComponent = ComponentName(context, ReelsAccessibilityService::class.java)

        val enabledServices = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false

        return enabledServices.split(":")
            .mapNotNull { ComponentName.unflattenFromString(it) }
            .any { it == expectedComponent }
    }

    fun openAccessibilitySettings(context: Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}