package com.sangeeta.reelbreak

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.sangeeta.reelbreak.data.DataStoreManager
import com.sangeeta.reelbreak.navigation.AppNavigation
import com.sangeeta.reelbreak.ui.theme.ReelBreakTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReelBreakTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    ReelsBreakApp(mainViewModel)









                    val dataStore = DataStoreManager(this)

                    val onboardingState by dataStore.isOnboardingCompleted.collectAsState(initial = null)

                    Scaffold(modifier = Modifier.fillMaxSize()) { _ ->

                        when (onboardingState) {
                            null -> {
                                // 🔹 2) In-app splash while DataStore loading
                                SplashScreen()
                            }

                            true -> {
                                AppNavigation(isOnboardingDone = true, dataStore = dataStore)
                            }

                            false -> {
                                AppNavigation(isOnboardingDone = false, dataStore = dataStore)
                            }
                        }
                    }

                }
            }
        }
    }
}

