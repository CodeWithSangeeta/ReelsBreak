package com.sangeeta.reelbreak.ui.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.getValue
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sangeeta.reelbreak.ui.theme.DashboardTheme
import com.sangeeta.reelbreak.viewmodel.DashboardViewModel


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
