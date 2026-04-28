package com.practice.reelbreak.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.reelbreak.ui.theme.LocalAppColors
import com.practice.reelbreak.R


@Composable
fun DashboardHeader(
    isOverlayGranted: Boolean,
    isOverlayEnabled: Boolean,
    isDarkMode: Boolean,
    onVisibilityClick: () -> Unit,
    onThemeToggle: () -> Unit
) {
    val colors = LocalAppColors.current
    val iconColor = if (isOverlayGranted && isOverlayEnabled)
        colors.successGreen else colors.textMuted
    val themeIcon = if (isDarkMode) Icons.Outlined.DarkMode else Icons.Outlined.LightMode

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo + wordmark
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // ── Actual app logo from res/mipmap ──
            Image(
                painter = painterResource(id = R.drawable.reelsbreak_logo),
                contentDescription = "ReelsBreak logo",
                modifier = Modifier
                    .size(55.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "Reels",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    Text(
                        text = "Break",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.purplePrimary
                    )
                }
                Text(
                    text = "Stay mindful",
                    fontSize = 11.sp,
                    color = colors.textMuted
                )
            }
        }

        // Right icons
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HeaderIconButton(
                icon = if (isOverlayGranted && isOverlayEnabled)
                    Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                onClick = onVisibilityClick,
                tint = iconColor
            )
            HeaderIconButton(
                icon = themeIcon,
                onClick = onThemeToggle,
                tint = colors.textMuted
            )
        }
    }
}