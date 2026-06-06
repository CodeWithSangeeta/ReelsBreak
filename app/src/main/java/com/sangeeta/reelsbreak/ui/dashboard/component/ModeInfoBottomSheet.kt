package com.sangeeta.reelsbreak.ui.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.PauseCircleOutline
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.reelsbreak.ui.dashboard.HomeProtectionMode
import com.sangeeta.reelsbreak.ui.theme.LocalAppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModeInfoBottomSheet(
    mode: HomeProtectionMode,
    title: String,
    description: String,
    features: List<String>,
    buttonText: String,
    onDismiss: () -> Unit,
    onPrimaryClick: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
) {
    val colors = LocalAppColors.current
    val accent = when (mode) {
        HomeProtectionMode.FLOW -> colors.successGreen
        HomeProtectionMode.PAUSED -> colors.pausedAccent
        HomeProtectionMode.CURIOUS -> colors.blueAccent
    }

    val icon = when (mode) {
        HomeProtectionMode.FLOW -> Icons.Outlined.Bolt
        HomeProtectionMode.PAUSED -> Icons.Outlined.PauseCircleOutline
        HomeProtectionMode.CURIOUS -> Icons.Outlined.SelfImprovement
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .background(
                        color = colors.sheetDragHandle,
                        shape = RoundedCornerShape(999.dp)
                    )
                    .fillMaxWidth(0.14f)
                    .padding(vertical = 2.dp)
            )
        }
    ) {
        SurfaceCard(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            innerPadding = PaddingValues(horizontal = 18.dp, vertical = 18.dp),
            useTransparentBorder = true
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = accent.copy(alpha = if (colors.isDark) 0.18f else 0.10f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = accent,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = title,
                            color = colors.textPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = modeLabel(mode),
                            color = colors.textSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Text(
                    text = description,
                    color = colors.textSecondary,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    features.take(3).forEach { feature ->
                        CompactFeatureRow(
                            text = feature,
                            accent = accent
                        )
                    }
                }

                if (mode == HomeProtectionMode.CURIOUS) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AutoAwesome,
                            contentDescription = null,
                            tint = colors.textMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "You can adjust these limits any time.",
                            color = colors.textMuted,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                }

                SheetButton(
                    text = buttonText,
                    onClick = onPrimaryClick
                )
            }
        }
    }
}

@Composable
private fun CompactFeatureRow(
    text: String,
    accent: androidx.compose.ui.graphics.Color
) {
    val colors = LocalAppColors.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(accent.copy(alpha = 0.9f), CircleShape)
        )

        Text(
            text = text,
            color = colors.textPrimary,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun modeLabel(mode: HomeProtectionMode): String {
    return when (mode) {
        HomeProtectionMode.FLOW -> "Recommended for fewer interruptions"
        HomeProtectionMode.PAUSED -> "Temporary break"
        HomeProtectionMode.CURIOUS -> "Intentional viewing with limits"
    }
}