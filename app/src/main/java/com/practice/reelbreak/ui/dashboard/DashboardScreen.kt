package com.practice.reelbreak.ui.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.practice.reelbreak.ui.component.GradientColor
import com.practice.reelbreak.ui.navigation.Routes
import com.practice.reelbreak.ui.permission.PermissionNudgeCard
import com.practice.reelbreak.viewmodel.DashboardViewModel
import com.practice.reelbreak.viewmodel.PermissionsViewModel
import kotlinx.coroutines.delay


@Composable
fun DashboardScreen(navController: NavController,
                    dashboardViewModel: DashboardViewModel = viewModel())
{
    val dashboardState by dashboardViewModel.uiState.collectAsState()
    var showPermissionNudge by remember { mutableStateOf(false) }

//    LaunchedEffect(Unit) {
//        delay(3000)
//        if (!permissionState.permissionGranted) showPermissionNudge = true
//    }

    Scaffold(
        bottomBar = {
            FloatingNavBar(
                selectedTab = dashboardState.selectedTab,
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
//              // Floating Permission Nudge
//              AnimatedVisibility(
//                  visible = showPermissionNudge && !permissionState.requiredGranted,
//                  enter = fadeIn(),
//                  exit = fadeOut(),
//                  modifier = Modifier
//                      .align(Alignment.BottomCenter)
//                      .padding(bottom = 100.dp) // Space for nav bar
//              ) {
//                  PermissionNudgeCard(
//                      onClick = { navController.navigate(Routes.PERMISSION) }
//                  )
//              }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                DashboardHeader(
                    userName = dashboardState.userName,
                    onVisibilityToggle = { dashboardViewModel.toggleCounterVisibility() },
                    onThemeToggle = { dashboardViewModel.toggleTheme() }
                )
                StatisticsCard(dashboardState)
                ActionGrid()
                Spacer(modifier = Modifier.height(100.dp)) // Space for the floating nav
            }

        }
      }
    }



