package com.practice.reelbreak

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.practice.reelbreak.ui.navigation.AppNavigation
import com.practice.reelbreak.ui.navigation.Routes
import com.practice.reelbreak.ui.theme.LocalAppColors
import com.practice.reelbreak.viewmodel.DashboardViewModel
import com.practice.reelbreak.viewmodel.MainViewModel

@Composable
fun ReelsBreakApp(
    mainViewModel: MainViewModel,
    dashboardViewModel: DashboardViewModel
) {
    val isOnboardingCompleted by mainViewModel.isOnboardingCompleted.collectAsState()
    val navController = rememberNavController()
    val colors = LocalAppColors.current

    when (isOnboardingCompleted) {
        null -> Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = colors.background)
        )
        false -> AppNavigation(
            navController = navController,
            startDestination = Routes.ONBOARDING,
            mainViewModel = mainViewModel,
            dashboardViewModel = dashboardViewModel
        )
        true -> AppNavigation(
            navController = navController,
            startDestination = Routes.DASHBOARD,
            mainViewModel = mainViewModel,
            dashboardViewModel = dashboardViewModel
        )
    }
}