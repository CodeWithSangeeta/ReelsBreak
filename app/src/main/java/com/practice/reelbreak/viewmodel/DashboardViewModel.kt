package com.practice.reelbreak.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.practice.reelbreak.domain.model.PermissionState
import com.practice.reelbreak.ui.dashboard.DashboardState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DashboardViewModel : ViewModel() {
     private val _uiState = MutableStateFlow(
        DashboardState(
            userName = "Sangeeta",
            isDarkMode = true,
            reelsCount = 0,
            percentageIncrease = 0,
            dailyLimitMinutes = 60,
            timeSpentMinutes = 0,
            selectedTab = 0,
            isCounterVisible = false
        )
    )
    val uiState: StateFlow<DashboardState> = _uiState.asStateFlow()
    private val _permissionState = MutableStateFlow(PermissionState())
    val permissionState = _permissionState.asStateFlow()


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

