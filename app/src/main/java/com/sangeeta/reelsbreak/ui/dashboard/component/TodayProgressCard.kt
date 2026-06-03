package com.sangeeta.reelsbreak.ui.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.reelsbreak.ui.dashboard.DashboardHomeUiState
import com.sangeeta.reelsbreak.ui.theme.LocalAppColors
import kotlin.math.min

@Composable
fun TodayProgressCard(
    state: DashboardHomeUiState,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val progress = progressValue(state)

    SurfaceCard(
        modifier = modifier.fillMaxWidth(),
        innerPadding = androidx.compose.foundation.layout.PaddingValues(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            SectionHeader(
                title = "Today",
                subtitle = motivationalMessage(state)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ProgressMetric(
                    title = "Reels blocked",
                    value = state.reelsClosedToday.toString()
                )
                ProgressMetric(
                    title = "Time saved",
                    value = "${state.timeBackTodayMinutes} min"
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .background(
                            color = if (colors.isDark) Color.White.copy(alpha = 0.06f)
                            else Color.Black.copy(alpha = 0.05f),
                            shape = RoundedCornerShape(999.dp)
                        ),
                    color = colors.successGreen,
                    trackColor = if (colors.isDark) {
                        Color.White.copy(alpha = 0.08f)
                    } else {
                        Color.Black.copy(alpha = 0.06f)
                    }
                )
                Text(
                    text = progressFooter(state),
                    color = colors.textMuted,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun ProgressMetric(
    title: String,
    value: String
) {
    val colors = LocalAppColors.current
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            color = colors.textSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            color = colors.textPrimary,
            fontSize = 24.sp,
            lineHeight = 28.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun progressValue(state: DashboardHomeUiState): Float {
    val combined = state.reelsClosedToday + state.timeBackTodayMinutes
    return min(1f, combined / 120f)
}

private fun motivationalMessage(state: DashboardHomeUiState): String {
    return when {
        state.reelsClosedToday >= 25 -> "Strong focus today — keep the momentum going."
        state.reelsClosedToday >= 10 -> "You are protecting your attention well today."
        state.timeBackTodayMinutes >= 15 -> "Small interruptions avoided are adding up."
        else -> "A calmer feed starts with a few intentional decisions."
    }
}

private fun progressFooter(state: DashboardHomeUiState): String {
    return when {
        state.selectedMode.name == "CURIOUS" ->
            "Curious Mode is helping you stay intentional."
        state.selectedMode.name == "PAUSED" ->
            "Protection is paused right now."
        else ->
            "Flow Mode is actively protecting your focus."
    }
}