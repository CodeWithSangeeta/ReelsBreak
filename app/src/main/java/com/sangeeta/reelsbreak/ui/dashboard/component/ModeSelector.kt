//package com.sangeeta.reelsbreak.ui.dashboard.component
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.PauseCircle
//import androidx.compose.material.icons.outlined.Block
//import androidx.compose.material.icons.outlined.SelfImprovement
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.sangeeta.reelsbreak.ui.dashboard.HomeProtectionMode
//import com.sangeeta.reelsbreak.ui.theme.LocalAppColors
//
//private data class ModeItem(
//    val mode: HomeProtectionMode,
//    val title: String,
//    val subtitle: String,
//    val icon: ImageVector
//)
//
//@Composable
//fun ModeSelection(
//    selectedMode: HomeProtectionMode,
//    isProtectionEnabled: Boolean,
//    onModeSelected: (HomeProtectionMode) -> Unit
//) {
//    val colors = LocalAppColors.current
//
//    val items = listOf(
//        ModeItem(
//            mode = HomeProtectionMode.FLOW,
//            title = "Flow",
//            subtitle = "Auto-close",
//            icon = Icons.Outlined.Block
//        ),
//        ModeItem(
//            mode = HomeProtectionMode.PAUSED,
//            title = "Pause",
//            subtitle = "Take a break",
//            icon = Icons.Filled.PauseCircle
//        ),
//        ModeItem(
//            mode = HomeProtectionMode.CURIOUS,
//            title = "Curious",
//            subtitle = "Set limits",
//            icon = Icons.Outlined.SelfImprovement
//        )
//    )
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(86.dp)
//            .clip(RoundedCornerShape(22.dp))
//            .background(colors.cardSurface)
//            .border(
//                width = 1.dp,
//                color = colors.borderSubtle,
//                shape = RoundedCornerShape(22.dp)
//            )
//            .padding(horizontal = 4.dp, vertical = 4.dp),
//        horizontalArrangement = Arrangement.spacedBy(4.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        items.forEach { item ->
//            val isSelected = selectedMode == item.mode
//
//            val accent = when (item.mode) {
//                HomeProtectionMode.FLOW -> colors.successGreen
//                HomeProtectionMode.PAUSED -> colors.pausedAccent
//                HomeProtectionMode.CURIOUS -> colors.blueAccent
//            }
//
//            ModeSelectionItem(
//                item = item,
//                isSelected = isSelected,
//                isProtectionEnabled = isProtectionEnabled,
//                accent = accent,
//                onClick = { onModeSelected(item.mode) },
//                modifier = Modifier.weight(1f)
//            )
//        }
//    }
//}
//
//@Composable
//private fun ModeSelectionItem(
//    item: ModeItem,
//    isSelected: Boolean,
//    isProtectionEnabled: Boolean,
//    accent: Color,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val colors = LocalAppColors.current
//
//    val selectedBrush = Brush.verticalGradient(
//        colors = listOf(
//            accent.copy(alpha = if (colors.isDark) 0.24f else 0.14f),
//            accent.copy(alpha = if (colors.isDark) 0.14f else 0.08f)
//        )
//    )
//
//    Box(
//        modifier = modifier
//            .height(86.dp)
//            .clip(RoundedCornerShape(18.dp))
//            .background(
//                if (isSelected) selectedBrush
//                else Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent))
//            )
//            .then(
//                if (isSelected) {
//                    Modifier.border(
//                        width = 1.dp,
//                        color = if (isProtectionEnabled) {
//                            accent.copy(alpha = 0.40f)
//                        } else {
//                            colors.borderActive
//                        },
//                        shape = RoundedCornerShape(18.dp)
//                    )
//                } else {
//                    Modifier
//                }
//            )
//            .clickable(
//                interactionSource = remember { MutableInteractionSource() },
//                indication = null,
//                onClick = onClick
//            ),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center,
//            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
//        ) {
//            Icon(
//                imageVector = item.icon,
//                contentDescription = item.title,
//                tint = if (isSelected) {
//                    if (isProtectionEnabled) accent else colors.textPrimary
//                } else {
//                    colors.textMuted
//                },
//                modifier = Modifier.size(24.dp)
//            )
//
//            Text(
//                text = item.title,
//                color = if (isSelected) colors.textPrimary else colors.textSecondary,
//                fontSize = 16.sp,
//                fontWeight = FontWeight.SemiBold,
//                maxLines = 1
//            )
//
//            Text(
//                text = item.subtitle,
//                color = if (isSelected) {
//                    if (isProtectionEnabled) accent.copy(alpha = 0.92f)
//                    else colors.textMuted
//                } else {
//                    colors.textMuted
//                },
//                fontSize = 10.sp,
//                lineHeight = 11.sp,
//                fontWeight = FontWeight.Medium,
//                textAlign = TextAlign.Center,
//                maxLines = 1
//            )
//        }
//    }
//}



package com.sangeeta.reelsbreak.ui.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.reelsbreak.ui.dashboard.HomeProtectionMode
import com.sangeeta.reelsbreak.ui.theme.LocalAppColors

private data class ModeItem(
    val mode: HomeProtectionMode,
    val title: String,
    val subtitle: String,
    val icon: ImageVector
)

@Composable
fun ModeSelection(
    selectedMode: HomeProtectionMode,
    isProtectionEnabled: Boolean,
    onModeSelected: (HomeProtectionMode) -> Unit
) {
    val colors = LocalAppColors.current

    val items = listOf(
        ModeItem(HomeProtectionMode.FLOW, "Flow", "Auto-close", Icons.Outlined.Block),
        ModeItem(HomeProtectionMode.PAUSED, "Pause", "Take a break", Icons.Filled.PauseCircle),
        ModeItem(HomeProtectionMode.CURIOUS, "Curious", "Set limits", Icons.Outlined.SelfImprovement)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(82.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(colors.cardSurface)
            .border(1.dp, colors.borderSubtle, RoundedCornerShape(20.dp))
            .padding(all = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            val isSelected = selectedMode == item.mode
            val accent = when (item.mode) {
                HomeProtectionMode.FLOW -> colors.successGreen
                HomeProtectionMode.PAUSED -> colors.pausedAccent
                HomeProtectionMode.CURIOUS -> colors.blueAccent
            }

            // High-end state logic: Dim elements elegantly if master protection engine is turned off
            val dynamicModifier = if (isSelected && !isProtectionEnabled) {
                Modifier.alpha(0.6f)
            } else if (!isSelected && !isProtectionEnabled) {
                Modifier.alpha(0.35f)
            } else {
                Modifier
            }

            ModeSelectionItem(
                item = item,
                isSelected = isSelected,
                isProtectionEnabled = isProtectionEnabled,
                accent = accent,
                onClick = { onModeSelected(item.mode) },
                modifier = Modifier.weight(1f).then(dynamicModifier)
            )
        }
    }
}

@Composable
private fun ModeSelectionItem(
    item: ModeItem,
    isSelected: Boolean,
    isProtectionEnabled: Boolean,
    accent: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current

    val selectedBrush = Brush.verticalGradient(
        colors = listOf(
            accent.copy(alpha = if (isProtectionEnabled) 0.16f else 0.06f),
            accent.copy(alpha = if (isProtectionEnabled) 0.06f else 0.02f)
        )
    )

    val activeBorderColor = if (isProtectionEnabled) accent.copy(alpha = 0.40f) else colors.textMuted.copy(alpha = 0.25f)

    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) selectedBrush else Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent)))
            .then(
                if (isSelected) Modifier.border(1.dp, activeBorderColor, RoundedCornerShape(16.dp))
                else Modifier
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = if (isSelected) accent else colors.textMuted,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = item.title,
                color = if (isSelected) colors.textPrimary else colors.textSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Text(
                text = item.subtitle,
                color = if (isSelected) colors.textPrimary.copy(alpha = 0.6f) else colors.textMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}