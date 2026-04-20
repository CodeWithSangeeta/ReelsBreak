package com.practice.reelbreak.ui.settings
import com.practice.reelbreak.ui.permission.PermissionSheetType
/**
 * SettingsState — The single source of truth for SettingsScreen UI.
 *
 * MVVM Role: This is the UI State data class.
 * - SettingsViewModel exposes a StateFlow<SettingsState>
 * - SettingsScreen reads it via collectAsState()
 * - NO logic lives here — only plain data
 */
data class SettingsState(

    // ── Permissions ───────────────────────────────────────────────────────
    val isAccessibilityGranted: Boolean = false,
    val isUsageAccessGranted: Boolean = false,
    val isOverlayGranted: Boolean = false,

    // ── Behavior Toggles ──────────────────────────────────────────────────
    val isNotificationsEnabled: Boolean = true,
    val isWeekendRelaxEnabled: Boolean = false,

    // ── Bottom Sheet ──────────────────────────────────────────────────────
    // Which permission detail sheet is open (null = none open)
    val activePermissionSheet: PermissionSheetType? = null,

    // ── Collapsible Sections ──────────────────────────────────────────────
    val isPrivacySectionExpanded: Boolean = false,
    val isHelpSectionExpanded: Boolean = false,

    // ── App Info ──────────────────────────────────────────────────────────
    val appVersion: String = "1.0.0"
)