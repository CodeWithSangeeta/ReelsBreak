package com.practice.reelbreak.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.practice.reelbreak.ui.component.AppScreenHeader

@Composable
fun DashboardHeader(
    isOverlayGranted: Boolean,
    isOverlayEnabled: Boolean,
    isDarkMode: Boolean,
    onVisibilityClick: () -> Unit,
    onThemeToggle: () -> Unit
) {
    AppScreenHeader(
        title    = "ReelBreak",
        subtitle = "Stay Mindful",
        actions  = {
            HeaderActionButton(
                icon = if (isOverlayGranted && isOverlayEnabled)
                    Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                onClick = onVisibilityClick
            )
            HeaderActionButton(
                icon = if (isDarkMode)
                    Icons.Outlined.DarkMode else Icons.Outlined.LightMode,
                onClick = onThemeToggle
            )
        }
    )
}

@Composable
private fun HeaderActionButton(
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .clickable(onClick = onClick)
        )
    }
}