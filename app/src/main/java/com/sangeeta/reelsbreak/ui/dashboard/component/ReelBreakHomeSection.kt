//package com.sangeeta.reelsbreak.ui.dashboard.component
//
//import androidx.compose.animation.Crossfade
//import androidx.compose.animation.core.FastOutSlowInEasing
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.sangeeta.reelsbreak.ui.dashboard.CuriousResetPeriod
//import com.sangeeta.reelsbreak.ui.dashboard.DashboardHomeUiState
//import com.sangeeta.reelsbreak.ui.dashboard.HomeProtectionMode
//
//@Composable
//fun ReelBreakHomeSection(
//    state: DashboardHomeUiState,
//    onProtectionToggle: () -> Unit,
//    onModeSelected: (HomeProtectionMode) -> Unit,
//    onOverlayToggle: (Boolean) -> Unit,
//    onCuriousCountToggle: (Boolean) -> Unit,
//    onCuriousTimeToggle: (Boolean) -> Unit,
//    onCuriousReelsLimitChange: (Int) -> Unit,
//    onCuriousTimeLimitChange: (Int) -> Unit,
//    onCuriousPeriodChange: (CuriousResetPeriod) -> Unit,
//    modifier: Modifier = Modifier
//) {
//
//    Column(
//        modifier = modifier.fillMaxWidth(),
//        verticalArrangement = Arrangement.spacedBy(14.dp)
//    ) {
//        ProtectionCard(
//            state = state,
//            onToggle = onProtectionToggle,
//            onModeSelected = onModeSelected
//        )
//
//        Crossfade(
//            targetState = state.selectedMode,
//            animationSpec = tween(
//                durationMillis = 280,
//                easing = FastOutSlowInEasing
//            ),
//            label = "modeCardCrossfade"
//        ) { mode ->
//            when (mode) {
//                HomeProtectionMode.FLOW -> {
//                    FlowModeDetailsCard(
//                        isActuallyActive = state.isProtectionEnabled &&
//                                state.selectedMode == HomeProtectionMode.FLOW
//                    )
//                }
//
//                HomeProtectionMode.PAUSED -> {
//                    PauseModeDetailsCard(
//                        isProtectionEnabled = state.isProtectionEnabled
//                    )
//                }
//
//                HomeProtectionMode.CURIOUS -> {
//                    CuriousModeDetailsCard(
//                        state = state,
//                        onCountToggle = onCuriousCountToggle,
//                        onTimeToggle = onCuriousTimeToggle,
//                        onReelsLimitChange = onCuriousReelsLimitChange,
//                        onTimeLimitChange = onCuriousTimeLimitChange,
//                        onPeriodChange = onCuriousPeriodChange
//                    )
//                }
//            }
//        }
//
//        OverlayToggleCard(
//            enabled = state.overlayEnabled,
//            onToggle = onOverlayToggle
//        )
//
//        TodayStatsCard(
//            state = state
//        )
//    }
//}




//package com.sangeeta.reelsbreak.ui.dashboard.component
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.expandVertically
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.animation.shrinkVertically
//import androidx.compose.animation.core.FastOutSlowInEasing
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import com.sangeeta.reelsbreak.ui.dashboard.CuriousResetPeriod
//import com.sangeeta.reelsbreak.ui.dashboard.DashboardHomeUiState
//import com.sangeeta.reelsbreak.ui.dashboard.HomeProtectionMode
//
//@Composable
//fun ReelBreakHomeSection(
//    state: DashboardHomeUiState,
//    onProtectionToggle: () -> Unit,
//    onModeSelected: (HomeProtectionMode) -> Unit,
//    onOverlayToggle: (Boolean) -> Unit,
//    onCuriousCountToggle: (Boolean) -> Unit,
//    onCuriousTimeToggle: (Boolean) -> Unit,
//    onCuriousReelsLimitChange: (Int) -> Unit,
//    onCuriousTimeLimitChange: (Int) -> Unit,
//    onCuriousPeriodChange: (CuriousResetPeriod) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Column(
//        modifier = modifier.fillMaxWidth(),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        // 1. The Core Bordered Hub Area
//        ProtectionCard(
//            state = state,
//            onToggle = onProtectionToggle,
//            onModeSelected = onModeSelected
//        )
//
//        // 2. Continuous Clean Content Stack (No card boxes used below this boundary)
//        AnimatedVisibility(
//            visible = state.selectedMode == HomeProtectionMode.CURIOUS,
//            enter = fadeIn(tween(200)) + expandVertically(animationSpec = tween(250, easing = FastOutSlowInEasing)),
//            exit = fadeOut(tween(150)) + shrinkVertically(animationSpec = tween(200, easing = FastOutSlowInEasing))
//        ) {
//            // Rendered directly over wallpaper gradients dynamically
//            CuriousModeDetailsCard(
//                state = state,
//                onCountToggle = onCuriousCountToggle,
//                onTimeToggle = onCuriousTimeToggle,
//                onReelsLimitChange = onCuriousReelsLimitChange,
//                onTimeLimitChange = onCuriousTimeLimitChange,
//                onPeriodChange = onCuriousPeriodChange
//            )
//        }
//
//        OverlayToggleCard(
//            enabled = state.overlayEnabled,
//            onToggle = onOverlayToggle
//        )
//
//        TodayStatsCard(
//            state = state
//        )
//    }
//}




//package com.sangeeta.reelsbreak.ui.dashboard.component
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.core.FastOutSlowInEasing
//import androidx.compose.animation.core.tween
//import androidx.compose.animation.expandVertically
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.animation.shrinkVertically
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.sangeeta.reelsbreak.ui.dashboard.DashboardHomeUiState
//import com.sangeeta.reelsbreak.ui.dashboard.HomeProtectionMode
//
//@Composable
//fun ReelBreakHomeSection(
//    state: DashboardHomeUiState,
//    onProtectionToggle: () -> Unit,
//    onModeSelected: (HomeProtectionMode) -> Unit,
//    onOverlayToggle: (Boolean) -> Unit,
//    onPreviewOverlayClick: () -> Unit,
//    onProtectionInfoClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Column(
//        modifier = modifier.fillMaxWidth(),
//        verticalArrangement = Arrangement.spacedBy(14.dp)
//    ) {
//        ProtectionCard(
//            state = state,
//            onToggle = onProtectionToggle,
//            onModeSelected = onModeSelected,
//            onInfoClick = onProtectionInfoClick
//        )
//
//        StatsCapsule(state = state)
//
//        QuickControlsCard(
//            overlayEnabled = state.overlayEnabled,
//            onOverlayToggle = onOverlayToggle,
//            onPreviewOverlayClick = onPreviewOverlayClick
//        )
//
//        TodayProgressCard(state = state)
//    }
//}


package com.sangeeta.reelsbreak.ui.dashboard.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
    onInfoClick: () -> Unit,
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
            onModeSelected = onModeSelected,
            onInfoClick = onInfoClick
        )

        AnimatedVisibility(
            visible = state.selectedMode == HomeProtectionMode.CURIOUS,
            enter = fadeIn(animationSpec = tween(220)) +
                    expandVertically(
                        animationSpec = tween(
                            durationMillis = 260,
                            easing = FastOutSlowInEasing
                        )
                    ),
            exit = fadeOut(animationSpec = tween(160)) +
                    shrinkVertically(
                        animationSpec = tween(
                            durationMillis = 220,
                            easing = FastOutSlowInEasing
                        )
                    )
        ) {
            CuriousModeDetailsCard(
                state = state,
                onCountToggle = onCuriousCountToggle,
                onTimeToggle = onCuriousTimeToggle,
                onReelsLimitChange = onCuriousReelsLimitChange,
                onTimeLimitChange = onCuriousTimeLimitChange,
                onPeriodChange = onCuriousPeriodChange
            )
        }

        OverlayToggleCard(
            enabled = state.overlayEnabled,
            onToggle = onOverlayToggle
        )

        TodayStatsCard(state = state)
    }
}
