package com.sangeeta.reelbreak.viewmodel

import androidx.lifecycle.ViewModel
import com.sangeeta.reelbreak.ui.dashboard.DashboardState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class DashboardViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        DashboardState(
            userName = "Sangeeta",
            isDarkMode = true,
            reelsCount = 202,
            percentageIncrease = 23,
            dailyLimitMinutes = 60,
            timeSpentMinutes = 142,
            selectedTab = 0,
            isCounterVisible = true
        )
    )
    val uiState: StateFlow<DashboardState> = _uiState

    fun toggleTheme() {
        _uiState.update {
            it.copy(isDarkMode = !it.isDarkMode)
        }
    }

    fun toggleCounterVisibility() {
        _uiState.update {
            it.copy(isCounterVisible = !it.isCounterVisible)
        }
    }

    fun updateSelectedTab(tab: Int) {
        _uiState.update {
            it.copy(selectedTab = tab)
        }
    }
}

