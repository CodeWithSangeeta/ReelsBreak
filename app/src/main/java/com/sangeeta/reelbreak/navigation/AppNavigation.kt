package com.practice.reelbreak.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.practice.reelbreak.HomeScreen
import com.practice.reelbreak.data.DataStoreManager
import com.practice.reelbreak.ui.onboarding.OnboardingScreen

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