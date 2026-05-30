package com.sangeeta.reelsbreak.ui.dashboard.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.TipsAndUpdates
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.reelsbreak.ui.dashboard.DashboardHomeUiState
import com.sangeeta.reelsbreak.ui.dashboard.MindfulResetPeriod
import com.sangeeta.reelsbreak.ui.theme.LocalAppColors
import kotlin.math.roundToInt

@Composable
fun DefaultModeDetailsCard(
    isActuallyActive: Boolean
) {
    val colors = LocalAppColors.current

    SurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        innerPadding = PaddingValues(18.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Default Mode",
                    color = colors.textPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                AnimatedVisibility(visible = isActuallyActive) {
                    SmallBadge(
                        text = "Active",
                        color = colors.successGreen
                    )
                }
            }

            Text(
                text = "Auto-closes short-video screens as soon as they open.",
                color = colors.textSecondary,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )

            HorizontalDivider(color = colors.borderSubtle.copy(alpha = 0.8f), thickness = 1.dp)

            FeatureRow(
                icon = Icons.Outlined.Bolt,
                title = "Auto-close reels instantly",
                tint = colors.successGreen
            )

            FeatureRow(
                icon = Icons.Outlined.CheckCircle,
                title = "Best for strict focus",
                tint = colors.successGreen
            )

            HorizontalDivider(color = colors.borderSubtle.copy(alpha = 0.8f), thickness = 1.dp)

            Text(
                text = "When a reel or short-video screen is detected, ReelBreak navigates away automatically. It keeps the behavior simple and predictable.",
                color = colors.textMuted,
                fontSize = 12.sp,
                lineHeight = 19.sp
            )
        }
    }
}

@Composable
 fun PauseModeDetailsCard(
    isProtectionEnabled: Boolean
) {
    val colors = LocalAppColors.current

    SurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        innerPadding = PaddingValues(18.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Pause",
                color = colors.textPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = if (isProtectionEnabled) {
                    "Protection is on, but currently paused. Use this when you want a short break without changing your saved setup."
                } else {
                    "Protection is currently off. You can switch back to Default or Mindful any time."
                },
                color = colors.textSecondary,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )

            HorizontalDivider(color = colors.borderSubtle.copy(alpha = 0.8f), thickness = 1.dp)

            FeatureRow(
                icon = Icons.Outlined.Pause,
                title = "Temporarily relaxes protection",
                tint = colors.warningOrange
            )

            FeatureRow(
                icon = Icons.Outlined.TipsAndUpdates,
                title = "Keeps your settings ready for later",
                tint = colors.warningOrange
            )
        }
    }
}

@Composable
fun MindfulModeDetailsCard(
    state: DashboardHomeUiState,
    onCountToggle: (Boolean) -> Unit,
    onTimeToggle: (Boolean) -> Unit,
    onReelsLimitChange: (Int) -> Unit,
    onTimeLimitChange: (Int) -> Unit,
    onPeriodChange: (MindfulResetPeriod) -> Unit
) {
    val colors = LocalAppColors.current

    SurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        innerPadding = PaddingValues(18.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Mindful Mode",
                color = colors.textPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Watch intentionally with self-set boundaries.",
                color = colors.textSecondary,
                fontSize = 13.sp
            )

            HorizontalDivider(color = colors.borderSubtle.copy(alpha = 0.8f), thickness = 1.dp)

            MindfulToggleSliderBlock(
                title = "Limit by reels count",
                valueLabel = "${state.mindfulReelsLimit} reels",
                enabled = state.mindfulCountEnabled,
                accent = colors.successGreen,
                sliderValue = state.mindfulReelsLimit.toFloat(),
                valueRange = 1f..100f,
                steps = 98,
                onToggle = onCountToggle,
                onValueChange = { onReelsLimitChange(it.roundToInt()) }
            )

            MindfulToggleSliderBlock(
                title = "Limit by watch time",
                valueLabel = "${state.mindfulTimeLimitMinutes} min",
                enabled = state.mindfulTimeEnabled,
                accent = colors.blueAccent,
                sliderValue = state.mindfulTimeLimitMinutes.toFloat(),
                valueRange = 5f..180f,
                steps = 34,
                onToggle = onTimeToggle,
                onValueChange = { onTimeLimitChange(it.roundToInt()) }
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Reset period",
                    color = colors.textMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PeriodChip(
                        text = "Per hour",
                        selected = state.mindfulResetPeriod == MindfulResetPeriod.HOUR,
                        accent = colors.successGreen,
                        onClick = { onPeriodChange(MindfulResetPeriod.HOUR) }
                    )
                    PeriodChip(
                        text = "Per day",
                        selected = state.mindfulResetPeriod == MindfulResetPeriod.DAY,
                        accent = colors.blueAccent,
                        onClick = { onPeriodChange(MindfulResetPeriod.DAY) }
                    )
                }
            }

            HorizontalDivider(color = colors.borderSubtle.copy(alpha = 0.8f), thickness = 1.dp)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = colors.successGreen,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = buildMindfulSummary(state),
                    color = colors.textSecondary,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}



@Composable
private fun FeatureRow(
    icon: ImageVector,
    title: String,
    tint: Color
) {
    val colors = LocalAppColors.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = title,
            color = colors.textPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun PeriodChip(
    text: String,
    selected: Boolean,
    accent: Color,
    onClick: () -> Unit
) {
    val colors = LocalAppColors.current

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (selected) accent.copy(alpha = if (colors.isDark) 0.20f else 0.12f)
                else Color.White.copy(alpha = if (colors.isDark) 0.04f else 0.55f)
            )
            .border(
                width = 1.dp,
                color = if (selected) accent.copy(alpha = 0.42f) else colors.borderSubtle,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = text,
            color = if (selected) accent else colors.textSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}


@Composable
private fun SmallBadge(
    text: String,
    color: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(color.copy(alpha = 0.14f))
            .border(1.dp, color.copy(alpha = 0.40f), RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}


@Composable
private fun MindfulToggleSliderBlock(
    title: String,
    valueLabel: String,
    enabled: Boolean,
    accent: Color,
    sliderValue: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    onToggle: (Boolean) -> Unit,
    onValueChange: (Float) -> Unit
) {
    val colors = LocalAppColors.current

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = colors.textPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = enabled,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = accent,
                    checkedTrackColor = accent.copy(alpha = 0.38f),
                    uncheckedThumbColor = if (colors.isDark) Color.White.copy(alpha = 0.56f) else Color.White,
                    uncheckedTrackColor = colors.switchTrackOff
                )
            )
        }

        AnimatedVisibility(
            visible = enabled,
            enter = fadeIn(tween(220)),
            exit = fadeOut(tween(120))
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = valueLabel,
                    color = colors.textPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Slider(
                    value = sliderValue,
                    onValueChange = onValueChange,
                    valueRange = valueRange,
                    steps = steps,
                    colors = androidx.compose.material3.SliderDefaults.colors(
                        thumbColor = accent,
                        activeTrackColor = accent,
                        inactiveTrackColor = accent.copy(alpha = 0.18f)
                    )
                )
            }
        }
    }
}


private fun buildMindfulSummary(state: DashboardHomeUiState): String {
    val parts = buildList {
        if (state.mindfulCountEnabled) add("${state.mindfulReelsLimit} reels")
        if (state.mindfulTimeEnabled) add("${state.mindfulTimeLimitMinutes} min")
    }

    val base = if (parts.isEmpty()) "No limit selected" else parts.joinToString(" and ")
    val period = if (state.mindfulResetPeriod == MindfulResetPeriod.HOUR) "per hour" else "per day"
    return "Limit: $base $period"
}