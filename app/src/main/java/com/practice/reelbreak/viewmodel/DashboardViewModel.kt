package com.practice.reelbreak.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.reelbreak.data.preferences.UserPreferencesRepository
import com.practice.reelbreak.domain.model.ActiveBlockMode
import com.practice.reelbreak.ui.dashboard.BlockMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.practice.reelbreak.ui.dashboard.DashboardState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardState())
    val uiState: StateFlow<DashboardState> = _uiState

    init {
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
            userPreferencesRepository.isOverlayEnabled.collectLatest { enabled ->
                _uiState.update { it.copy(isOverlayEnabled = enabled) }
            }
        }
    }

    fun toggleTheme() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    fun toggleCounterVisibility() {
        _uiState.update { it.copy(isCounterVisible = !it.isCounterVisible) }
    }

    fun updateSelectedTab(tab: Int) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun toggleOverlayEnabled() {
        viewModelScope.launch {
            val current = uiState.value.isOverlayEnabled
            userPreferencesRepository.setOverlayEnabled(!current)
        }
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

                BlockMode.SMART_FILTER -> {
                    userPreferencesRepository.setActiveMode(ActiveBlockMode.SMART)
                    userPreferencesRepository.setStrictMode(false)
                    // later: set curated/smart flags too
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

    }

