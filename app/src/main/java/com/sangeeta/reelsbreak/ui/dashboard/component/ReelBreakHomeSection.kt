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
import com.sangeeta.reelsbreak.ui.dashboard.CuriousResetPeriod
import com.sangeeta.reelsbreak.ui.dashboard.DashboardHomeUiState
import com.sangeeta.reelsbreak.ui.dashboard.HomeProtectionMode

@Composable
fun ReelBreakHomeSection(
    state: DashboardHomeUiState,
    onProtectionToggle: () -> Unit,
    onModeSelected: (HomeProtectionMode) -> Unit,
    onOverlayToggle: (Boolean) -> Unit,
    onCuriousCountToggle: (Boolean) -> Unit,
    onCuriousTimeToggle: (Boolean) -> Unit,
    onCuriousReelsLimitChange: (Int) -> Unit,
    onCuriousTimeLimitChange: (Int) -> Unit,
    onCuriousPeriodChange: (CuriousResetPeriod) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        ProtectionCard(
            state = state,
            onToggle = onProtectionToggle,
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
                HomeProtectionMode.FLOW -> {
                    FlowModeDetailsCard(
                        isActuallyActive = state.isProtectionEnabled &&
                                state.selectedMode == HomeProtectionMode.FLOW
                    )
                }

                HomeProtectionMode.PAUSED -> {
                    PauseModeDetailsCard(
                        isProtectionEnabled = state.isProtectionEnabled
                    )
                }

                HomeProtectionMode.CURIOUS -> {
                    CuriousModeDetailsCard(
                        state = state,
                        onCountToggle = onCuriousCountToggle,
                        onTimeToggle = onCuriousTimeToggle,
                        onReelsLimitChange = onCuriousReelsLimitChange,
                        onTimeLimitChange = onCuriousTimeLimitChange,
                        onPeriodChange = onCuriousPeriodChange
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
