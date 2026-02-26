package com.practice.reelbreak.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.practice.reelbreak.viewmodel.DashboardViewModel


@Composable
fun DashboardScreen(navController: NavController, viewModel: DashboardViewModel ) {
    Scaffold(
        bottomBar = {
            FloatingNavBar(
                selectedTab = viewModel.selectedTab,
                onTabSelected = { tabIndex ->
                    viewModel.updateSelectedTab(tabIndex)
                    when (tabIndex) {
                        0 -> navController.navigate("dashboard") { popUpTo(navController.graph.startDestinationId) }
                        1 -> navController.navigate("stats")
                        // Add other tabs
                    }
                }
            )
        }
    ) { paddingValues ->
        val state by viewModel.uiState.collectAsState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                DashboardHeader(
                    userName = state.userName,
                    onVisibilityToggle = { viewModel.toggleCounterVisibility() },
                    onThemeToggle = { viewModel.toggleTheme() }
                )
                StatisticsCard(state)
                ActionGrid()
                Spacer(modifier = Modifier.height(100.dp)) // Space for the floating nav
            }

        }
      }
    }



