package com.sangeeta.reelsbreak.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.sangeeta.reelsbreak.domain.model.ActiveBlockMode
import com.sangeeta.reelsbreak.domain.model.LimitResetPeriod
import com.sangeeta.reelsbreak.domain.model.ProtectionMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preferences"
)

class UserPreferencesRepository(private val context: Context) {

    val isStrictMode: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.IS_STRICT_MODE] ?: true }

    val dailyReelLimit: Flow<Int> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.DAILY_REEL_LIMIT] ?: 0 }

    val dailyTimeLimitMinutes: Flow<Int> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.DAILY_TIME_LIMIT_MINUTES] ?: 0 }

    val reelsWatchedToday: Flow<Int> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.REELS_WATCHED_TODAY] ?: 0 }

      val activeMode: Flow<ActiveBlockMode> = context.dataStore.data
        .map { prefs ->
            val stored = prefs[UserPreferences.ACTIVE_MODE] ?: ActiveBlockMode.STRICT.value
            ActiveBlockMode.fromValue(stored)
        }

    val isNotificationsEnabled: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.IS_NOTIFICATIONS_ENABLED] ?: true }

    val isWeekendRelaxEnabled: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.IS_WEEKEND_RELAX_ENABLED] ?: false }

    val isFocusActive: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.IS_FOCUS_ACTIVE] ?: false }

    val focusEndTimestamp: Flow<Long> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.FOCUS_END_TIMESTAMP] ?: 0L }

    val blockedPackages: Flow<Set<String>> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.BLOCKED_PACKAGES] ?: emptySet() }

    val selectedFocusMinutes: Flow<Int> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.SELECTED_FOCUS_MINUTES] ?: 0 }

    val timeSpentTodayMillis: Flow<Long> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.TIME_SPENT_TODAY_MILLIS] ?: 0L }

    val timeSpentTodayMinutes: Flow<Int> = timeSpentTodayMillis
        .map { millis -> (millis / 60_000L).toInt() }

    val limitResetPeriod: Flow<LimitResetPeriod> = context.dataStore.data
        .map { prefs ->
            val stored = prefs[UserPreferences.LIMIT_RESET_PERIOD] ?: LimitResetPeriod.DAY.value
            LimitResetPeriod.fromValue(stored)
        }

    val isLimitExceededNow: Flow<Boolean> = context.dataStore.data
        .map { prefs ->
            val blockedUntil = prefs[UserPreferences.LIMIT_EXCEEDED_UNTIL_EPOCH_MILLIS] ?: 0L
            System.currentTimeMillis() < blockedUntil
        }

    val isOverlayReminderEnabled: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.IS_OVERLAY_REMINDER_ENABLED] ?: false }

    val protectionMode: Flow<ProtectionMode> = context.dataStore.data
        .map { prefs ->
            when (prefs[UserPreferences.PROTECTION_MODE]) {
                "FLOW"  -> ProtectionMode.FLOW
                "CURIOUS" -> ProtectionMode.CURIOUS
                else      -> ProtectionMode.PAUSED
            }
        }

    suspend fun setActiveMode(mode: ActiveBlockMode) {
        context.dataStore.edit { prefs ->
            prefs[UserPreferences.ACTIVE_MODE] = mode.value
        }
    }

    suspend fun setStrictMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[UserPreferences.IS_STRICT_MODE] = enabled
        }
    }
        suspend fun setDailyReelLimit(limit: Int) {
        context.dataStore.edit { prefs ->
            prefs[UserPreferences.DAILY_REEL_LIMIT] = limit
        }
    }

    suspend fun setDailyTimeLimitMinutes(minutes: Int) {
        context.dataStore.edit { prefs ->
            prefs[UserPreferences.DAILY_TIME_LIMIT_MINUTES] = minutes
        }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[UserPreferences.IS_NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun setWeekendRelaxEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[UserPreferences.IS_WEEKEND_RELAX_ENABLED] = enabled
        }
    }

    suspend fun setBlockedPackages(packages: Set<String>) {
        context.dataStore.edit { prefs ->
            prefs[UserPreferences.BLOCKED_PACKAGES] = packages
        }
    }

    suspend fun startFocusSession(endTimestampMillis: Long) {
        context.dataStore.edit { prefs ->
            prefs[UserPreferences.IS_FOCUS_ACTIVE] = true
            prefs[UserPreferences.FOCUS_END_TIMESTAMP] = endTimestampMillis
        }
    }

    suspend fun stopFocusSession() {
        context.dataStore.edit { prefs ->
            prefs[UserPreferences.IS_FOCUS_ACTIVE] = false
            prefs[UserPreferences.FOCUS_END_TIMESTAMP] = 0L
            prefs[UserPreferences.BLOCKED_PACKAGES] = emptySet()
        }
    }

    suspend fun setSelectedFocusMinutes(minutes: Int) {
        context.dataStore.edit { prefs ->
            prefs[UserPreferences.SELECTED_FOCUS_MINUTES] = minutes
        }
    }

    suspend fun incrementReelsWatched() {
        context.dataStore.edit { prefs ->
            val current = prefs[UserPreferences.REELS_WATCHED_TODAY] ?: 0
            prefs[UserPreferences.REELS_WATCHED_TODAY] = current + 1
        }
    }

    suspend fun addTimeSpentMillis(deltaMillis: Long) {
        context.dataStore.edit { prefs ->
            val current = prefs[UserPreferences.TIME_SPENT_TODAY_MILLIS] ?: 0L
            prefs[UserPreferences.TIME_SPENT_TODAY_MILLIS] = current + deltaMillis
        }
    }

    suspend fun setLimitResetPeriod(period: LimitResetPeriod) {
        context.dataStore.edit { prefs ->
            prefs[UserPreferences.LIMIT_RESET_PERIOD] = period.value
        }
    }

    suspend fun setOverlayReminderEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[UserPreferences.IS_OVERLAY_REMINDER_ENABLED] = enabled
        }
    }

    suspend fun setProtectionMode(mode: ProtectionMode) {
        context.dataStore.edit { prefs ->
            prefs[UserPreferences.PROTECTION_MODE] = mode.name
            prefs[UserPreferences.ACTIVE_MODE] = when (mode) {
                ProtectionMode.FLOW -> ActiveBlockMode.STRICT.value
                ProtectionMode.CURIOUS -> ActiveBlockMode.LIMIT.value
                ProtectionMode.PAUSED  -> ActiveBlockMode.LIMIT.value
            }
            prefs[UserPreferences.IS_STRICT_MODE] = (mode == ProtectionMode.FLOW)
        }
    }

    suspend fun ensureCountersAreFresh() {
        val now = System.currentTimeMillis()
        val period = limitResetPeriod.first()

        context.dataStore.edit { prefs ->
            val lastReset = prefs[UserPreferences.LAST_COUNTER_RESET_EPOCH_MILLIS] ?: 0L

            val shouldReset = when (period) {
                LimitResetPeriod.HOUR -> {
                    lastReset == 0L || (now - lastReset) >= 3600000L // 1 hour threshold
                }
                LimitResetPeriod.DAY -> {
                    if (lastReset == 0L) {
                        true
                    } else {
                        val lastDate = Instant.ofEpochMilli(lastReset)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        val today = LocalDate.now()
                        lastDate != today
                    }
                }
            }

            if (shouldReset) {
                prefs[UserPreferences.REELS_WATCHED_TODAY] = 0
                prefs[UserPreferences.TIME_SPENT_TODAY_MILLIS] = 0L
                prefs[UserPreferences.LIMIT_EXCEEDED_UNTIL_EPOCH_MILLIS] = 0L
                prefs[UserPreferences.LAST_COUNTER_RESET_EPOCH_MILLIS] = now
            }
        }
    }

    suspend fun markLimitExceededForCurrentWindow() {
        val now = System.currentTimeMillis()
        val period = limitResetPeriod.first()

        context.dataStore.edit { prefs ->
            val blockedUntil = when (period) {
                LimitResetPeriod.HOUR -> {
                    val lastReset = prefs[UserPreferences.LAST_COUNTER_RESET_EPOCH_MILLIS] ?: now
                    lastReset + 3600000L
                }
                LimitResetPeriod.DAY -> {
                    LocalDate.now()
                        .plusDays(1)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                }
            }
            prefs[UserPreferences.LIMIT_EXCEEDED_UNTIL_EPOCH_MILLIS] = blockedUntil
        }
    }
}