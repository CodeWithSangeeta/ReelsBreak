package com.practice.reelbreak.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.practice.reelbreak.ui.alert.AlertsScreen
import com.practice.reelbreak.ui.dashboard.AnalyticsScreen
import com.practice.reelbreak.ui.dashboard.DashboardScreen
import com.practice.reelbreak.ui.focused_mode.FocusScreen
import com.practice.reelbreak.ui.limit.LimitsScreen
import com.practice.reelbreak.ui.onboarding.OnboardingScreen
import com.practice.reelbreak.viewmodel.DashboardViewModel
import com.practice.reelbreak.viewmodel.MainViewModel
import com.practice.reelbreak.viewmodel.PermissionsViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String,
    mainViewModel: MainViewModel,
    dashboardViewModel: DashboardViewModel = viewModel(),
    permissionsViewModel: PermissionsViewModel = viewModel()
) {
    var selectedTab by remember { mutableIntStateOf(0) }

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


        composable(Routes.DASHBOARD) {
            DashboardScreen(
                navController = navController,
                dashboardViewModel = dashboardViewModel,
                permissionsViewModel = permissionsViewModel,
                selectedTab = selectedTab,
                onTabSelected = { index ->
                    selectedTab = index
                    when (index) {
                        0 -> navController.navigate(Routes.DASHBOARD) { popUpTo(0) { inclusive = true } }
                        1 -> navController.navigate(Routes.ANALYTICS) { popUpTo(0) { inclusive = true } }
                        2 -> navController.navigate(Routes.FOCUS) { popUpTo(0) { inclusive = true } }
                        3 -> navController.navigate(Routes.LIMITS) { popUpTo(0) { inclusive = true } }
                        4 -> navController.navigate(Routes.ALERTS) { popUpTo(0) { inclusive = true } }
                    }
                }
            )
        }

//        composable(Routes.ANALYTICS) {
//            AnalyticsScreen(
//                navController = navController,
//                selectedTab = selectedTab,
//                onTabSelected = { index ->
//                    selectedTab = index
//                    navController.navigate(Routes.ANALYTICS) { popUpTo(0) { inclusive = true } }
//                }
//            )
//        }
//        composable(Routes.ANALYTICS) {
//            AnalyticsScreen(
//                navController = navController,
//                selectedTab = selectedTab,
//                onTabSelected = { index ->
//                    selectedTab = index
//                    navController.navigate(Routes.ANALYTICS) { popUpTo(0) { inclusive = true } }
//                }
//            )
//        }
        composable(Routes.FOCUS) { FocusScreen() }
        composable(Routes.ANALYTICS) { AnalyticsScreen(navController) }
        composable(Routes.LIMITS) { LimitsScreen() }
        composable(Routes.ALERTS) { AlertsScreen() }
    }
}