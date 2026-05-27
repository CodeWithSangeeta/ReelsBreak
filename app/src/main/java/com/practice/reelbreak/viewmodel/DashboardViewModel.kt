package com.practice.reelbreak.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.reelbreak.data.preferences.UserPreferencesRepository
import com.practice.reelbreak.domain.model.ActiveBlockMode
import com.practice.reelbreak.domain.model.LimitResetPeriod
import com.practice.reelbreak.ui.dashboard.BlockMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import com.practice.reelbreak.ui.dashboard.DashboardState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardState())
    val uiState: StateFlow<DashboardState> = _uiState

    init {

        // daily reel limit
        viewModelScope.launch {
            userPreferencesRepository.dailyReelLimit.collect { limit ->
                _uiState.update { it.copy(dailyReelLimit = limit) }
                refreshMindfulRemaining()
            }
        }

        // daily time limit minutes
        viewModelScope.launch {
            userPreferencesRepository.dailyTimeLimitMinutes.collect { minutes ->
                _uiState.update { it.copy(dailyTimeLimitMinutes = minutes) }
                refreshMindfulRemaining()
            }
        }

        // reels watched today
        viewModelScope.launch {
            userPreferencesRepository.reelsWatchedToday.collect { count ->
                _uiState.update { it.copy(reelsCount = count) }
                refreshMindfulRemaining()
            }
        }

        // time spent today minutes
        viewModelScope.launch {
            userPreferencesRepository.timeSpentTodayMinutes.collect { minutes ->
                _uiState.update { it.copy(timeSpentMinutes = minutes) }
                refreshMindfulRemaining()
            }
        }

        viewModelScope.launch {
            userPreferencesRepository.isStrictMode.collectLatest { isStrict ->
                _uiState.update { it.copy(isStrictMode = isStrict) }
            }
        }

        viewModelScope.launch {
            userPreferencesRepository.activeMode.collectLatest { mode ->
                _uiState.update { it.copy(activeMode = mode) }
            }
        }

        viewModelScope.launch {
            userPreferencesRepository.dailyReelLimit.collectLatest { limit ->
                _uiState.update { it.copy(dailyReelLimit = limit) }
            }
        }

        viewModelScope.launch {
            userPreferencesRepository.dailyTimeLimitMinutes.collectLatest { minutes ->
                _uiState.update { it.copy(dailyTimeLimitMinutes = minutes) }
            }
        }

        viewModelScope.launch {
            userPreferencesRepository.limitResetPeriod.collect { period ->
                _uiState.update { it.copy(limitResetPeriod = period) }
            }
        }

        viewModelScope.launch {
            userPreferencesRepository.isOverlayReminderEnabled.collectLatest { enabled ->
                _uiState.update { it.copy(overlayEnabled = enabled) }
            }
        }

    }

    fun toggleTheme() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }



    fun onBlockModeCardClicked(mode: BlockMode) {
        _uiState.update { state ->
            val newExpanded =
                if (state.expandedMode == mode) null else mode
            state.copy(expandedMode = newExpanded)
        }

        viewModelScope.launch {
            when (mode) {
                BlockMode.BLOCK_NOW -> {
                    // Strict active, others off
                    userPreferencesRepository.setActiveMode(ActiveBlockMode.STRICT)
                    userPreferencesRepository.setStrictMode(true)
                }

                BlockMode.LIMIT_BASED -> {
                    userPreferencesRepository.setActiveMode(ActiveBlockMode.LIMIT)
                    // Strict off when Limit is active
                    userPreferencesRepository.setStrictMode(false)
                }

            }
        }

            if (mode == BlockMode.BLOCK_NOW) {
                viewModelScope.launch {
                    val current = uiState.value.isStrictMode
                    userPreferencesRepository.setStrictMode(!current)
                }
            }

    }

    fun onExpandToggle(mode: BlockMode) {
        val currentExpanded = _uiState.value.expandedMode
        _uiState.update {
            it.copy(
                // If same mode tapped — collapse. If different — expand that one.
                expandedMode = if (currentExpanded == mode) null else mode
            )
        }
    }


    fun incrementDailyTimeLimit(step: Int = 5) {
        viewModelScope.launch {
            val current = uiState.value.dailyTimeLimitMinutes
            val newValue = (current + step).coerceAtMost(600)   // max 10 hours
            userPreferencesRepository.setDailyTimeLimitMinutes(newValue)
        }
    }

    fun decrementDailyTimeLimit(step: Int = 5) {
        viewModelScope.launch {
            val current = uiState.value.dailyTimeLimitMinutes
            val newValue = (current - step).coerceAtLeast(5)    // min 5 minutes
            userPreferencesRepository.setDailyTimeLimitMinutes(newValue)
        }
    }

    fun incrementDailyReelLimit(step: Int = 5) {
        viewModelScope.launch {
            val current = uiState.value.dailyReelLimit
            val newValue = (current + step).coerceAtMost(1000)
            userPreferencesRepository.setDailyReelLimit(newValue)
        }
    }

    fun decrementDailyReelLimit(step: Int = 5) {
        viewModelScope.launch {
            val current = uiState.value.dailyReelLimit
            val newValue = (current - step).coerceAtLeast(5)
            userPreferencesRepository.setDailyReelLimit(newValue)
        }
    }

    fun setDailyReelLimit(value: Int) {
        viewModelScope.launch {
            userPreferencesRepository.setDailyReelLimit(value.coerceAtLeast(0))
        }
    }

    fun setDailyTimeLimit(value: Int) {
        viewModelScope.launch {
            userPreferencesRepository.setDailyTimeLimitMinutes(value.coerceAtLeast(0))
        }
    }


    fun setMindfulCountEnabled(enabled: Boolean) = viewModelScope.launch {
        if (!enabled) {
            userPreferencesRepository.setDailyReelLimit(0)
        } else if (uiState.value.dailyReelLimit == 0) {
            userPreferencesRepository.setDailyReelLimit(10)
        }
    }

    fun setMindfulTimeEnabled(enabled: Boolean) = viewModelScope.launch {
        if (!enabled) {
            userPreferencesRepository.setDailyTimeLimitMinutes(0)
        } else if (uiState.value.dailyTimeLimitMinutes == 0) {
            userPreferencesRepository.setDailyTimeLimitMinutes(20)
        }
    }

    private fun refreshMindfulRemaining() {
        val state = uiState.value
        val remainingCount = (state.dailyReelLimit - state.reelsCount).coerceAtLeast(0)
        val remainingMinutes = (state.dailyTimeLimitMinutes - state.timeSpentMinutes).coerceAtLeast(0)

        _uiState.update {
            it.copy(
                mindfulRemainingCount = remainingCount,
                mindfulRemainingMinutes = remainingMinutes
            )
        }
    }


    fun setLimitResetPeriod(period: LimitResetPeriod) = viewModelScope.launch {
        userPreferencesRepository.setLimitResetPeriod(period)
    }



    fun onOverlayReminderToggle(enabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setOverlayReminderEnabled(enabled)
        }
    }
    }

