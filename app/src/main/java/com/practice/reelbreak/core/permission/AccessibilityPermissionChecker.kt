//package com.practice.reelbreak.core.permission
//
//
//import android.content.Context
//import android.provider.Settings
//
//object AccessibilityPermissionChecker {
//
//    fun isAccessibilityEnabled(context: Context): Boolean {
//        val enabledServices =
//            Settings.Secure.getString(
//                context.contentResolver,
//                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
//            ) ?: return false
//
//        return enabledServices.contains(context.packageName)
//    }
//
//    fun openAccessibilitySettings(context: Context) {
//        context.startActivity(
//            android.content.Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
//                addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
//            }
//        )
//    }
//}


package com.practice.reelbreak.core.permission

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.practice.reelbreak.core.accessibility.ReelsAccessibilityService

object AccessibilityPermissionChecker {

    /**
     * Checks explicitly if our exact ReelsAccessibilityService component is
     * turned on in the system security settings.
     */
    fun isAccessibilityEnabled(context: Context): Boolean {
        // Build the precise target identifier for your background service component
        val expectedComponent = ComponentName(context, ReelsAccessibilityService::class.java)

        val enabledServices = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false

        // Settings split by colons format -> package/className:package/className
        return enabledServices.split(":")
            .mapNotNull { ComponentName.unflattenFromString(it) }
            .any { it == expectedComponent }
    }

    /**
     * Directs the user instantly into the system accessibility menu page.
     */
    fun openAccessibilitySettings(context: Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}