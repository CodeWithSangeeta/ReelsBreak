package com.practice.reelbreak


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.practice.reelbreak.ui.navigation.AppNavigation
import com.practice.reelbreak.ui.navigation.Routes
import com.practice.reelbreak.ui.theme.ReelsBreakTheme
import com.practice.reelbreak.viewmodel.MainViewModel
import com.practice.reelbreak.viewmodel.PermissionsViewModel

@Composable
fun ReelsBreakApp(mainViewModel: MainViewModel) {
    val isOnboardingCompleted by mainViewModel
        .isOnboardingCompleted
        .collectAsState()
    val permissionsViewModel: PermissionsViewModel = viewModel()
    val navController = rememberNavController()

    val startDestination = when {
        !isOnboardingCompleted -> Routes.ONBOARDING
        else -> Routes.DASHBOARD
    }


    AppNavigation(
        navController = navController,
        startDestination = startDestination,
        mainViewModel = mainViewModel,
        permissionsViewModel = permissionsViewModel
    )

}
