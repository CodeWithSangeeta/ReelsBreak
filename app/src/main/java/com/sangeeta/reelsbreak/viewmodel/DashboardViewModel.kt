package com.sangeeta.reelsbreak.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangeeta.reelsbreak.data.preferences.UserPreferencesRepository
import com.sangeeta.reelsbreak.domain.model.ActiveBlockMode
import com.sangeeta.reelsbreak.domain.model.LimitResetPeriod
import com.sangeeta.reelsbreak.domain.model.ProtectionMode
import com.sangeeta.reelsbreak.ui.dashboard.BlockMode
import com.sangeeta.reelsbreak.ui.dashboard.DashboardState
import com.sangeeta.reelsbreak.ui.dashboard.HomeProtectionMode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    // Isolate pure view-local interactive properties from disk pipeline streams
    private val _isDarkMode = MutableStateFlow(true)
    private val _expandedMode = MutableStateFlow<BlockMode?>(null)

    val uiState: StateFlow<DashboardState> = combine(
        userPreferencesRepository.dailyReelLimit,
        userPreferencesRepository.dailyTimeLimitMinutes,
        userPreferencesRepository.reelsWatchedToday,
        userPreferencesRepository.timeSpentTodayMinutes,
        userPreferencesRepository.isStrictMode,
        userPreferencesRepository.activeMode,
        userPreferencesRepository.limitResetPeriod,
        userPreferencesRepository.isOverlayReminderEnabled,
        userPreferencesRepository.protectionMode,
        _isDarkMode,
        _expandedMode
    ) { params ->
        val dailyReelLimit = params[0] as Int
        val dailyTimeLimitMinutes = params[1] as Int
        val reelsCount = params[2] as Int
        val timeSpentMinutes = params[3] as Int
        val isStrictMode = params[4] as Boolean
        val activeMode = params[5] as ActiveBlockMode
        val limitResetPeriod = params[6] as LimitResetPeriod
        val overlayEnabled = params[7] as Boolean
        val protectionMode = params[8] as ProtectionMode
        val darkChoice = params[9] as Boolean
        val expandedChoice = params[10] as BlockMode?

        val remainingCount = (dailyReelLimit - reelsCount).coerceAtLeast(0)
        val remainingMinutes = (dailyTimeLimitMinutes - timeSpentMinutes).coerceAtLeast(0)

        DashboardState(
            dailyReelLimit = dailyReelLimit,
            dailyTimeLimitMinutes = dailyTimeLimitMinutes,
            reelsCount = reelsCount,
            timeSpentMinutes = timeSpentMinutes,
            isStrictMode = isStrictMode,
            activeMode = activeMode,
            limitResetPeriod = limitResetPeriod,
            overlayEnabled = overlayEnabled,
            protectionMode = protectionMode,
            curiousRemainingCount = remainingCount,
            curiousRemainingMinutes = remainingMinutes,
            isDarkMode = darkChoice,
            expandedMode = expandedChoice
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardState()
    )

    fun toggleTheme() {
        _isDarkMode.update { !it }
    }

    fun onBlockModeCardClicked(mode: BlockMode) {
        _expandedMode.update { current -> if (current == mode) null else mode }
        viewModelScope.launch {
            when (mode) {
                BlockMode.BLOCK_NOW -> {
                    userPreferencesRepository.setActiveMode(ActiveBlockMode.STRICT)
                    userPreferencesRepository.setStrictMode(true)
                }
                BlockMode.LIMIT_BASED -> {
                    userPreferencesRepository.setActiveMode(ActiveBlockMode.LIMIT)
                    userPreferencesRepository.setStrictMode(false)
                }
            }
        }
    }

    fun setLimitResetPeriod(period: LimitResetPeriod) = viewModelScope.launch {
        userPreferencesRepository.setLimitResetPeriod(period)
    }

    fun onOverlayReminderToggle(enabled: Boolean) = viewModelScope.launch {
        userPreferencesRepository.setOverlayReminderEnabled(enabled)
    }

    fun onModeSelected(mode: HomeProtectionMode) = viewModelScope.launch {
        when (mode) {
            HomeProtectionMode.PAUSED -> {
                userPreferencesRepository.setProtectionMode(ProtectionMode.PAUSED)
                _expandedMode.value = null
            }
            HomeProtectionMode.FLOW -> {
                userPreferencesRepository.setProtectionMode(ProtectionMode.FLOW)
                _expandedMode.value = null
            }
            HomeProtectionMode.CURIOUS -> {
                userPreferencesRepository.setProtectionMode(ProtectionMode.CURIOUS)
                _expandedMode.value = BlockMode.LIMIT_BASED
            }
        }
    }

    fun setCuriousCountEnabled(enabled: Boolean) = viewModelScope.launch {
        if (!enabled) {
            userPreferencesRepository.setDailyReelLimit(0)
        } else if (uiState.value.dailyReelLimit == 0) {
            userPreferencesRepository.setDailyReelLimit(10)
        }
    }

    fun setCuriousTimeEnabled(enabled: Boolean) = viewModelScope.launch {
        if (!enabled) {
            userPreferencesRepository.setDailyTimeLimitMinutes(0)
        } else if (uiState.value.dailyTimeLimitMinutes == 0) {
            userPreferencesRepository.setDailyTimeLimitMinutes(20)
        }
    }

    fun setDailyReelLimit(value: Int) = viewModelScope.launch {
        userPreferencesRepository.setDailyReelLimit(value.coerceAtLeast(0))
    }

    fun setDailyTimeLimit(value: Int) = viewModelScope.launch {
        userPreferencesRepository.setDailyTimeLimitMinutes(value.coerceAtLeast(0))
    }
}