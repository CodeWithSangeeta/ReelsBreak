package com.sangeeta.reelsbreak.data.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey


object UserPreferences {
    val IS_STRICT_MODE = booleanPreferencesKey("is_strict_mode")
    val IS_CURATED_MODE = booleanPreferencesKey("is_curated_mode")
    val ACTIVE_MODE = intPreferencesKey("active_mode")
    val DAILY_REEL_LIMIT = intPreferencesKey("daily_reel_limit")
    val DAILY_TIME_LIMIT_MINUTES = intPreferencesKey("daily_time_limit_minutes")
    val REELS_WATCHED_TODAY = intPreferencesKey("reels_watched_today")
    val TIME_SPENT_TODAY_MINUTES = intPreferencesKey("time_spent_today_minutes")
    val LIMIT_EXCEEDED_DATE = stringPreferencesKey("limit_exceeded_date")
    val FOLLOWED_CREATORS = stringSetPreferencesKey("followed_creators")
    val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    val IS_NOTIFICATIONS_ENABLED = booleanPreferencesKey("is_notifications_enabled")
    val IS_WEEKEND_RELAX_ENABLED = booleanPreferencesKey("is_weekend_relax_enabled")
    val IS_FOCUS_ACTIVE = booleanPreferencesKey("is_focus_active")
    val BLOCKED_PACKAGES = stringSetPreferencesKey("blocked_packages")
    val FOCUS_END_TIMESTAMP = longPreferencesKey("focus_end_timestamp")
    val SELECTED_FOCUS_MINUTES = intPreferencesKey("selected_focus_minutes")
    val TIME_SPENT_TODAY_MILLIS = longPreferencesKey("time_spent_today_millis")
    val LAST_COUNTER_RESET_DATE = stringPreferencesKey("last_counter_reset_date")
    val LIMIT_RESET_PERIOD = intPreferencesKey("limit_reset_period")
    val LAST_COUNTER_RESET_EPOCH_MILLIS = longPreferencesKey("last_counter_reset_epoch_millis")
    val LIMIT_EXCEEDED_UNTIL_EPOCH_MILLIS = longPreferencesKey("limit_exceeded_until_epoch_millis")
    val IS_OVERLAY_REMINDER_ENABLED = booleanPreferencesKey("is_overlay_reminder_enabled")
    val PROTECTION_MODE = stringPreferencesKey("protection_mode")

    val FLOW_MODE_INFO_SEEN = booleanPreferencesKey("flow_mode_info_seen")
    val PAUSE_MODE_INFO_SEEN = booleanPreferencesKey("pause_mode_info_seen")
    val CURIOUS_MODE_INFO_SEEN = booleanPreferencesKey("curious_mode_info_seen")

}