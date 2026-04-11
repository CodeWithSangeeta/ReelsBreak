package com.practice.reelbreak.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.practice.reelbreak.ReelBreakApplication
import com.practice.reelbreak.data.preferences.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * LimitsViewModel — bridges LimitsScreen UI ↔ UserPreferencesRepository
 *
 * WHY AndroidViewModel (not ViewModel)?
 * We need applicationContext to access our repository singleton.
 * AndroidViewModel provides `application` safely without leaking Activity.
 *
 * WHY stateIn()?
 * Converts a cold Flow (DataStore) into a hot StateFlow that the UI
 * can collect. UI always gets the latest value immediately on first collect.
 * SharingStarted.WhileSubscribed(5000) → keeps the flow alive for 5s
 * after the last subscriber (e.g. screen rotation) — avoids restarting.
 */
class LimitsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserPreferencesRepository =
        (application as ReelBreakApplication).repository

    // UI observes these StateFlows
    val isStrictMode: StateFlow<Boolean> = repository.isStrictMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true  // shown while DataStore loads
        )

    val dailyReelLimit: StateFlow<Int> = repository.dailyReelLimit
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val dailyTimeLimitMinutes: StateFlow<Int> = repository.dailyTimeLimitMinutes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // UI calls these functions on user interaction
    fun setStrictMode(enabled: Boolean) {
        viewModelScope.launch {
            repository.setStrictMode(enabled)
        }
    }

    fun setDailyReelLimit(limit: Int) {
        viewModelScope.launch {
            repository.setDailyReelLimit(limit)
        }
    }

    fun setDailyTimeLimit(minutes: Int) {
        viewModelScope.launch {
            repository.setDailyTimeLimit(minutes)
        }
    }
}