package com.practice.reelbreak.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.reelbreak.data.preferences.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

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


@HiltViewModel
class LimitsViewModel @Inject constructor(
    private val repo: UserPreferencesRepository
) : ViewModel() {

    val dailyReelLimit: Flow<Int> = repo.dailyReelLimit
    val dailyTimeLimitMinutes: Flow<Int> = repo.dailyTimeLimitMinutes

    fun saveLimits(reelLimit: Int, timeLimitMinutes: Int) {
        viewModelScope.launch {
            repo.setDailyReelLimit(reelLimit)
            repo.setDailyTimeLimit(timeLimitMinutes)
        }
    }
}

