package com.sangeeta.reelsbreak

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.sangeeta.reelsbreak.ui.navigation.AppNavigation
import com.sangeeta.reelsbreak.ui.navigation.DashboardKey
import com.sangeeta.reelsbreak.ui.navigation.OnboardingKey
import com.sangeeta.reelsbreak.ui.theme.LocalAppColors
import com.sangeeta.reelsbreak.viewmodel.DashboardViewModel
import com.sangeeta.reelsbreak.viewmodel.MainViewModel

@Composable
fun ReelsBreakApp(
    mainViewModel: MainViewModel,
    dashboardViewModel: DashboardViewModel,
    initialTab: Int = 0
) {
    val isOnboardingCompleted by mainViewModel.isOnboardingCompleted.collectAsState()
    val colors = LocalAppColors.current

    when (isOnboardingCompleted) {
        null -> Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
        )

        false -> AppNavigation(
            startDestination = OnboardingKey,
            mainViewModel = mainViewModel,
            dashboardViewModel = dashboardViewModel,
            initialTab = 0
        )

        true -> AppNavigation(
            startDestination = DashboardKey,
            mainViewModel = mainViewModel,
            dashboardViewModel = dashboardViewModel,
            initialTab = initialTab
        )
    }
}