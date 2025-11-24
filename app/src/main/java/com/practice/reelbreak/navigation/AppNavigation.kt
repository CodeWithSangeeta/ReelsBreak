package com.practice.reelbreak.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.practice.reelbreak.HomeScreen
import com.practice.reelbreak.ui.onboarding.OnboardingScreen
import kotlin.math.round

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController= navController, startDestination = routes.OnBoarding, builder = {
        composable(routes.OnBoarding){
            OnboardingScreen(navController)
        }
        composable(routes.HOME){
            HomeScreen()
        }

    })
}