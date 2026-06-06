package com.sangeeta.reelsbreak.ui.dashboard.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import kotlin.math.min

@Composable
fun TodayProgressCard(
    state: DashboardHomeUiState,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val showMindfulColumn =
        state.selectedMode == HomeProtectionMode.CURIOUS && state.isProtectionEnabled

    val progress = todayProgressValue(state)

    val innerPanelBg = if (colors.isDark) {
        colors.purplePrimary.copy(alpha = 0.08f)
    } else {
        colors.purplePrimary.copy(alpha = 0.06f)
    }

    val streakBg = if (colors.isDark) {
        colors.purplePrimary.copy(alpha = 0.16f)
    } else {
        colors.purplePrimary.copy(alpha = 0.10f)
    }

    val progressTrack = if (colors.isDark) {
        Color.White.copy(alpha = 0.08f)
    } else {
        Color.Black.copy(alpha = 0.06f)
    }

    SurfaceCard(
        modifier = modifier.fillMaxWidth(),
        innerPadding = PaddingValues(18.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(
                                color = if (colors.isDark) {
                                    colors.purplePrimary.copy(alpha = 0.16f)
                                } else {
                                    colors.purplePrimary.copy(alpha = 0.10f)
                                },
                                shape = CircleShape
                            )
                            .border(
                                width = 1.dp,
                                color = colors.borderSubtle.copy(alpha = if (colors.isDark) 0.36f else 0.16f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarToday,
                            contentDescription = null,
                            tint = colors.purplePrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "Today",
                            color = colors.textPrimary,
                            fontSize = 24.sp,
                            lineHeight = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = progressSubtitle(state),
                            color = colors.textSecondary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .background(
                            color = streakBg,
                            shape = RoundedCornerShape(999.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = colors.borderSubtle.copy(alpha = if (colors.isDark) 0.34f else 0.14f),
                            shape = RoundedCornerShape(999.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocalFireDepartment,
                            contentDescription = null,
                            tint = colors.warningOrange,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "${dayStreak(state)} Day Streak",
                            color = colors.textPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = innerPanelBg,
                        shape = RoundedCornerShape(22.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = colors.borderSubtle.copy(alpha = if (colors.isDark) 0.28f else 0.10f),
                        shape = RoundedCornerShape(22.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                InsightStat(
                    modifier = Modifier.weight(1f),
                    value = state.reelsClosedToday.toString(),
                    label = "Reels blocked",
                    accent = colors.successGreen
                )

                VerticalDivider()

                InsightStat(
                    modifier = Modifier.weight(1f),
                    value = "${state.timeBackTodayMinutes}m",
                    label = "Time saved",
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
                } else {
                    VerticalDivider()
                    InsightStat(
                        modifier = Modifier.weight(1f),
                        value = dayStreak(state).toString(),
                        label = "Day streak",
                        accent = colors.warningOrange
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Today's progress",
                        color = colors.textPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )

                    Box(
                        modifier = Modifier
                            .background(
                                color = if (colors.isDark) {
                                    colors.purplePrimary.copy(alpha = 0.18f)
                                } else {
                                    colors.purplePrimary.copy(alpha = 0.12f)
                                },
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            color = colors.purplePrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp),
                    color = colors.purplePrimary,
                    trackColor = progressTrack
                )

                HorizontalDivider(
                    color = colors.borderSubtle.copy(alpha = if (colors.isDark) 0.22f else 0.10f),
                    thickness = 1.dp
                )

                Text(
                    text = progressFooter(state),
                    color = colors.textMuted,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }
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
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        AnimatedContent(
            targetState = value,
            transitionSpec = {
                fadeIn(tween(180)) togetherWith fadeOut(tween(120))
            },
            label = "todayInsightValue"
        ) { target ->
            Text(
                text = target,
                color = accent,
                fontSize = 30.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = label,
            color = colors.textSecondary,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

private fun todayProgressValue(state: DashboardHomeUiState): Float {
    val combined = state.reelsClosedToday + state.timeBackTodayMinutes
    return min(1f, combined / 120f)
}

private fun progressSubtitle(state: DashboardHomeUiState): String {
    return when {
        !state.isProtectionEnabled -> "Protection is currently off."
        state.selectedMode == HomeProtectionMode.CURIOUS -> "Small choices. Big change."
        state.reelsClosedToday >= 15 -> "Strong focus today."
        state.timeBackTodayMinutes >= 20 -> "Your attention is adding up."
        else -> "Your progress updates through the day."
    }
}

private fun progressFooter(state: DashboardHomeUiState): String {
    return when {
        !state.isProtectionEnabled ->
            "Turn on protection to start tracking your progress."
        state.selectedMode == HomeProtectionMode.CURIOUS ->
            "Curious Mode is helping you stay intentional."
        state.selectedMode == HomeProtectionMode.PAUSED ->
            "Protection is paused right now."
        else ->
            "Flow Mode is actively protecting your focus."
    }
}

private fun dayStreak(state: DashboardHomeUiState): Int {
    return when {
        state.reelsClosedToday >= 25 -> 7
        state.reelsClosedToday >= 15 -> 5
        state.reelsClosedToday >= 8 -> 3
        state.reelsClosedToday > 0 || state.timeBackTodayMinutes > 0 -> 1
        else -> 0
    }
}



private fun mindfulRemainingShort(state: DashboardHomeUiState): String {
    val pieces = buildList {
        if (state.curiousCountEnabled) add("${state.curiousRemainingCount} reels")
        if (state.curiousTimeEnabled) add("${state.curiousRemainingMinutes}m")
    }
    return pieces.ifEmpty { listOf("—") }.joinToString(" • ")
}



@Composable
fun VerticalDivider() {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .height(44.dp)
            .width(1.dp)
            .background(if (colors.isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.08f))
    )
}