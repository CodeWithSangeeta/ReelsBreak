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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardState())
    val uiState: StateFlow<DashboardState> = _uiState.asStateFlow()

    init {
        // Combined Collection Pipeline to minimize thread resource allocations
        viewModelScope.launch {
            combine(
                userPreferencesRepository.dailyReelLimit,
                userPreferencesRepository.dailyTimeLimitMinutes,
                userPreferencesRepository.reelsWatchedToday,
                userPreferencesRepository.timeSpentTodayMinutes,
                userPreferencesRepository.isStrictMode,
                userPreferencesRepository.activeMode,
                userPreferencesRepository.limitResetPeriod,
                userPreferencesRepository.isOverlayReminderEnabled,
                userPreferencesRepository.protectionMode
            ) { params ->
                // Destructure snapshot fields safely
                val dailyReelLimit = params[0] as Int
                val dailyTimeLimitMinutes = params[1] as Int
                val reelsCount = params[2] as Int
                val timeSpentMinutes = params[3] as Int
                val isStrictMode = params[4] as Boolean
                val activeMode = params[5] as ActiveBlockMode
                val limitResetPeriod = params[6] as LimitResetPeriod
                val overlayEnabled = params[7] as Boolean
                val protectionMode = params[8] as ProtectionMode

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
                    isDarkMode = _uiState.value.isDarkMode, // Retain existing UI choice
                    expandedMode = _uiState.value.expandedMode
                )
            }.collect { updatedState ->
                _uiState.value = updatedState
            }
        }
    }

    fun toggleTheme() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    fun onBlockModeCardClicked(mode: BlockMode) {
        _uiState.update { state ->
            val newExpanded = if (state.expandedMode == mode) null else mode
            state.copy(expandedMode = newExpanded)
        }

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
                _uiState.update { it.copy(expandedMode = null) }
            }
            HomeProtectionMode.FLOW -> {
                userPreferencesRepository.setProtectionMode(ProtectionMode.FLOW)
                _uiState.update { it.copy(expandedMode = null) }
            }
            HomeProtectionMode.CURIOUS -> {
                userPreferencesRepository.setProtectionMode(ProtectionMode.CURIOUS)
                _uiState.update { it.copy(expandedMode = BlockMode.LIMIT_BASED) }
            }
        }
    }
    fun setCuriousCountEnabled(enabled: Boolean) = viewModelScope.launch {
        if (!enabled) {
            userPreferencesRepository.setDailyReelLimit(0)
        } else if (_uiState.value.dailyReelLimit == 0) {
            userPreferencesRepository.setDailyReelLimit(10) // Standard production default threshold count
        }
    }

    fun setCuriousTimeEnabled(enabled: Boolean) = viewModelScope.launch {
        if (!enabled) {
            userPreferencesRepository.setDailyTimeLimitMinutes(0)
        } else if (_uiState.value.dailyTimeLimitMinutes == 0) {
            userPreferencesRepository.setDailyTimeLimitMinutes(20) // Standard production default threshold duration
        }
    }

    fun setDailyReelLimit(value: Int) = viewModelScope.launch {
        userPreferencesRepository.setDailyReelLimit(value.coerceAtLeast(0))
    }

    fun setDailyTimeLimit(value: Int) = viewModelScope.launch {
        userPreferencesRepository.setDailyTimeLimitMinutes(value.coerceAtLeast(0))
    }
}