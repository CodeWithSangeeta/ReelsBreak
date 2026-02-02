package com.sangeeta.reelbreak.ui.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import com.sangeeta.reelbreak.viewmodel.DashboardViewModel


@Composable
fun DashboardContent(
    state : DashboardState,
    viewModel: DashboardViewModel)
{
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
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

        FloatingNavBar(
            selectedTab = state.selectedTab,
            onTabSelected = { viewModel.updateSelectedTab(it) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}
