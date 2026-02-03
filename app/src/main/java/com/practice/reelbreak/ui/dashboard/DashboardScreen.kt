package com.practice.reelbreak.ui.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practice.reelbreak.ui.theme.DashboardTheme
import com.practice.reelbreak.viewmodel.DashboardViewModel


@Composable
fun DashboardScreen(viewModel: DashboardViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()

    DashboardTheme(isDarkMode = state.isDarkMode) {
        DashboardContent(
            state = state,
            viewModel = viewModel
        )
    }


}
