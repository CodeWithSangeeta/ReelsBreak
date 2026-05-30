package com.sangeeta.reelsbreak.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object OnboardingKey : NavKey

@Serializable
data object DashboardKey : NavKey

@Serializable
data object FocusKey : NavKey

@Serializable
data object SettingsKey : NavKey

object Routes {
    const val DASHBOARD = "home"
    const val FOCUS = "focus"
    const val SETTINGS = "settings"
}