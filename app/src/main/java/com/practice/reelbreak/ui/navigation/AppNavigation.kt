package com.practice.reelbreak.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.practice.reelbreak.ui.alert.AlertsScreen
import com.practice.reelbreak.ui.dashboard.AnalyticsScreen
import com.practice.reelbreak.ui.dashboard.DashboardScreen
import com.practice.reelbreak.ui.focused_mode.FocusScreen
import com.practice.reelbreak.ui.limit.LimitsScreen
import com.practice.reelbreak.ui.onboarding.OnboardingScreen
import com.practice.reelbreak.viewmodel.MainViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String,
    mainViewModel: MainViewModel,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onComplete = {
                    mainViewModel.completeOnboarding()
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                },
                onSkip = {
                    mainViewModel.completeOnboarding()
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.DASHBOARD) { DashboardScreen(navController) }
      //  composable(Routes.PERMISSION) { PermissionScreen(navController) }
        composable(Routes.FOCUS) { FocusScreen() }
        composable(Routes.ANALYTICS) { AnalyticsScreen(navController) }
        composable(Routes.LIMITS) { LimitsScreen() }
        composable(Routes.ALERTS) { AlertsScreen() }
    }
}