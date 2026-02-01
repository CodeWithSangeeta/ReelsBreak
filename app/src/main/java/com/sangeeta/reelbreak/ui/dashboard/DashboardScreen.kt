package com.sangeeta.reelbreak.ui.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sangeeta.reelbreak.viewmodel.DashboardViewModel


@Composable
fun DashboardScreen(viewModel: DashboardViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()
    val isDarkMode = state.isDarkMode // Handled by ViewModel toggle

    Box(modifier = Modifier.fillMaxSize().background(if (isDarkMode) Color(0xFF0F0425) else Color.White)) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            DashboardHeader(state.userName, isDarkMode) { viewModel.toggleTheme() }
            StatisticsCard(state, isDarkMode)
            ActionGrid(isDarkMode)
            Spacer(modifier = Modifier.height(100.dp)) // Space for the floating nav
        }

        FloatingNavBar(
            selectedTab = state.selectedTab,
            onTabSelected = { viewModel.updateTab(it) },
            isDarkMode = isDarkMode,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp)
        )
    }
}
