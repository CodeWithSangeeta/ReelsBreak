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
import androidx.lifecycle.dashboardViewModel.compose.dashboardViewModel
import androidx.navigation.NavController
import com.practice.reelbreak.ui.component.GradientColor
import com.practice.reelbreak.ui.navigation.Routes
import com.practice.reelbreak.ui.permission.PermissionNudgeCard
import com.practice.reelbreak.dashboardViewModel.DashboarddashboardViewModel
import com.practice.reelbreak.viewmodel.DashboardViewModel
import com.practice.reelbreak.viewmodel.PermissionsViewModel
import kotlinx.coroutines.delay


@Composable
fun DashboardScreen(navController: NavController, dashboardViewModel: DashboardViewModel,
                    permissionsViewModel : PermissionsViewModel
) {
    val dashboardState by dashboardViewModel.uiState.collectAsState()
    val permissionState by permissionsViewModel.permissionState.collectAsState()
    var showPermissionNudge by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(3000)
        if (!permissionState.permissionsGranted) showPermissionNudge = true
    }

    Scaffold(
        bottomBar = {
            FloatingNavBar(
                selectedTab = permissionsViewModel.selectedTab,
                onTabSelected = { index ->
                    dashboardViewModel.updateSelectedTab(index)
                    when (index) {
                        0 -> navController.navigate(Routes.DASHBOARD)
                        1 -> navController.navigate(Routes.ANALYTICS)
                        2 -> navController.navigate(Routes.FOCUS)
                        3 -> navController.navigate(Routes.LIMITS)
                        4 -> navController.navigate(Routes.ALERTS)
                    }
                }
            )
        }
    )
        { paddingValues ->
          Box(
            modifier = Modifier
                .fillMaxSize()
                .background(GradientColor.background)
              .padding(paddingValues)
        ) {
              // Floating Permission Nudge
              if (showPermissionNudge && !state.permissionsGranted) {
                  PermissionNudgeCard(
                      onClick = { navController.navigate(Routes.PERMISSION) }
                  )
              }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                DashboardHeader(
                    userName = state.userName,
                    onVisibilityToggle = { dashboardViewModel.toggleCounterVisibility() },
                    onThemeToggle = { dashboardViewModel.toggleTheme() }
                )
                StatisticsCard(state)
                ActionGrid(
                    onAnalyticsClick = { navController.navigate(Routes.ANALYTICS) },
                    onFocusClick = { navController.navigate(Routes.FOCUS) },
                    onLimitsClick = { navController.navigate(Routes.LIMITS) },
                    onAlertsClick = { navController.navigate(Routes.ALERTS) }
                )
                Spacer(modifier = Modifier.height(100.dp)) // Space for the floating nav
            }

        }
      }
    }



