package com.practice.reelbreak.ui.settings

data class SettingsState(
    val isNotificationsEnabled: Boolean = true,
    val isWeekendRelaxEnabled: Boolean = false,

    val isPrivacySectionExpanded: Boolean = false,
    val isHelpSectionExpanded: Boolean = false,

    val appVersion: String = "1.0.0"
)