package com.practice.reelbreak.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.PauseCircle
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.TipsAndUpdates
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.reelbreak.ui.theme.LocalAppColors

@Composable
fun StrictDetails() {
    val colors = LocalAppColors.current
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            HowItWorksRow(
                icon = Icons.Outlined.Block,
                text = "Immediately closes Reels and Shorts the moment you open them",
                iconTint = colors.errorRed.copy(alpha = 0.9f)
            )
            HowItWorksRow(
                icon = Icons.Outlined.Bolt,
                text = "No timer or grace period — every attempt is blocked instantly",
                iconTint = colors.warningOrange.copy(alpha = 0.9f)
            )
            HowItWorksRow(
                icon = Icons.Outlined.PhoneAndroid,
                text = "Works across Instagram, YouTube, Snapchat & Facebook",
                iconTint = Color.White.copy(alpha = 0.7f)
            )
            HowItWorksRow(
                icon = Icons.Outlined.Shield,
                text = "Best choice if you want to stop the habit completely",
                iconTint = colors.successGreen.copy(alpha = 0.9f)
            )
        }
    }

@Composable
fun LimitSettingsContent(
    dailyTimeLimitMinutes: Int,
    dailyReelLimit: Int,
    onTimeDecrement: () -> Unit,
    onTimeIncrement: () -> Unit,
    onReelDecrement: () -> Unit,
    onReelIncrement: () -> Unit
) {

    val colors = LocalAppColors.current
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        CompactStepper(
            label = "Daily time limit",
            value = dailyTimeLimitMinutes,
            unit = "min",
            onDecrement = onTimeDecrement,
            onIncrement = onTimeIncrement
        )

        CompactStepper(
            label = "Daily reel limit",
            value = dailyReelLimit,
            unit = "reels",
            onDecrement = onReelDecrement,
            onIncrement = onReelIncrement
        )

        HorizontalDivider(
            color = Color.White.copy(alpha = 0.1f),
            thickness = 0.8.dp
        )

        // ── How it works section ──
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            HowItWorksRow(
                icon = Icons.Outlined.AccessTime,
                text = "Set a daily time limit and/or a max reel count — whichever limit is hit first will trigger the block",
                iconTint = colors.blueAccent.copy(alpha = 0.9f)
            )
            HowItWorksRow(
                icon = Icons.Outlined.PauseCircle,
                text = "Once your limit is reached, reels are blocked for the rest of the day",
                iconTint = colors.warningOrange.copy(alpha = 0.9f)
            )
            HowItWorksRow(
                icon = Icons.Outlined.Refresh,
                text = "Your limit resets automatically at midnight every day",
                iconTint = colors.successGreen.copy(alpha = 0.9f)
            )
            HowItWorksRow(
                icon = Icons.Outlined.NotificationsActive,
                text = "You will get a nudge when you are close to hitting your limit",
                iconTint = colors.purpleSoft.copy(alpha = 0.9f)
            )
        }

    }
}


@Composable
fun SmartFilterDetails() {
    val colors = LocalAppColors.current
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        // Coming soon badge
       Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(colors.purplePrimary.copy(alpha = 0.18f))
                .border(
                    width = 1.dp,
                    color = colors.purplePrimary.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Text(
                text = "Coming Soon",
                color = colors.purpleSoft,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(2.dp))

        HowItWorksRow(
            icon = Icons.Outlined.FilterList,
            text = "Filters reels intelligently instead of blocking them all",
            iconTint = colors.purpleSoft.copy(alpha = 0.9f)
        )
        HowItWorksRow(
            icon = Icons.Outlined.AutoAwesome,
            text = "Planned: allow content only from creators you have liked",
            iconTint = colors.blueAccent.copy(alpha = 0.9f)
        )
        HowItWorksRow(
            icon = Icons.Outlined.TipsAndUpdates,
            text = "Will adapt based on your usage patterns over time",
            iconTint = colors.warningOrange.copy(alpha = 0.9f)
        )
    }
}



@Composable
fun HowItWorksRow(
    icon: ImageVector,
    text: String,
    iconTint: Color = Color.White.copy(alpha = 0.75f)
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(15.dp)
        )
        Text(
            text = text,
            color = Color.White.copy(alpha = 0.80f),
            fontSize = 12.sp,
            lineHeight = 17.sp,
            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
fun CompactStepper(
    label: String,
    value: Int,
    unit: String,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit
) {
    val colors = LocalAppColors.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                Color.Black.copy(
                    alpha = if (colors.isDark) 0.22f else 0.06f
                )
            )
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = colors.textPrimary.copy(alpha = 0.92f),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // − button
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(
                        Color.White.copy(
                            alpha = if (colors.isDark) 0.12f else 0.16f
                        )
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onDecrement
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "−",
                    color = colors.textPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Value
            Text(
                text = "$value $unit",
                color = colors.textPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            // + button
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(
                        Color.White.copy(
                            alpha = if (colors.isDark) 0.12f else 0.16f
                        )
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onIncrement
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    color = colors.textPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}