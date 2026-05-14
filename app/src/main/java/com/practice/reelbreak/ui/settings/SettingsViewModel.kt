package com.practice.reelbreak.ui.settings

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.practice.reelbreak.data.preferences.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val context: Context get() = getApplication<Application>().applicationContext
    private val repository = UserPreferencesRepository(context)

    private val _uiState = MutableStateFlow(SettingsState())
    val uiState: StateFlow<SettingsState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
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



        // Load app version
        loadAppVersion()
    }



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
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            ).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
            context.startActivity(intent)
        }
    }

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

    fun openPrivacyPolicy() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://your-privacy-policy-url.com")
        ).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
        context.startActivity(intent)
    }

    private fun loadAppVersion() {
        try {
            val versionName = context.packageManager
                .getPackageInfo(context.packageName, 0).versionName
            _uiState.update { it.copy(appVersion = versionName ?: "1.0.0") }
        } catch (e: Exception) {
        }
    }
}