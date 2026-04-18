package com.practice.reelbreak.ui.dashboard


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.reelbreak.ui.theme.LocalAppColors

@Composable
 fun ServiceStatusCard(
    isActive: Boolean,
    onToggle: () -> Unit
) {
    val colors = LocalAppColors.current
    val toggleColor by animateColorAsState(
        targetValue = if (isActive) colors.successGreen else colors.textMuted,
        animationSpec = tween(300),
        label = "toggleColor"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = if (isActive) Color(0x442ECC71) else colors.glowPurple
            )
            .clip(RoundedCornerShape(20.dp))
            .background(colors.cardSurface)
            .border(
                width = 1.dp,
                color = if (isActive) Color(0x552ECC71) else colors.borderSubtle,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Power icon circle
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            if (isActive) Brush.radialGradient(
                                colors = listOf(Color(0x442ECC71), Color(0x002ECC71))
                            ) else Brush.radialGradient(
                                colors = listOf(Color(0x22FFFFFF), Color(0x00FFFFFF))
                            )
                        )
                        .border(
                            1.dp,
                            color = if (isActive) Color(0x882ECC71) else colors.borderSubtle,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Power,
                        contentDescription = "Power",
                        tint = toggleColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column {
                    Text(
                        text = if (isActive) "Guard is ON" else "Guard is OFF",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = if (isActive) "Reels are being monitored" else "Tap to enable protection",
                        color = colors.textSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            // Toggle button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isActive) Brush.linearGradient(
                            listOf(Color(0xFF1A7A44), Color(0xFF2ECC71))
                        ) else colors.button
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onToggle
                    )
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = if (isActive) "Turn Off" else "Enable",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
