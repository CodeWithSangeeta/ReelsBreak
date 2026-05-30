package com.sangeeta.reelsbreak.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.reelsbreak.ui.theme.LocalAppColors

@Composable
fun PermissionPagerCard(
    item: PermissionPagerItem,
    isGranted: Boolean,
    onClick: () -> Unit
) {
    val colors = LocalAppColors.current
    val accentColor = if (isGranted) colors.successGreen else colors.purplePrimary

    // Play-Store-safe description override
    val safeDescription = when {
        isGranted -> "ReelBreak is set up and ready to help you stay focused."
        else -> "ReelBreak uses Android's Accessibility feature to detect " +
                "short-video screens and automatically navigate away from them, " +
                "helping you stay focused and in control of your screen time."
    }

    val safeTitle = when {
        isGranted -> "Setup Complete"
        else -> "One-time Setup Required"
    }

    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        accentColor.copy(alpha = if (colors.isDark) 0.10f else 0.06f),
                        colors.cardSurface
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        accentColor.copy(alpha = if (isGranted) 0.6f else 0.35f),
                        accentColor.copy(alpha = 0.10f)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

            // ── Top row: icon badge + status chip ──────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon badge
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(accentColor.copy(alpha = if (colors.isDark) 0.18f else 0.10f))
                        .border(
                            width = 1.dp,
                            color = accentColor.copy(alpha = 0.30f),
                            shape = RoundedCornerShape(14.dp)
                        )
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Status chip
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(accentColor.copy(alpha = if (colors.isDark) 0.15f else 0.10f))
                        .border(
                            width = 1.dp,
                            color = accentColor.copy(alpha = 0.35f),
                            shape = RoundedCornerShape(999.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = if (isGranted) "✓  Active" else "Required",
                        color = accentColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.3.sp
                    )
                }
            }

            // ── Title + Description ────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = safeTitle,
                    color = colors.textPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp
                )
                Text(
                    text = safeDescription,
                    color = colors.textSecondary,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            }

            // ── Divider line ───────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(accentColor.copy(alpha = 0.12f))
            )

            // ── CTA Button ─────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (isGranted)
                            colors.successGreen.copy(alpha = if (colors.isDark) 0.15f else 0.10f)
                        else
                            Brush.horizontalGradient(
                                colors = listOf(
                                    colors.purplePrimary,
                                    colors.purpleDeep
                                )
                            )
                    )
                    .then(
                        if (isGranted) Modifier.border(
                            1.dp,
                            colors.successGreen.copy(alpha = 0.30f),
                            RoundedCornerShape(14.dp)
                        ) else Modifier
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        enabled = !isGranted,
                        onClick = onClick
                    )
                    .padding(horizontal = 18.dp, vertical = 13.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isGranted) "Setup complete — no action needed"
                    else item.buttonText,
                    color = if (isGranted) colors.successGreen else colors.textPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}