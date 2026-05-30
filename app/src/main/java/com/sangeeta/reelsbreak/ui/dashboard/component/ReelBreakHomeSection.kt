package com.sangeeta.reelsbreak.ui.dashboard.component

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sangeeta.reelsbreak.ui.dashboard.DashboardHomeUiState
import com.sangeeta.reelsbreak.ui.dashboard.HomeProtectionMode
import com.sangeeta.reelsbreak.ui.dashboard.MindfulResetPeriod

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
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        ProtectionCard(
            state = state,
            onToggle = onProtectionToggle
        )

        ModeSelection(
            selectedMode = state.selectedMode,
            isProtectionEnabled = state.isProtectionEnabled,
            onModeSelected = onModeSelected
        )

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

        OverlayToggleCard(
            enabled = state.overlayEnabled,
            onToggle = onOverlayToggle
        )

        TodayStatsCard(
            state = state
        )
    }
}
