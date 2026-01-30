package com.sangeeta.reelbreak.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sangeeta.reelbreak.ui.onboarding.OnboardingScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.ONBOARDING) {
            OnboardingScreen()
        }

        composable(Routes.DASHBOARD) {
          //  DashboardScreen()
        }
    }
}