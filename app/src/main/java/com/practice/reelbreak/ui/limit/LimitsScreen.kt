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
import androidx.navigation.NavController
import com.practice.reelbreak.ui.component.GradientColor
import com.practice.reelbreak.ui.component.MainScaffold

@Composable
fun LimitsScreen(
    navController: NavController,
    selectedTab: Int = 2,
    onTabSelected: (Int) -> Unit
) {
    // Local UI state (wire to ViewModel later)
    var reelLimit by remember { mutableIntStateOf(10) }
    var dailyTimeLimit by remember { mutableIntStateOf(30) }
    var cooldownMinutes by remember { mutableIntStateOf(5) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var strictModeEnabled by remember { mutableStateOf(false) }
    var weekendModeEnabled by remember { mutableStateOf(true) }

    MainScaffold(
        selectedTab = selectedTab,
        onTabSelected = onTabSelected
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = GradientColor.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 100.dp)
            ) {
                // ── Header ─────────────────────────────────────────────────
                LimitsHeader()

                Spacer(modifier = Modifier.height(8.dp))

                // ── Reel Count Limit ───────────────────────────────────────
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
                    trackColor = GradientColor.PurplePrimary,
                    onValueChange = { reelLimit = it.toInt() }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ── Daily Time Limit ───────────────────────────────────────
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
                    trackColor = GradientColor.BlueAccent,
                    onValueChange = { dailyTimeLimit = it.toInt() }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ── Cooldown Period ────────────────────────────────────────
                LimitsSectionLabel(
                    icon = Icons.Filled.HourglassBottom,
                    title = "Cooldown Period",
                    subtitle = "Wait time after a block is triggered"
                )
                Spacer(modifier = Modifier.height(12.dp))
                SliderCard(
                    value = cooldownMinutes.toFloat(),
                    valueRange = 1f..30f,
                    steps = 28,
                    displayValue = "${cooldownMinutes}m cooldown",
                    trackColor = GradientColor.SuccessGreen,
                    onValueChange = { cooldownMinutes = it.toInt() }
                )

                Spacer(modifier = Modifier.height(28.dp))

                // ── Toggle Settings ────────────────────────────────────────
                LimitsSectionLabel(
                    icon = Icons.Filled.Tune,
                    title = "Preferences",
                    subtitle = "Customize your guard behavior"
                )
                Spacer(modifier = Modifier.height(12.dp))

                ToggleCard(
                    icon = Icons.Filled.Notifications,
                    iconTint = GradientColor.PurpleSoft,
                    title = "Notifications",
                    subtitle = "Alert me when limit is reached",
                    isEnabled = notificationsEnabled,
                    onToggle = { notificationsEnabled = it }
                )
                Spacer(modifier = Modifier.height(10.dp))
                ToggleCard(
                    icon = Icons.Filled.Lock,
                    iconTint = GradientColor.ErrorRed,
                    title = "Strict Mode",
                    subtitle = "Block immediately, no grace period",
                    isEnabled = strictModeEnabled,
                    onToggle = { strictModeEnabled = it }
                )
                Spacer(modifier = Modifier.height(10.dp))
                ToggleCard(
                    icon = Icons.Filled.Weekend,
                    iconTint = GradientColor.WarningOrange,
                    title = "Relax on Weekends",
                    subtitle = "Double the limit on Sat & Sun",
                    isEnabled = weekendModeEnabled,
                    onToggle = { weekendModeEnabled = it }
                )

                Spacer(modifier = Modifier.height(28.dp))

                // ── Save Button ────────────────────────────────────────────
                SaveSettingsButton(onClick = { /* wire to ViewModel later */ })

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

