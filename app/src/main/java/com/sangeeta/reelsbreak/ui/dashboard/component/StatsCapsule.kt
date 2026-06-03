package com.sangeeta.reelsbreak.ui.dashboard.component


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material.icons.outlined.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.reelsbreak.ui.dashboard.DashboardHomeUiState
import com.sangeeta.reelsbreak.ui.theme.LocalAppColors
import kotlin.math.max

@Composable
fun StatsCapsule(
    state: DashboardHomeUiState,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val streak = estimatedStreak(state)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 78.dp)
            .background(
                color = if (colors.isDark) {
                    Color.White.copy(alpha = 0.04f)
                } else {
                    Color.White.copy(alpha = 0.72f)
                },
                shape = RoundedCornerShape(24.dp)
            )
            .border(
                width = 1.dp,
                color = colors.borderSubtle.copy(alpha = if (colors.isDark) 0.42f else 0.14f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MetricItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Bolt,
                    contentDescription = "Time saved",
                    tint = colors.successGreen
                )
            },
            value = "${state.timeBackTodayMinutes}m",
            label = "Saved",
            modifier = Modifier.weight(1f)
        )

        CapsuleDivider()

        MetricItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.PlayCircleOutline,
                    contentDescription = "Reels closed",
                    tint = colors.purplePrimary
                )
            },
            value = state.reelsClosedToday.toString(),
            label = "Closed",
            modifier = Modifier.weight(1f)
        )

        CapsuleDivider()

        MetricItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Whatshot,
                    contentDescription = "Streak",
                    tint = colors.warningOrange
                )
            },
            value = "🔥$streak",
            label = "Streak",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun MetricItem(
    icon: @Composable RowScope.() -> Unit,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(horizontal = 3.dp))
        Text(
            text = value,
            color = colors.textPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(horizontal = 2.dp))
        Text(
            text = label,
            color = colors.textMuted,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun CapsuleDivider() {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .background(
                colors.borderSubtle.copy(alpha = if (colors.isDark) 0.30f else 0.10f),
                RoundedCornerShape(999.dp)
            )
            .fillMaxWidth(0.002f)
            .heightIn(min = 26.dp)
    )
}

private fun estimatedStreak(state: DashboardHomeUiState): Int {
    return max(1, state.reelsClosedToday / 6)
}