package com.practice.reelbreak.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.practice.reelbreak.ui.dashboard.DashboardScreen
import com.practice.reelbreak.ui.onboarding.OnboardingScreen
import com.practice.reelbreak.ui.permission.PermissionEducationScreen
import com.practice.reelbreak.viewmodel.MainViewModel

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

        composable(Routes.PERMISSION) {
            Log.d("NAV", "PERMISSION route entered")
            PermissionEducationScreen(
                viewModel = mainViewModel,
                onContinue = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.PERMISSION) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.DASHBOARD) {
           DashboardScreen()
        }
    }
}