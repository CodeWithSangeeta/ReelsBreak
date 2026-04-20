package com.practice.reelbreak.ui.settings

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.practice.reelbreak.core.permission.AccessibilityPermissionChecker
import com.practice.reelbreak.core.permission.UsagePermissionChecker
import com.practice.reelbreak.data.preferences.UserPreferencesRepository
import com.practice.reelbreak.ui.permission.PermissionSheetType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * SettingsViewModel — Owns all logic for SettingsScreen.
 *
 * MVVM Role: ViewModel layer
 * - Reads permissions (live, refreshed on every onResume via refreshPermissions())
 * - Reads/writes behavior toggles from UserPreferencesRepository (DataStore)
 * - Handles all Intent launches (share, email, Play Store) — keeps UI pure
 * - Exposes ONE StateFlow<SettingsState> — UI only reads this, never writes directly
 *
 * WHY AndroidViewModel (not ViewModel)?
 * We need Application context to:
 *   1. Check permission state (Settings.Secure needs a Context)
 *   2. Launch Intents (share, email)
 * AndroidViewModel gives us applicationContext safely — no memory leaks.
 */
class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val context: Context get() = getApplication<Application>().applicationContext

    // Repository — single source of truth for persisted user preferences
    private val repository = UserPreferencesRepository(context)

    // ── Internal mutable state ────────────────────────────────────────────
    private val _uiState = MutableStateFlow(SettingsState())
    val uiState: StateFlow<SettingsState> = _uiState.asStateFlow()

    // ── Init: wire DataStore flows into uiState ───────────────────────────
    init {
        viewModelScope.launch {
            // Combine both behavior toggles into a single collector
            // so we don't launch two separate coroutines
            combine(
                repository.isNotificationsEnabled,
                repository.isWeekendRelaxEnabled
            ) { notifications, weekend ->
                Pair(notifications, weekend)
            }.collect { (notifications, weekend) ->
                _uiState.update { current ->
                    current.copy(
                        isNotificationsEnabled = notifications,
                        isWeekendRelaxEnabled = weekend
                    )
                }
            }
        }

        // Load permission state on first launch
        refreshPermissions()

        // Load app version
        loadAppVersion()
    }

    // ── Permission Refresh ────────────────────────────────────────────────
    /**
     * Call this from SettingsScreen's LaunchedEffect(Unit) AND
     * from the Screen's onResume (via LifecycleEventObserver).
     * This ensures status updates LIVE after user returns from system settings.
     */
    fun refreshPermissions() {
        _uiState.update { current ->
            current.copy(
                isAccessibilityGranted = AccessibilityPermissionChecker.isAccessibilityEnabled(context),
                isUsageAccessGranted = UsagePermissionChecker.isUsageAccessGranted(context),
                isOverlayGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    android.provider.Settings.canDrawOverlays(context)
                } else {
                    true
                }
            )
        }
    }

    // ── Permission Sheet ──────────────────────────────────────────────────
    fun openPermissionSheet(type: PermissionSheetType) {
        _uiState.update { it.copy(activePermissionSheet = type) }
    }

    fun closePermissionSheet() {
        _uiState.update { it.copy(activePermissionSheet = null) }
    }

    // ── Open System Settings for Permissions ─────────────────────────────
    fun openPermissionSettings(type: PermissionSheetType) {
        when (type) {
            PermissionSheetType.ACCESSIBILITY ->
                AccessibilityPermissionChecker.openAccessibilitySettings(context)
            PermissionSheetType.USAGE_ACCESS ->
                UsagePermissionChecker.openUsageAccessSettings(context)
            PermissionSheetType.OVERLAY -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val intent = Intent(
                        android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:${context.packageName}")
                    ).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
                    context.startActivity(intent)
                }
            }
        }
    }

    // ── Behavior Toggles ──────────────────────────────────────────────────
    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            repository.setNotificationsEnabled(enabled)
        }
    }

    fun toggleWeekendRelax(enabled: Boolean) {
        viewModelScope.launch {
            repository.setWeekendRelaxEnabled(enabled)
        }
    }

    // ── Collapsible Sections ──────────────────────────────────────────────
    fun togglePrivacySection() {
        _uiState.update { it.copy(isPrivacySectionExpanded = !it.isPrivacySectionExpanded) }
    }

    fun toggleHelpSection() {
        _uiState.update { it.copy(isHelpSectionExpanded = !it.isHelpSectionExpanded) }
    }

    // ── About & Support Actions ───────────────────────────────────────────
    /**
     * Opens Android's native share sheet — shows WhatsApp, Telegram, Gmail etc.
     * No SDK needed for individual apps — Android handles all targets automatically.
     */
    fun shareApp() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                "Take control of your screen time! I'm using ReelsBreak to stop mindless scrolling 🚫📱\n\n" +
                        "Download it here: https://play.google.com/store/apps/details?id=${context.packageName}"
            )
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(Intent.createChooser(intent, "Share ReelsBreak").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    /**
     * Opens Play Store directly on the app's page.
     * Falls back to browser if Play Store is not installed.
     */
    fun rateApp() {
        val packageName = context.packageName
        try {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$packageName")
            ).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
            context.startActivity(intent)
        } catch (e: android.content.ActivityNotFoundException) {
            // Play Store not installed — open browser
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            ).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
            context.startActivity(intent)
        }
    }

    /**
     * Opens email app with pre-filled subject for feedback.
     * Device: auto-filled so you know what Android version they're on.
     */
    fun sendFeedback() {
        val deviceInfo = "Device: ${Build.MANUFACTURER} ${Build.MODEL} | Android ${Build.VERSION.RELEASE}"
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:reelsbreak.app@gmail.com")
            putExtra(Intent.EXTRA_SUBJECT, "ReelsBreak - Feedback & Suggestions")
            putExtra(
                Intent.EXTRA_TEXT,
                "Hi ReelsBreak team,\n\nI have a suggestion:\n\n\n\n---\n" +
                        "App Version: ${_uiState.value.appVersion}\n$deviceInfo"
            )
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    /**
     * Opens email app pre-filled for support/contact.
     */
    fun contactUs() {
        val deviceInfo = "Device: ${Build.MANUFACTURER} ${Build.MODEL} | Android ${Build.VERSION.RELEASE}"
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:reelsbreak.app@gmail.com")
            putExtra(Intent.EXTRA_SUBJECT, "ReelsBreak - Support Request")
            putExtra(
                Intent.EXTRA_TEXT,
                "Hi ReelsBreak team,\n\nI need help with:\n\n\n\n---\n" +
                        "App Version: ${_uiState.value.appVersion}\n$deviceInfo"
            )
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    /**
     * Opens Privacy Policy in the default browser.
     * Replace the URL with your actual hosted privacy policy page.
     */
    fun openPrivacyPolicy() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://your-privacy-policy-url.com")
        ).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
        context.startActivity(intent)
    }

    // ── Private Helpers ───────────────────────────────────────────────────
    private fun loadAppVersion() {
        try {
            val versionName = context.packageManager
                .getPackageInfo(context.packageName, 0).versionName
            _uiState.update { it.copy(appVersion = versionName ?: "1.0.0") }
        } catch (e: Exception) {
            // Keep default "1.0.0" if PackageManager fails
        }
    }
}