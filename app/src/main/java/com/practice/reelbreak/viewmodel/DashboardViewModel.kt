package com.practice.reelbreak.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.practice.reelbreak.ui.dashboard.DashboardState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class DashboardViewModel : ViewModel() {
    private val _selectedTab = mutableStateOf(0)
  //  val selectedTab: State<Int> = _selectedTab


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

    // DashboardViewModel.kt
    init {
        _uiState.value = DashboardState(
            userName = "Sangeeta",
            isDarkMode = true,
            reelsCount = 0,           // ZERO by default
            percentageIncrease = 0,
            dailyLimitMinutes = 60,
            timeSpentMinutes = 0,     // ZERO by default
            selectedTab = 0,
            isCounterVisible = false  // Hidden until permissions
        )
    }

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

