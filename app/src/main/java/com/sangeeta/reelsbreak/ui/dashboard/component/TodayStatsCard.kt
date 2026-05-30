package com.sangeeta.reelsbreak.ui.dashboard.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.reelsbreak.ui.dashboard.DashboardHomeUiState
import com.sangeeta.reelsbreak.ui.dashboard.HomeProtectionMode
import com.sangeeta.reelsbreak.ui.theme.LocalAppColors

@Composable
fun TodayStatsCard(
    state: DashboardHomeUiState
) {
    val colors = LocalAppColors.current
    val showMindfulColumn = state.selectedMode == HomeProtectionMode.MINDFUL && state.isProtectionEnabled
    val isEmpty = !state.isProtectionEnabled && state.reelsClosedToday == 0 && state.timeBackTodayMinutes == 0

    SurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        innerPadding = PaddingValues(18.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "TODAY",
                color = colors.textMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                InsightStat(
                    modifier = Modifier.weight(1f),
                    value = state.reelsClosedToday.toString(),
                    label = "Reels closed",
                    accent = colors.successGreen
                )

                VerticalDivider()

                InsightStat(
                    modifier = Modifier.weight(1f),
                    value = "${state.timeBackTodayMinutes}m",
                    label = "Time back",
                    accent = colors.blueAccent
                )

                if (showMindfulColumn) {
                    VerticalDivider()
                    InsightStat(
                        modifier = Modifier.weight(1f),
                        value = mindfulRemainingShort(state),
                        label = "Remaining",
                        accent = colors.warningOrange
                    )
                }
            }

            HorizontalDivider(color = colors.borderSubtle.copy(alpha = 0.8f), thickness = 1.dp)

            Text(
                text = if (isEmpty) {
                    "Turn on protection to start tracking your progress."
                } else if (showMindfulColumn) {
                    "Your mindful limits update through the day as you use supported short-video feeds."
                } else {
                    "Your dashboard updates as ReelBreak closes supported short-video screens."
                },
                color = colors.textMuted,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        }
    }
}


@Composable
private fun InsightStat(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    accent: Color
) {
    val colors = LocalAppColors.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        AnimatedContent(
            targetState = value,
            transitionSpec = {
                fadeIn(tween(180)) togetherWith fadeOut(tween(120))
            },
            label = "insightValue"
        ) { target ->
            Text(
                text = target,
                color = accent,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = label,
            color = colors.textSecondary,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}



