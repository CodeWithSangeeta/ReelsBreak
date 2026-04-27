package com.practice.reelbreak.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.reelbreak.data.preferences.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


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

