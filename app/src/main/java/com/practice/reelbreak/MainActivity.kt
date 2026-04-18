package com.practice.reelbreak

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practice.reelbreak.ui.theme.ReelBreakTheme
import com.practice.reelbreak.viewmodel.DashboardViewModel
import com.practice.reelbreak.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch {
            mainViewModel.preloadOnboardingStatus(this@MainActivity)
        }

        setContent {
            val dashboardViewModel: DashboardViewModel = viewModel()
            val dashboardState by dashboardViewModel.uiState.collectAsState()

            ReelBreakTheme(isDarkMode = dashboardState.isDarkMode) {
                ReelsBreakApp(
                    mainViewModel = mainViewModel,
                    dashboardViewModel = dashboardViewModel
                )
            }
        }
    }
}