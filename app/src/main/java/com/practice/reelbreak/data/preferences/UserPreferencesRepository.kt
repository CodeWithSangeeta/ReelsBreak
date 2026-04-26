package com.practice.reelbreak.data.preferences


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.practice.reelbreak.domain.model.ActiveBlockMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

/**
 * Extension property — creates a single DataStore instance tied to Context.
 *
 * WHY at file level (not inside the class)?
 * DataStore must be a SINGLETON — only one instance should exist for the
 * entire app lifetime. Defining it as an extension on Context ensures
 * Android only ever creates it once, no matter how many times it's accessed.
 *
 * "user_preferences" → the name of the file stored on disk.
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preferences"
)

/**
 * UserPreferencesRepository — The single gateway to all user preferences.
 *
 * MVVM Role: This is the REPOSITORY layer.
 * - ViewModels call this to read/write user settings.
 * - BlockingDecisionEngine calls this to read blocking rules.
 * - NO UI code here. NO ViewModel logic here. Pure data access.
 *
 * WHY suspend functions for writes?
 * DataStore writes happen on a background coroutine thread.
 * 'suspend' tells Kotlin: "this function can pause without blocking the UI thread."
 * You must call suspend functions from a coroutine (ViewModel's viewModelScope).
 *
 * WHY Flow for reads?
 * Flow is like a live stream of data. When a value changes in DataStore,
 * every Flow subscriber automatically gets the new value.
 * ViewModel collects this Flow → UI re-renders automatically.
 * This is the core of reactive MVVM.
 */
class UserPreferencesRepository(private val context: Context) {

    // ── Read Flows (ViewModel observes these) ─────────────────────────────
    //
    // .data → the raw Flow<Preferences> from DataStore
    // .map  → transforms it into the specific value we want
    // ?: default → if key doesn't exist yet, use this default value

    val isStrictMode: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.IS_STRICT_MODE] ?: true }
    //                                                           ^^^^ default ON

    val isCuratedMode: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.IS_CURATED_MODE] ?: false }

    val dailyReelLimit: Flow<Int> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.DAILY_REEL_LIMIT] ?: 0 }

    val dailyTimeLimitMinutes: Flow<Int> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.DAILY_TIME_LIMIT_MINUTES] ?: 0 }

    val reelsWatchedToday: Flow<Int> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.REELS_WATCHED_TODAY] ?: 0 }

    val timeSpentTodayMinutes: Flow<Int> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.TIME_SPENT_TODAY_MINUTES] ?: 0 }

    val followedCreators: Flow<Set<String>> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.FOLLOWED_CREATORS] ?: emptySet() }

    val onboardingCompleted: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.ONBOARDING_COMPLETED] ?: false }

    /**
     * Is today's limit already exceeded?
     *
     * Compares the stored exceeded-date with today's date.
     * If they match → limit was exceeded today → return true.
     * If they don't match (yesterday or null) → return false (auto-reset!).
     */
    val isLimitExceededToday: Flow<Boolean> = context.dataStore.data
        .map { prefs ->
            val exceededDate = prefs[UserPreferences.LIMIT_EXCEEDED_DATE]
            val today = LocalDate.now().toString() // "2026-04-05"
            exceededDate == today
        }

    // ── Write Functions (ViewModel calls these) ───────────────────────────

    val activeMode: Flow<ActiveBlockMode> =
        context.dataStore.data.map { prefs ->
            val stored = prefs[UserPreferences.ACTIVE_MODE] ?: ActiveBlockMode.STRICT.value
            ActiveBlockMode.fromValue(stored)
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

    suspend fun setDailyTimeLimit(minutes: Int) {
        context.dataStore.edit { prefs ->
            prefs[UserPreferences.DAILY_TIME_LIMIT_MINUTES] = minutes
        }
    }

    /**
     * Called by DetectionManager each time a new reel is detected.
     * Increments today's reel count by 1.
     */
    suspend fun incrementReelsWatched() {
        context.dataStore.edit { prefs ->
            val current = prefs[UserPreferences.REELS_WATCHED_TODAY] ?: 0
            prefs[UserPreferences.REELS_WATCHED_TODAY] = current + 1
        }
    }

    /**
     * Called by DetectionManager periodically while user is on shorts.
     * Adds elapsed minutes to today's time spent.
     */
    suspend fun addTimeSpent(minutes: Int) {
        context.dataStore.edit { prefs ->
            val current = prefs[UserPreferences.TIME_SPENT_TODAY_MINUTES] ?: 0
            prefs[UserPreferences.TIME_SPENT_TODAY_MINUTES] = current + minutes
        }
    }

    /**
     * Called by BlockingDecisionEngine when a limit is exceeded.
     * Stores today's date so isLimitExceededToday returns true for
     * the rest of the day.
     */
    suspend fun markLimitExceededToday() {
        context.dataStore.edit { prefs ->
            prefs[UserPreferences.LIMIT_EXCEEDED_DATE] = LocalDate.now().toString()
        }
    }

    suspend fun addFollowedCreator(name: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[UserPreferences.FOLLOWED_CREATORS] ?: emptySet()
            prefs[UserPreferences.FOLLOWED_CREATORS] = current + name
        }
    }

    suspend fun removeFollowedCreator(name: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[UserPreferences.FOLLOWED_CREATORS] ?: emptySet()
            prefs[UserPreferences.FOLLOWED_CREATORS] = current - name
        }
    }

    suspend fun setOnboardingCompleted() {
        context.dataStore.edit { prefs ->
            prefs[UserPreferences.ONBOARDING_COMPLETED] = true
        }
    }

    /**
     * Resets today's usage counters.
     * Call this at midnight OR when a new day is detected.
     */
    suspend fun resetDailyCounters() {
        context.dataStore.edit { prefs ->
            prefs[UserPreferences.REELS_WATCHED_TODAY] = 0
            prefs[UserPreferences.TIME_SPENT_TODAY_MINUTES] = 0
            // Don't clear LIMIT_EXCEEDED_DATE — isLimitExceededToday
            // auto-resets by comparing with today's date
        }
    }


    // Read Flows
    val isNotificationsEnabled: Flow<Boolean> = context.dataStore.data.map { prefs -> prefs[UserPreferences.IS_NOTIFICATIONS_ENABLED] ?: true }

    val isWeekendRelaxEnabled: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.IS_WEEKEND_RELAX_ENABLED] ?: false }

    // Write Functions
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

    val isOverlayEnabled: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[UserPreferences.IS_OVERLAY_ENABLED] ?: false }

    suspend fun setOverlayEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[UserPreferences.IS_OVERLAY_ENABLED] = enabled
        }
    }

}