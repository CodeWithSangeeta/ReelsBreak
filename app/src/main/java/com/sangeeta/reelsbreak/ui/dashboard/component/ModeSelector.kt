package com.practice.reelbreak.ui.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.reelbreak.ui.dashboard.HomeProtectionMode
import com.practice.reelbreak.ui.theme.LocalAppColors

@Composable
fun ModeSelection(
    selectedMode: HomeProtectionMode,
    isProtectionEnabled: Boolean,
    onModeSelected: (HomeProtectionMode) -> Unit
) {
    val items = listOf(
        Triple(HomeProtectionMode.DEFAULT, "Default", Icons.Outlined.Shield),
        Triple(HomeProtectionMode.PAUSED, "Pause", Icons.Filled.PauseCircle),
        Triple(HomeProtectionMode.MINDFUL, "Mindful", Icons.Outlined.SelfImprovement)
    )

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
                        .defaultMinSize(minHeight = 64.dp)
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