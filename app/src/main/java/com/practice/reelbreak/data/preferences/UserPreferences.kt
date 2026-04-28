package com.practice.reelbreak.data.preferences


import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * UserPreferences — Central registry of all DataStore keys.
 *
 * WHY: Instead of hardcoding string key names like "is_strict_mode"
 * scattered across the codebase, we define them ONCE here.
 * This prevents typos and makes refactoring safe.
 *
 * HOW DataStore works:
 * DataStore stores key-value pairs on disk (like SharedPreferences but
 * safer — it uses Kotlin Coroutines and Flow, so reads/writes are
 * always on a background thread and never block the UI).
 */
object UserPreferences {

    // ── Mode Settings ─────────────────────────────────────────────────────
    // These keys store which blocking mode the user has chosen.

    /**
     * Strict Mode — block all shorts immediately, every time.
     * Default: true (on by default for new users)
     */
    val IS_STRICT_MODE = booleanPreferencesKey("is_strict_mode")

    val IS_OVERLAY_ENABLED = booleanPreferencesKey("is_overlay_enabled")

    /**
     * Curated Mode — only allow shorts from followed creators.
     * Default: false
     */
    val IS_CURATED_MODE = booleanPreferencesKey("is_curated_mode")

    // ── Limit Settings ────────────────────────────────────────────────────
    // These keys store the limits the user configures in LimitsScreen.

    val ACTIVE_MODE = intPreferencesKey("active_mode")
    /**
     * Max reels the user can watch per day.
     * 0 means "no reel count limit set".
     */
    val DAILY_REEL_LIMIT = intPreferencesKey("daily_reel_limit")

    /**
     * Max time (in minutes) the user can spend on shorts per day.
     * 0 means "no time limit set".
     */
    val DAILY_TIME_LIMIT_MINUTES = intPreferencesKey("daily_time_limit_minutes")

    // ── Today's Usage ─────────────────────────────────────────────────────
    // These keys track the user's ACTUAL usage today.
    // They reset at midnight.

    /**
     * How many reels the user has watched today.
     * Incremented by DetectionManager every time a new reel is detected.
     */
    val REELS_WATCHED_TODAY = intPreferencesKey("reels_watched_today")

    /**
     * How many minutes the user has spent on shorts today.
     * Updated periodically by DetectionManager while a reel is playing.
     */
    val TIME_SPENT_TODAY_MINUTES = intPreferencesKey("time_spent_today_minutes")

    /**
     * The date (as "yyyy-MM-dd" string) when the daily limit was exceeded.
     * Used to check: "did the user already hit their limit TODAY?"
     *
     * WHY a date string and not a boolean?
     * If we stored a boolean "limit_exceeded = true", it would stay true
     * forever. With a date string, we compare it to today's date —
     * if it's yesterday's date, the limit auto-resets. No cron job needed.
     *
     * Example: "2026-04-05" → limit was hit on April 5th
     */
    val LIMIT_EXCEEDED_DATE = stringPreferencesKey("limit_exceeded_date")

    // ── Curated Mode Data ─────────────────────────────────────────────────

    /**
     * The set of creator names the user follows (whitelist).
     * Example: {"MrBeast", "TechBurner", "CarryMinati"}
     *
     * WHY Set<String> and not a Room DB table?
     * For a whitelist of names, a Set is perfect — fast lookup (contains()),
     * no duplicates, easy to add/remove. Room DB would be overkill here.
     * If in future we store creator metadata (profile pic, follower count),
     * THEN we'd move to Room.
     */
    val FOLLOWED_CREATORS = stringSetPreferencesKey("followed_creators")

    // ── App State ─────────────────────────────────────────────────────────

    /**
     * Whether the user has completed the onboarding flow.
     * Already used in MainViewModel — we keep it here for consistency.
     */
    val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")

    val IS_NOTIFICATIONS_ENABLED = booleanPreferencesKey("is_notifications_enabled")

    // Whether limits are relaxed on Sat/Sun. Default: false
    val IS_WEEKEND_RELAX_ENABLED = booleanPreferencesKey("is_weekend_relax_enabled")

    // Is a focus session currently active
    val IS_FOCUS_ACTIVE = booleanPreferencesKey("is_focus_active")

    // When the current focus session ends (epoch millis)
    val FOCUS_END_TIMESTAMP = longPreferencesKey("focus_end_timestamp")

}