package com.practice.reelbreak.ui.limit


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.practice.reelbreak.ui.theme.LocalAppColors
import com.practice.reelbreak.viewmodel.LimitsViewModel


import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.PauseCircle
import androidx.compose.material.icons.outlined.TipsAndUpdates
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun LimitSettingsCntent(
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
















