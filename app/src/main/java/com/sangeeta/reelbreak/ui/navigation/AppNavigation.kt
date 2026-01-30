package com.sangeeta.reelbreak.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sangeeta.reelbreak.ui.dashboard.DashboardScreen
import com.sangeeta.reelbreak.ui.onboarding.OnboardingScreen
import com.sangeeta.reelbreak.viewmodel.MainViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String,
    mainViewModel: MainViewModel
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                navController = navController,
                mainViewModel = mainViewModel
            )
        }

        composable(Routes.DASHBOARD) {
           DashboardScreen()
        }
    }
}