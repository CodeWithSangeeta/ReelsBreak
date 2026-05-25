package com.practice.reelbreak.ui.dashboard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.QueryStats
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Timelapse
import androidx.compose.material.icons.outlined.TipsAndUpdates
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.TableInfo
import com.practice.reelbreak.ui.theme.LocalAppColors
import kotlin.math.roundToInt

enum class HomeProtectionMode {
    DEFAULT, PAUSED, MINDFUL
}

enum class MindfulResetPeriod {
    HOUR, DAY
}

@Immutable
data class DashboardHomeUiState(
    val isProtectionEnabled: Boolean = false,
    val selectedMode: HomeProtectionMode = HomeProtectionMode.DEFAULT,
    val accessibilityGranted: Boolean = true,
    val overlayEnabled: Boolean = false,

    val mindfulCountEnabled: Boolean = true,
    val mindfulTimeEnabled: Boolean = true,
    val mindfulReelsLimit: Int = 10,
    val mindfulTimeLimitMinutes: Int = 20,
    val mindfulResetPeriod: MindfulResetPeriod = MindfulResetPeriod.HOUR,

    val reelsClosedToday: Int = 0,
    val timeBackTodayMinutes: Int = 0,
    val mindfulRemainingCount: Int = 7,
    val mindfulRemainingMinutes: Int = 12,
)

//@Composable
//fun ReelBreakHomeSection(
//    state: DashboardHomeUiState,
//    onProtectionToggle: () -> Unit,
//    onModeSelected: (HomeProtectionMode) -> Unit,
//    onOverlayToggle: (Boolean) -> Unit,
//    onMindfulCountToggle: (Boolean) -> Unit,
//    onMindfulTimeToggle: (Boolean) -> Unit,
//    onMindfulReelsLimitChange: (Int) -> Unit,
//    onMindfulTimeLimitChange: (Int) -> Unit,
//    onMindfulPeriodChange: (MindfulResetPeriod) -> Unit,
//    onPermissionClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val colors = LocalAppColors.current
//
//    LazyColumn(
//        modifier = modifier.fillMaxWidth(),
//        verticalArrangement = Arrangement.spacedBy(14.dp),
//        contentPadding = PaddingValues(bottom = 120.dp)
//    ) {
//        item {
//            ProtectionHeroCard(
//                state = state,
//                onToggle = onProtectionToggle
//            )
//        }
//
//        item {
//            ModeSelectionRow(
//                selectedMode = state.selectedMode,
//                isProtectionEnabled = state.isProtectionEnabled,
//                onModeSelected = onModeSelected
//            )
//        }
//
//        if (!state.accessibilityGranted) {
//            item {
//                PermissionInfoCard(
//                    onClick = onPermissionClick
//                )
//            }
//        }
//
//        item {
//            Crossfade(
//                targetState = state.selectedMode,
//                animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing),
//                label = "modeCardCrossfade"
//            ) { mode ->
//                when (mode) {
//                    HomeProtectionMode.DEFAULT -> {
//                        DefaultModeDetailsCard(
//                            isActuallyActive = state.isProtectionEnabled && state.selectedMode == HomeProtectionMode.DEFAULT
//                        )
//                    }
//                    HomeProtectionMode.PAUSED -> {
//                        PauseModeDetailsCard(
//                            isProtectionEnabled = state.isProtectionEnabled
//                        )
//                    }
//                    HomeProtectionMode.MINDFUL -> {
//                        MindfulModeDetailsCard(
//                            state = state,
//                            onCountToggle = onMindfulCountToggle,
//                            onTimeToggle = onMindfulTimeToggle,
//                            onReelsLimitChange = onMindfulReelsLimitChange,
//                            onTimeLimitChange = onMindfulTimeLimitChange,
//                            onPeriodChange = onMindfulPeriodChange
//                        )
//                    }
//                }
//            }
//        }
//
//        item {
//            OverlayReminderCard(
//                enabled = state.overlayEnabled,
//                onToggle = onOverlayToggle
//            )
//        }
//
//        item {
//            TodayInsightCard(
//                state = state
//            )
//        }
//    }
//}


@Composable
fun ReelBreakHomeSection(
    state: DashboardHomeUiState,
    onProtectionToggle: () -> Unit,
    onModeSelected: (HomeProtectionMode) -> Unit,
    onOverlayToggle: (Boolean) -> Unit,
    onMindfulCountToggle: (Boolean) -> Unit,
    onMindfulTimeToggle: (Boolean) -> Unit,
    onMindfulReelsLimitChange: (Int) -> Unit,
    onMindfulTimeLimitChange: (Int) -> Unit,
    onMindfulPeriodChange: (MindfulResetPeriod) -> Unit,
    onPermissionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        ProtectionHeroCard(
            state = state,
            onToggle = onProtectionToggle
        )

        ModeSelectionRow(
            selectedMode = state.selectedMode,
            isProtectionEnabled = state.isProtectionEnabled,
            onModeSelected = onModeSelected
        )

        if (!state.accessibilityGranted) {
            PermissionInfoCard(
                onClick = onPermissionClick
            )
        }

        Crossfade(
            targetState = state.selectedMode,
            animationSpec = tween(
                durationMillis = 280,
                easing = FastOutSlowInEasing
            ),
            label = "modeCardCrossfade"
        ) { mode ->
            when (mode) {
                HomeProtectionMode.DEFAULT -> {
                    DefaultModeDetailsCard(
                        isActuallyActive = state.isProtectionEnabled &&
                                state.selectedMode == HomeProtectionMode.DEFAULT
                    )
                }

                HomeProtectionMode.PAUSED -> {
                    PauseModeDetailsCard(
                        isProtectionEnabled = state.isProtectionEnabled
                    )
                }

                HomeProtectionMode.MINDFUL -> {
                    MindfulModeDetailsCard(
                        state = state,
                        onCountToggle = onMindfulCountToggle,
                        onTimeToggle = onMindfulTimeToggle,
                        onReelsLimitChange = onMindfulReelsLimitChange,
                        onTimeLimitChange = onMindfulTimeLimitChange,
                        onPeriodChange = onMindfulPeriodChange
                    )
                }
            }
        }

        OverlayReminderCard(
            enabled = state.overlayEnabled,
            onToggle = onOverlayToggle
        )

        TodayInsightCard(
            state = state
        )
    }
}

@Composable
private fun ProtectionHeroCard(
    state: DashboardHomeUiState,
    onToggle: () -> Unit
) {
    val colors = LocalAppColors.current
    val accent = when {
        !state.isProtectionEnabled -> colors.textMuted
        state.selectedMode == HomeProtectionMode.DEFAULT -> colors.successGreen
        state.selectedMode == HomeProtectionMode.MINDFUL -> colors.blueAccent
        else -> colors.warningOrange
    }

    val title = if (state.isProtectionEnabled) "PROTECTION ON" else "PROTECTION OFF"
    val statusText = rememberStatusText(state)

    SurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        innerPadding = PaddingValues(horizontal = 18.dp, vertical = 18.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            AnimatedProtectionButton(
                isOn = state.isProtectionEnabled,
                accent = accent,
                mode = state.selectedMode,
                onClick = onToggle
            )

            Text(
                text = title,
                color = colors.textPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )

            LiveStatusPill(
                text = statusText,
                color = accent,
                isEnabled = state.isProtectionEnabled
            )
        }
    }
}

@Composable
private fun AnimatedProtectionButton(
    isOn: Boolean,
    accent: Color,
    mode: HomeProtectionMode,
    onClick: () -> Unit
) {
    val colors = LocalAppColors.current
    val infinite = rememberInfiniteTransition(label = "protectionRing")
    val animatedAngle by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (isOn) 2200 else 5200,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "ringAngle"
    )

    val pulse by infinite.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1600,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ringPulse"
    )

    val backgroundBrush = Brush.radialGradient(
        colors = listOf(
            accent.copy(alpha = if (colors.isDark) 0.22f else 0.16f),
            accent.copy(alpha = if (colors.isDark) 0.10f else 0.06f),
            Color.Transparent
        )
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(220.dp)
            .clip(CircleShape)
            .background(backgroundBrush)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        Canvas(
            modifier = Modifier.size(210.dp)
        ) {
            drawArc(
                color = colors.timerTrack,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
            )

            val sweep = if (isOn) 300f else 82f
            drawArc(
                color = accent.copy(alpha = if (isOn) 0.95f else 0.55f),
                startAngle = animatedAngle - 90f,
                sweepAngle = sweep,
                useCenter = false,
                style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        Box(
            modifier = Modifier
                .size((170f * pulse).dp)
                .shadow(
                    elevation = if (isOn) 18.dp else 6.dp,
                    shape = CircleShape,
                    ambientColor = accent.copy(alpha = 0.34f),
                    spotColor = accent.copy(alpha = 0.34f)
                )
                .clip(CircleShape)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            accent.copy(alpha = if (colors.isDark) 0.95f else 0.88f),
                            accent.copy(alpha = if (colors.isDark) 0.76f else 0.70f)
                        )
                    )
                )
                .border(
                    width = 1.4.dp,
                    color = Color.White.copy(alpha = if (colors.isDark) 0.18f else 0.55f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = when {
                        !isOn -> Icons.Filled.PlayArrow
                        mode == HomeProtectionMode.PAUSED -> Icons.Outlined.Pause
                        else -> Icons.Outlined.Shield
                    },
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(34.dp)
                )

                Text(
                    text = if (isOn) "Turn Off" else "Turn On",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun LiveStatusPill(
    text: String,
    color: Color,
    isEnabled: Boolean
) {
    val colors = LocalAppColors.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(
                if (colors.isDark) Color.White.copy(alpha = 0.05f)
                else color.copy(alpha = 0.08f)
            )
            .border(
                1.dp,
                if (isEnabled) color.copy(alpha = 0.35f) else colors.borderSubtle,
                RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(if (isEnabled) color else colors.textMuted.copy(alpha = 0.7f))
        )

        Text(
            text = text,
            color = if (isEnabled) color else colors.textSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ModeSelectionRow(
    selectedMode: HomeProtectionMode,
    isProtectionEnabled: Boolean,
    onModeSelected: (HomeProtectionMode) -> Unit
) {
    val items = listOf(
        Triple(HomeProtectionMode.DEFAULT, "Default", Icons.Outlined.Shield),
        Triple(HomeProtectionMode.PAUSED, "Pause", Icons.Filled.PauseCircle),
        Triple(HomeProtectionMode.MINDFUL, "Mindful", Icons.Outlined.SelfImprovement)
    )

    SurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        innerPadding = PaddingValues(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items.forEach { (mode, title, icon) ->
                val isSelected = selectedMode == mode
                val colors = LocalAppColors.current

                val accent = when (mode) {
                    HomeProtectionMode.DEFAULT -> colors.successGreen
                    HomeProtectionMode.PAUSED -> colors.warningOrange
                    HomeProtectionMode.MINDFUL -> colors.blueAccent
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .defaultMinSize(minHeight = 74.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            when {
                                isSelected && isProtectionEnabled ->
                                    accent.copy(alpha = if (colors.isDark) 0.16f else 0.10f)
                                isSelected ->
                                    Color.White.copy(alpha = if (colors.isDark) 0.07f else 0.62f)
                                else ->
                                    Color.Transparent
                            }
                        )
                        .border(
                            width = if (isSelected) 1.2.dp else 1.dp,
                            color = if (isSelected) {
                                if (isProtectionEnabled) accent.copy(alpha = 0.45f)
                                else colors.borderActive
                            } else {
                                colors.borderSubtle
                            },
                            shape = RoundedCornerShape(18.dp)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onModeSelected(mode) }
                        .padding(horizontal = 10.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (isSelected) {
                                if (isProtectionEnabled) accent else colors.textPrimary
                            } else colors.textMuted,
                            modifier = Modifier.size(20.dp)
                        )

                        Text(
                            text = title,
                            color = if (isSelected) colors.textPrimary else colors.textSecondary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = when (mode) {
                                HomeProtectionMode.DEFAULT -> "Auto-close"
                                HomeProtectionMode.PAUSED -> "Temporarily off"
                                HomeProtectionMode.MINDFUL -> "Set limits"
                            },
                            color = colors.textMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DefaultModeDetailsCard(
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
private fun PauseModeDetailsCard(
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
private fun MindfulModeDetailsCard(
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

@Composable
private fun OverlayReminderCard(
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    val colors = LocalAppColors.current

    SurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        innerPadding = PaddingValues(18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "Overlay reminder",
                    color = colors.textPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Shows a small nudge while watching. You can dismiss it anytime.",
                    color = colors.textSecondary,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            }

            Switch(
                checked = enabled,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colors.successGreen,
                    checkedTrackColor = colors.successGreen.copy(alpha = 0.38f),
                    uncheckedThumbColor = if (colors.isDark) Color.White.copy(alpha = 0.56f) else Color.White,
                    uncheckedTrackColor = colors.switchTrackOff
                )
            )
        }
    }
}

@Composable
private fun TodayInsightCard(
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
private fun PermissionInfoCard(
    onClick: () -> Unit
) {
    val colors = LocalAppColors.current

    SurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        innerPadding = PaddingValues(18.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(colors.purplePrimary.copy(alpha = if (colors.isDark) 0.16f else 0.10f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Security,
                    contentDescription = null,
                    tint = colors.purplePrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Permission needed",
                    color = colors.textPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Accessibility access helps ReelBreak detect supported short-video screens on your device. Processing stays on-device for the blocking experience.",
                    color = colors.textSecondary,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(colors.purplePrimary.copy(alpha = if (colors.isDark) 0.14f else 0.09f))
                        .border(
                            1.dp,
                            colors.purplePrimary.copy(alpha = 0.30f),
                            RoundedCornerShape(999.dp)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onClick
                        )
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = "Review access",
                        color = colors.purplePrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
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

@Composable
private fun VerticalDivider() {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .height(54.dp)
            .width(1.dp)
            .background(colors.borderSubtle.copy(alpha = 0.9f))
    )
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
private fun SurfaceCard(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = LocalAppColors.current

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(colors.cardSurface)
            .border(
                width = 1.dp,
                color = if (colors.isDark) {
                    colors.borderSubtle.copy(alpha = 0.95f)
                } else {
                    colors.borderSubtle.copy(alpha = 0.55f)
                },
                shape = RoundedCornerShape(24.dp)
            )
            .padding(innerPadding),
        content = content
    )
}

private fun rememberStatusText(state: DashboardHomeUiState): String {
    return when {
        !state.isProtectionEnabled -> "Protection is off."
        state.selectedMode == HomeProtectionMode.DEFAULT ->
            "Reels will close automatically."
        state.selectedMode == HomeProtectionMode.PAUSED ->
            "Protection is paused for now."
        state.selectedMode == HomeProtectionMode.MINDFUL ->
            "Remaining: ${mindfulRemainingShort(state)}"
        else -> "Protection is ready."
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

private fun mindfulRemainingShort(state: DashboardHomeUiState): String {
    val pieces = buildList {
        if (state.mindfulCountEnabled) add("${state.mindfulRemainingCount} reels")
        if (state.mindfulTimeEnabled) add("${state.mindfulRemainingMinutes}m")
    }
    return pieces.ifEmpty { listOf("—") }.joinToString(" • ")
}

