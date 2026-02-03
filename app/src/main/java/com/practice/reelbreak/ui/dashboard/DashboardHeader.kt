package com.sangeeta.reelbreak.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.sp
import androidx.lint.kotlin.metadata.Visibility

@Composable
fun DashboardHeader(
    userName: String,
    onVisibilityToggle: () -> Unit,
    onThemeToggle: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column {
            Text(
                text = "ReelsGuard",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground            )
            Text(
                text = "Stay mindful, $userName.",
                color = MaterialTheme.colorScheme.primary
            )
            Text(text = "You are doing great today ✨",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                fontSize = 12.sp
                )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HeaderIconButton(
                icon = Icons.Outlined.Visibility,
                onClick = onVisibilityToggle
            )
            HeaderIconButton(
                // Dynamically change icon based on mode
                icon = Icons.Outlined.DarkMode,
                onClick = onThemeToggle
            )
        }
    }
}
