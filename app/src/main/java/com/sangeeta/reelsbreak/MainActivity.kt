package com.sangeeta.reelsbreak

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sangeeta.reelsbreak.ui.theme.ReelBreakTheme
import com.sangeeta.reelsbreak.viewmodel.DashboardViewModel
import com.sangeeta.reelsbreak.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import com.sangeeta.reelsbreak.viewmodel.PermissionsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private val permissionsViewModel: PermissionsViewModel by viewModels()
    private var initialTab: Int = 0

    override fun onResume() {
        super.onResume()
        permissionsViewModel.refreshPermissionState(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        initialTab = intent?.getIntExtra("openTab", 0) ?: 0
        enableEdgeToEdge()

        lifecycleScope.launch {
            mainViewModel.preloadOnboardingStatus(this@MainActivity)
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val dashboardViewModel: DashboardViewModel = viewModel()
            val dashboardState by dashboardViewModel.uiState.collectAsState()

            ReelBreakTheme(isDarkMode = dashboardState.isDarkMode) {
                ReelsBreakApp(
                    mainViewModel = mainViewModel,
                    dashboardViewModel = dashboardViewModel,
                    initialTab = initialTab
                )
            }
        }

        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
        }
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        initialTab = intent.getIntExtra("openTab", 0)
    }
}