package com.practice.reelbreak.ui.limit


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.practice.reelbreak.ui.theme.LocalAppColors
import com.practice.reelbreak.viewmodel.LimitsViewModel

@Composable
fun LimitSettingsContent(
    viewModel: LimitsViewModel = hiltViewModel()
) {
    val savedReelLimit by viewModel.dailyReelLimit.collectAsState(initial = 0)
    val savedTimeLimit by viewModel.dailyTimeLimitMinutes.collectAsState(initial = 0)

    var reelLimit by remember(savedReelLimit) { mutableStateOf(savedReelLimit) }
    var dailyTimeLimit by remember(savedTimeLimit) { mutableStateOf(savedTimeLimit) }
    val colors = LocalAppColors.current

    Column {
        // Reel limit slider
        LimitsSectionLabel(
            icon = Icons.Filled.VideoLibrary,
            title = "Reel Count Limit",
            subtitle = "Max reels per session before blocking"
        )
        Spacer(Modifier.height(12.dp))
        SliderCard(
            value = reelLimit.toFloat(),
            valueRange = 1f..50f,
            steps = 48,
            displayValue = "$reelLimit reels",
            trackColor = colors.purplePrimary,
            onValueChange = { reelLimit = it.toInt() }
        )

        Spacer(Modifier.height(24.dp))

        // Time limit slider
        LimitsSectionLabel(
            icon = Icons.Filled.AccessTime,
            title = "Daily Time Limit",
            subtitle = "Total reel-watching time per day"
        )
        Spacer(Modifier.height(12.dp))
        SliderCard(
            value = dailyTimeLimit.toFloat(),
            valueRange = 5f..120f,
            steps = 22,
            displayValue = "$dailyTimeLimit m",
            trackColor = colors.purplePrimary,
            onValueChange = { dailyTimeLimit = it.toInt() }
        )

        Spacer(Modifier.height(24.dp))

        SaveLimitsButton(
            onClick = {
                viewModel.saveLimits(
                    reelLimit = reelLimit,
                    timeLimitMinutes = dailyTimeLimit
                )
            }
        )
    }
}
