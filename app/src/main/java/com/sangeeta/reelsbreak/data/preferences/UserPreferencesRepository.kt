//package com.practice.reelbreak.data.preferences
//
//import android.content.Context
//import androidx.datastore.core.DataStore
//import androidx.datastore.preferences.core.Preferences
//import androidx.datastore.preferences.core.edit
//import androidx.datastore.preferences.preferencesDataStore
//import com.practice.reelbreak.data.preferences.UserPreferences.DAILY_TIME_LIMIT_MINUTES
//import com.practice.reelbreak.domain.model.ActiveBlockMode
//import com.practice.reelbreak.domain.model.LimitResetPeriod
//import com.practice.reelbreak.domain.model.ProtectionMode
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.runBlocking
//import java.time.Instant
//import java.time.LocalDate
//import java.time.ZoneId
//
//
//private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
//    name = "user_preferences"
//)
//
//class UserPreferencesRepository(private val context: Context) {
//
//    val isStrictMode: Flow<Boolean> = context.dataStore.data
//        .map { prefs -> prefs[UserPreferences.IS_STRICT_MODE] ?: true }
//
//    val isCuratedMode: Flow<Boolean> = context.dataStore.data
//        .map { prefs -> prefs[UserPreferences.IS_CURATED_MODE] ?: false }
//
//    val dailyReelLimit: Flow<Int> = context.dataStore.data
//        .map { prefs -> prefs[UserPreferences.DAILY_REEL_LIMIT] ?: 0 }
//
//    val dailyTimeLimitMinutes: Flow<Int> = context.dataStore.data
//        .map { prefs -> prefs[UserPreferences.DAILY_TIME_LIMIT_MINUTES] ?: 0 }
//
//    val reelsWatchedToday: Flow<Int> = context.dataStore.data
//        .map { prefs -> prefs[UserPreferences.REELS_WATCHED_TODAY] ?: 0 }
//
//
//    val followedCreators: Flow<Set<String>> = context.dataStore.data
//        .map { prefs -> prefs[UserPreferences.FOLLOWED_CREATORS] ?: emptySet() }
//
//    val onboardingCompleted: Flow<Boolean> = context.dataStore.data
//        .map { prefs -> prefs[UserPreferences.ONBOARDING_COMPLETED] ?: false }
//
//
//    val isLimitExceededToday: Flow<Boolean> = context.dataStore.data
//        .map { prefs ->
//            val exceededDate = prefs[UserPreferences.LIMIT_EXCEEDED_DATE]
//            val today = LocalDate.now().toString() // "2026-04-05"
//            exceededDate == today
//        }
//
//
//    val activeMode: Flow<ActiveBlockMode> =
//        context.dataStore.data.map { prefs ->
//            val stored = prefs[UserPreferences.ACTIVE_MODE] ?: ActiveBlockMode.STRICT.value
//            ActiveBlockMode.fromValue(stored)
//        }
//
//    suspend fun setActiveMode(mode: ActiveBlockMode) {
//        context.dataStore.edit { prefs ->
//            prefs[UserPreferences.ACTIVE_MODE] = mode.value
//        }
//    }
//
//    suspend fun setStrictMode(enabled: Boolean) {
//        context.dataStore.edit { prefs ->
//            prefs[UserPreferences.IS_STRICT_MODE] = enabled
//        }
//    }
//
//    suspend fun setCuratedMode(enabled: Boolean) {
//        context.dataStore.edit { prefs ->
//            prefs[UserPreferences.IS_CURATED_MODE] = enabled
//        }
//    }
//
//    suspend fun setDailyReelLimit(limit: Int) {
//        context.dataStore.edit { prefs ->
//            prefs[UserPreferences.DAILY_REEL_LIMIT] = limit
//        }
//    }
//
//    suspend fun setDailyTimeLimit(minutes: Int) {
//        context.dataStore.edit { prefs ->
//            prefs[UserPreferences.DAILY_TIME_LIMIT_MINUTES] = minutes
//        }
//    }
//
//
//    suspend fun addTimeSpent(minutes: Int) {
//        context.dataStore.edit { prefs ->
//            val current = prefs[UserPreferences.TIME_SPENT_TODAY_MINUTES] ?: 0
//            prefs[UserPreferences.TIME_SPENT_TODAY_MINUTES] = current + minutes
//        }
//    }
//
//
//    suspend fun markLimitExceededToday() {
//        context.dataStore.edit { prefs ->
//            prefs[UserPreferences.LIMIT_EXCEEDED_DATE] = LocalDate.now().toString()
//        }
//    }
//
//    suspend fun addFollowedCreator(name: String) {
//        context.dataStore.edit { prefs ->
//            val current = prefs[UserPreferences.FOLLOWED_CREATORS] ?: emptySet()
//            prefs[UserPreferences.FOLLOWED_CREATORS] = current + name
//        }
//    }
//
//    suspend fun removeFollowedCreator(name: String) {
//        context.dataStore.edit { prefs ->
//            val current = prefs[UserPreferences.FOLLOWED_CREATORS] ?: emptySet()
//            prefs[UserPreferences.FOLLOWED_CREATORS] = current - name
//        }
//    }
//
//    suspend fun setOnboardingCompleted() {
//        context.dataStore.edit { prefs ->
//            prefs[UserPreferences.ONBOARDING_COMPLETED] = true
//        }
//    }
//
//    val isNotificationsEnabled: Flow<Boolean> = context.dataStore.data.map { prefs -> prefs[UserPreferences.IS_NOTIFICATIONS_ENABLED] ?: true }
//
//    val isWeekendRelaxEnabled: Flow<Boolean> = context.dataStore.data
//        .map { prefs -> prefs[UserPreferences.IS_WEEKEND_RELAX_ENABLED] ?: false }
//
//    // Write Functions
//    suspend fun setNotificationsEnabled(enabled: Boolean) {
//        context.dataStore.edit { prefs ->
//            prefs[UserPreferences.IS_NOTIFICATIONS_ENABLED] = enabled
//        }
//    }
//
//    suspend fun setWeekendRelaxEnabled(enabled: Boolean) {
//        context.dataStore.edit { prefs ->
//            prefs[UserPreferences.IS_WEEKEND_RELAX_ENABLED] = enabled
//        }
//    }
//
//
//
//    fun getActiveModeBlocking(): Int = runBlocking {
//        context.dataStore.data.map { prefs ->
//            prefs[UserPreferences.ACTIVE_MODE] ?: ActiveBlockMode.STRICT.value
//        }.first()
//    }
//
//
//    suspend fun setDailyTimeLimitMinutes(minutes: Int) {
//        context.dataStore.edit { prefs ->
//            prefs[DAILY_TIME_LIMIT_MINUTES] = minutes
//        }
//    }
//
//
//    val isFocusActive: Flow<Boolean> = context.dataStore.data
//        .map { prefs -> prefs[UserPreferences.IS_FOCUS_ACTIVE] ?: false }
//
//    val focusEndTimestamp: Flow<Long> = context.dataStore.data
//        .map { prefs -> prefs[UserPreferences.FOCUS_END_TIMESTAMP] ?: 0L }
//
//
//    val blockedPackages: Flow<Set<String>> =
//        context.dataStore.data.map { prefs ->
//            prefs[UserPreferences.BLOCKED_PACKAGES] ?: emptySet()
//        }
//
//    suspend fun setBlockedPackages(packages: Set<String>) {
//        context.dataStore.edit { prefs ->
//            prefs[UserPreferences.BLOCKED_PACKAGES] = packages
//        }
//    }
//    // Set focus active + end time
//    suspend fun startFocusSession(endTimestampMillis: Long) {
//        context.dataStore.edit { prefs ->
//            prefs[UserPreferences.IS_FOCUS_ACTIVE] = true
//            prefs[UserPreferences.FOCUS_END_TIMESTAMP] = endTimestampMillis
//        }
//    }
//
//    suspend fun stopFocusSession() {
//        context.dataStore.edit { prefs ->
//            prefs[UserPreferences.IS_FOCUS_ACTIVE] = false
//            prefs[UserPreferences.FOCUS_END_TIMESTAMP] = 0L
//            prefs[UserPreferences.BLOCKED_PACKAGES] = emptySet()
//        }
//    }
//
//    val selectedFocusMinutes: Flow<Int> = context.dataStore.data.map { prefs ->
//        prefs[UserPreferences.SELECTED_FOCUS_MINUTES] ?: 0
//    }
//
//    suspend fun setSelectedFocusMinutes(minutes: Int) {
//        context.dataStore.edit { prefs ->
//            prefs[UserPreferences.SELECTED_FOCUS_MINUTES] = minutes
//        }
//    }
//
//    // NEW: raw millis
//    val timeSpentTodayMillis: Flow<Long> = context.dataStore.data
//        .map { prefs ->
//            prefs[UserPreferences.TIME_SPENT_TODAY_MILLIS] ?: 0L
//        }
//
//    // CHANGE: timeSpentTodayMinutes should derive from millis now
//    val timeSpentTodayMinutes: Flow<Int> = timeSpentTodayMillis
//        .map { millis ->
//            (millis / 60_000L).toInt()
//        }
//
//
//    suspend fun incrementReelsWatched() {
//        ensureCountersAreFresh()
//        context.dataStore.edit { prefs ->
//            val current = prefs[UserPreferences.REELS_WATCHED_TODAY] ?: 0
//            prefs[UserPreferences.REELS_WATCHED_TODAY] = current + 1
//        }
//    }
//
//    suspend fun addTimeSpentMillis(deltaMillis: Long) {
//        ensureCountersAreFresh()
//        context.dataStore.edit { prefs ->
//            val current = prefs[UserPreferences.TIME_SPENT_TODAY_MILLIS] ?: 0L
//            prefs[UserPreferences.TIME_SPENT_TODAY_MILLIS] = current + deltaMillis
//        }
//    }
//
//    suspend fun resetDailyCounters() {
//        context.dataStore.edit { prefs ->
//            prefs[UserPreferences.REELS_WATCHED_TODAY] = 0
//            prefs[UserPreferences.TIME_SPENT_TODAY_MILLIS] = 0L
//            prefs[UserPreferences.LIMIT_EXCEEDED_DATE] = ""
//            prefs[UserPreferences.LAST_COUNTER_RESET_DATE] = LocalDate.now().toString()
//        }
//    }
//
//    val limitResetPeriod: Flow<LimitResetPeriod> = context.dataStore.data.map { prefs ->
//        val stored = prefs[UserPreferences.LIMIT_RESET_PERIOD] ?: LimitResetPeriod.DAY.value
//        LimitResetPeriod.fromValue(stored)
//    }
//
//    suspend fun setLimitResetPeriod(period: LimitResetPeriod) {
//        context.dataStore.edit { prefs ->
//            prefs[UserPreferences.LIMIT_RESET_PERIOD] = period.value
//        }
//    }
//
//    val isLimitExceededNow: Flow<Boolean> = context.dataStore.data.map { prefs ->
//        val blockedUntil = prefs[UserPreferences.LIMIT_EXCEEDED_UNTIL_EPOCH_MILLIS] ?: 0L
//        System.currentTimeMillis() < blockedUntil
//    }
//
//    suspend fun ensureCountersAreFresh() {
//        val now = System.currentTimeMillis()
//        val period = limitResetPeriod.first()
//
//        context.dataStore.edit { prefs ->
//            val lastReset = prefs[UserPreferences.LAST_COUNTER_RESET_EPOCH_MILLIS] ?: 0L
//
//            val shouldReset = when (period) {
//                LimitResetPeriod.HOUR -> {
//                    lastReset == 0L || now - lastReset >= 60 * 60 * 1000L
//                }
//                LimitResetPeriod.DAY -> {
//                    if (lastReset == 0L) {
//                        true
//                    } else {
//                        val lastDate = Instant.ofEpochMilli(lastReset)
//                            .atZone(ZoneId.systemDefault())
//                            .toLocalDate()
//                        val today = LocalDate.now()
//                        lastDate != today
//                    }
//                }
//            }
//
//            if (shouldReset) {
//                prefs[UserPreferences.REELS_WATCHED_TODAY] = 0
//                prefs[UserPreferences.TIME_SPENT_TODAY_MILLIS] = 0L
//                prefs[UserPreferences.LIMIT_EXCEEDED_UNTIL_EPOCH_MILLIS] = 0L
//                prefs[UserPreferences.LAST_COUNTER_RESET_EPOCH_MILLIS] = now
//            }
//        }
//    }
//
//    suspend fun markLimitExceededForCurrentWindow() {
//        val now = System.currentTimeMillis()
//        val period = limitResetPeriod.first()
//
//        context.dataStore.edit { prefs ->
//            val blockedUntil = when (period) {
//                LimitResetPeriod.HOUR -> {
//                    val lastReset = prefs[UserPreferences.LAST_COUNTER_RESET_EPOCH_MILLIS] ?: now
//                    lastReset + 60 * 60 * 1000L
//                }
//                LimitResetPeriod.DAY -> {
//                    LocalDate.now()
//                        .plusDays(1)
//                        .atStartOfDay(ZoneId.systemDefault())
//                        .toInstant()
//                        .toEpochMilli()
//                }
//            }
//
//            prefs[UserPreferences.LIMIT_EXCEEDED_UNTIL_EPOCH_MILLIS] = blockedUntil
//        }
//    }
//
//    val isOverlayReminderEnabled: Flow<Boolean> =
//        context.dataStore.data.map { prefs ->
//            prefs[UserPreferences.IS_OVERLAY_REMINDER_ENABLED] ?: false
//        }
//
//    suspend fun setOverlayReminderEnabled(enabled: Boolean) {
//        context.dataStore.edit { prefs ->
//            prefs[UserPreferences.IS_OVERLAY_REMINDER_ENABLED] = enabled
//        }
//    }
//
//
//    val protectionMode: Flow<ProtectionMode> =
//        context.dataStore.data.map { prefs ->
//            when (prefs[UserPreferences.PROTECTION_MODE]) {
//                "PAUSED"  -> ProtectionMode.PAUSED
//                "MINDFUL" -> ProtectionMode.MINDFUL
//                else      -> ProtectionMode.DEFAULT
//            }
//        }
//
//    suspend fun setProtectionMode(mode: ProtectionMode) {
//        context.dataStore.edit { prefs ->
//            prefs[UserPreferences.PROTECTION_MODE] = mode.name
//            prefs[UserPreferences.ACTIVE_MODE] = when (mode) {
//                ProtectionMode.DEFAULT -> ActiveBlockMode.STRICT.value
//                ProtectionMode.MINDFUL -> ActiveBlockMode.LIMIT.value
//                ProtectionMode.PAUSED  -> ActiveBlockMode.LIMIT.value
//            }
//            prefs[UserPreferences.IS_STRICT_MODE] = (mode == ProtectionMode.DEFAULT)
//        }
//    }
//}



package com.practice.reelbreak.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.practice.reelbreak.domain.model.ActiveBlockMode
import com.practice.reelbreak.domain.model.LimitResetPeriod
import com.practice.reelbreak.domain.model.ProtectionMode
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

    val isCuratedMode: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.IS_CURATED_MODE] ?: false }

    val dailyReelLimit: Flow<Int> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.DAILY_REEL_LIMIT] ?: 0 }

    val dailyTimeLimitMinutes: Flow<Int> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.DAILY_TIME_LIMIT_MINUTES] ?: 0 }

    val reelsWatchedToday: Flow<Int> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.REELS_WATCHED_TODAY] ?: 0 }

    val followedCreators: Flow<Set<String>> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.FOLLOWED_CREATORS] ?: emptySet() }

    val onboardingCompleted: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.ONBOARDING_COMPLETED] ?: false }

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
                "PAUSED"  -> ProtectionMode.PAUSED
                "MINDFUL" -> ProtectionMode.MINDFUL
                else      -> ProtectionMode.DEFAULT
            }
        }

    // ─── WRITE API OPERATIONS (ALL COMPLIANT ASYNC) ──────────────────────────

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

    suspend fun setCuratedMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[UserPreferences.IS_CURATED_MODE] = enabled
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
                ProtectionMode.DEFAULT -> ActiveBlockMode.STRICT.value
                ProtectionMode.MINDFUL -> ActiveBlockMode.LIMIT.value
                ProtectionMode.PAUSED  -> ActiveBlockMode.LIMIT.value
            }
            prefs[UserPreferences.IS_STRICT_MODE] = (mode == ProtectionMode.DEFAULT)
        }
    }

    // ─── CRITICAL COUNTER AUTOMATION PROTECTION SYSTEMS ──────────────────────

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