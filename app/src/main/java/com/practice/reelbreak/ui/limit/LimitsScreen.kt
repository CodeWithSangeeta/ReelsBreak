package com.practice.reelbreak.ui.limit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.practice.reelbreak.ui.component.MainScaffold
import com.practice.reelbreak.ui.theme.LocalAppColors
import com.practice.reelbreak.viewmodel.LimitsViewModel


@Composable
fun LimitsScreen(
    navController: NavController,
    selectedTab: Int = 2,
    onTabSelected: (Int) -> Unit,
    viewModel: LimitsViewModel = viewModel()
) {
    val colors = LocalAppColors.current
    // Read current saved values from ViewModel (one-time initial values)
    val savedReelLimit by viewModel.dailyReelLimit.collectAsState()
    val savedTimeLimit by viewModel.dailyTimeLimitMinutes.collectAsState()
    val isStrictMode by viewModel.isStrictMode.collectAsState()

    // Local mutable state for sliders — user can drag freely
    // 'by remember(savedReelLimit)' means: when the saved value changes
    // (e.g. first load from DataStore), sync the local state to it
    var reelLimit by remember(savedReelLimit) { mutableIntStateOf(savedReelLimit) }
    var dailyTimeLimit by remember(savedTimeLimit) { mutableIntStateOf(savedTimeLimit) }



    MainScaffold(
        selectedTab = selectedTab,
        onTabSelected = onTabSelected
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = colors.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 100.dp)
            ) {


                LimitsSectionLabel(
                    icon = Icons.Filled.VideoLibrary,
                    title = "Reel Count Limit",
                    subtitle = "Max reels per session before blocking"
                )
                Spacer(modifier = Modifier.height(12.dp))
                SliderCard(
                    value = reelLimit.toFloat(),
                    valueRange = 1f..50f,
                    steps = 48,
                    displayValue = "$reelLimit reels",
                    trackColor = colors.purplePrimary,
                    onValueChange = { reelLimit = it.toInt() }  // ✅ local var, no error
                )

                Spacer(modifier = Modifier.height(24.dp))

                LimitsSectionLabel(
                    icon = Icons.Filled.AccessTime,
                    title = "Daily Time Limit",
                    subtitle = "Total reel-watching time per day"
                )
                Spacer(modifier = Modifier.height(12.dp))
                SliderCard(
                    value = dailyTimeLimit.toFloat(),
                    valueRange = 5f..120f,
                    steps = 22,
                    displayValue = "${dailyTimeLimit}m",
                    trackColor = colors.blueAccent,
                    onValueChange = { dailyTimeLimit = it.toInt() }  // ✅ local var
                )

                Spacer(modifier = Modifier.height(28.dp))

                LimitsSectionLabel(
                    icon = Icons.Filled.Tune,
                    title = "Preferences",
                    subtitle = "Customize your guard behavior"
                )

            }
        }
    }
}