package com.practice.reelbreak.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.practice.reelbreak.ui.theme.LocalAppColors

@Composable
fun DashboardHeader(
    isOverlayGranted: Boolean,
    isOverlayEnabled: Boolean,
    isDarkMode: Boolean,
    onVisibilityClick: () -> Unit,
    onThemeToggle: () -> Unit
) {
    val colors = LocalAppColors.current
    val iconColor = if (isOverlayGranted && isOverlayEnabled) Color(0xFF4CAF50)  else Color(0xFF9E9E9E)
    val themeIcon =
        if (isDarkMode) Icons.Outlined.DarkMode else Icons.Outlined.LightMode
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column {
            Text(
                text = "ReelsBreak",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Stay mindful",
                color = MaterialTheme.colorScheme.primary
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            HeaderIconButton(
                icon = Icons.Outlined.Visibility,
                onClick = onVisibilityClick,
                tint = iconColor
            )
            HeaderIconButton(
                icon = themeIcon,
                onClick = onThemeToggle
            )
        }
    }
}