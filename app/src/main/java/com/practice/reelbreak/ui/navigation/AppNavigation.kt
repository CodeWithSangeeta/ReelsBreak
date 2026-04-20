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
import com.practice.reelbreak.ui.dashboard.DashboardScreen
import com.practice.reelbreak.ui.focusedmode.FocusScreen
import com.practice.reelbreak.ui.limit.LimitsScreen
import com.practice.reelbreak.ui.onboarding.OnboardingScreen
import com.practice.reelbreak.ui.settings.SettingsScreen
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
                        1 -> navController.navigate(Routes.FOCUS) {
                            popUpTo(Routes.DASHBOARD) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        2 -> navController.navigate(Routes.LIMITS) {
                            popUpTo(Routes.DASHBOARD) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }

        // Tab 1 → Focus/Guard
        composable(Routes.FOCUS) {
            FocusScreen(
                navController = navController,
                selectedTab = selectedTab,
                onTabSelected = { index ->
                    selectedTab = index
                    when (index) {
                        0 -> navController.navigate(Routes.DASHBOARD) {
                            popUpTo(Routes.DASHBOARD) { inclusive = true }
                        }
                        2 -> navController.navigate(Routes.LIMITS) {
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

//        // Tab 2 → Settings/Limits
//        composable(Routes.LIMITS) {
//            LimitsScreen(
//                navController = navController,
//                selectedTab = selectedTab,
//                onTabSelected = { index ->
//                    selectedTab = index
//                    when (index) {
//                        0 -> navController.navigate(Routes.DASHBOARD) {
//                            popUpTo(Routes.DASHBOARD) { inclusive = true }
//                        }
//                        1 -> navController.navigate(Routes.FOCUS) {
//                            launchSingleTop = true
//                        }
//                    }
//                }
//            )
//        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                navController = navController,
                selectedTab = 2,
                onTabSelected = { index ->
                    selectedTab = index
                    when (index) {
                        0 -> navController.navigate(Routes.DASHBOARD) {
                            popUpTo(Routes.DASHBOARD) { inclusive = true }
                        }

                        1 -> navController.navigate(Routes.FOCUS) {
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}