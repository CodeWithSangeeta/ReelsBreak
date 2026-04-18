package com.practice.reelbreak.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.practice.reelbreak.ui.dashboard.DashboardState

class DashboardViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        DashboardState(
            userName = "Sangeeta",
            isDarkMode = false,
            reelsCount = 0,
            percentageIncrease = 0,
            dailyLimitMinutes = 60,
            timeSpentMinutes = 0,
            selectedTab = 0,
            isCounterVisible = false
        )
    )
    val uiState: StateFlow<DashboardState> = _uiState.asStateFlow()

    // Controls whole-app theme
    fun toggleTheme() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    fun toggleCounterVisibility() {
        _uiState.update { it.copy(isCounterVisible = !it.isCounterVisible) }
    }

    fun updateSelectedTab(tab: Int) {
        _uiState.update { it.copy(selectedTab = tab) }
    }
}