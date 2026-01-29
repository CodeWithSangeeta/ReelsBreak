package com.sangeeta.reelbreak.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sangeeta.reelbreak.HomeScreen
import com.sangeeta.reelbreak.data.DataStoreManager
import com.sangeeta.reelbreak.ui.onboarding.OnboardingScreen

@Composable
fun AppNavigation(isOnboardingDone: Boolean,dataStore : DataStoreManager) {
    val navController = rememberNavController()
    NavHost(
        navController= navController,
        startDestination = if(isOnboardingDone) routes.HOME else routes.OnBoarding,
        builder = {
        composable(routes.OnBoarding){
            OnboardingScreen(navController,dataStore)
        }
        composable(routes.HOME){
            HomeScreen()
        }

    })
}