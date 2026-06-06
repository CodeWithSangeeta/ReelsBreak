package com.sangeeta.reelsbreak.ui.dashboard.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sangeeta.reelsbreak.ui.dashboard.DashboardHomeUiState
import com.sangeeta.reelsbreak.ui.dashboard.HomeProtectionMode
import com.sangeeta.reelsbreak.ui.focused_mode.SupportedAppsCard

@Composable
fun DashboardHomeSection(
    state: DashboardHomeUiState,
    onProtectionToggle: () -> Unit,
    onModeSelected: (HomeProtectionMode) -> Unit,
    onProtectionInfoClick: () -> Unit,
    onOverlayToggle: (Boolean) -> Unit,
    onPreviewOverlayClick: () -> Unit,
    onSupportedAppToggle: (String) -> Unit,
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
            onInfoClick = onProtectionInfoClick
        )

        QuickControlsCard(
            overlayEnabled = state.overlayEnabled,
            onOverlayToggle = onOverlayToggle,
            onPreviewOverlayClick = onPreviewOverlayClick
        )

        TodayProgressCard(state = state)

        SupportedAppsCard(
            selectedPackages = state.selectedSupportedPackages,
            isEnabled = true,
            onToggle = onSupportedAppToggle
        )
    }
}
