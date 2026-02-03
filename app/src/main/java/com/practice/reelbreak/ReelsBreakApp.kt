package com.sangeeta.reelbreak


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.sangeeta.reelbreak.ui.navigation.AppNavigation
import com.sangeeta.reelbreak.ui.navigation.Routes
import com.sangeeta.reelbreak.viewmodel.MainViewModel

@Composable
fun ReelsBreakApp(mainViewModel: MainViewModel) {

    val isOnboardingCompleted by mainViewModel
        .isOnboardingCompleted
        .collectAsState()

    val navController = rememberNavController()

    val startDestination = remember {
        if (isOnboardingCompleted) {
            Routes.DASHBOARD
        } else {
            Routes.ONBOARDING
        }
    }


    AppNavigation(
        navController = navController,
        startDestination = startDestination,
        mainViewModel = mainViewModel
    )
}
