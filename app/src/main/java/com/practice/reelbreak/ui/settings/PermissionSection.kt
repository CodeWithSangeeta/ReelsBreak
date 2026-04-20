package com.practice.reelbreak.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessibilityNew
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.practice.reelbreak.ui.permission.PermissionSheetType
import com.practice.reelbreak.ui.theme.LocalAppColors

@Composable
 fun PermissionsSection(
    uiState: SettingsState,
    onInfoClick: (PermissionSheetType) -> Unit,
    onEnableClick: (PermissionSheetType) -> Unit
) {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(colors.cardSurface)
            .border(1.dp, colors.borderSubtle, RoundedCornerShape(18.dp))
    ) {
        Column {
            PermissionRow(
                icon = Icons.Outlined.AccessibilityNew,
                title = "Accessibility Service",
                subtitle = "Required to detect reels",
                isGranted = uiState.isAccessibilityGranted,
                type = PermissionSheetType.ACCESSIBILITY,
                onInfoClick = onInfoClick,
                onEnableClick = onEnableClick
            )
            RowDivider(horizontal = 16.dp)
            PermissionRow(
                icon = Icons.Outlined.BarChart,
                title = "Usage Access",
                subtitle = "Required for screen time tracking",
                isGranted = uiState.isUsageAccessGranted,
                type = PermissionSheetType.USAGE_ACCESS,
                onInfoClick = onInfoClick,
                onEnableClick = onEnableClick
            )
            RowDivider(horizontal = 16.dp)
            PermissionRow(
                icon = Icons.Outlined.Layers,
                title = "Display Overlay",
                subtitle = "Optional — for block overlay screen",
                isGranted = uiState.isOverlayGranted,
                type = PermissionSheetType.OVERLAY,
                onInfoClick = onInfoClick,
                onEnableClick = onEnableClick
            )
        }
    }
}
