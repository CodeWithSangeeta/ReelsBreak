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
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        modifier = Modifier.fillMaxWidth().padding(top= 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column {
            Text(
                text = "ReelsBreak",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground )
            Spacer(modifier=Modifier.height(2.dp))
            Text(
                text = "Stay mindful, $userName.",
                color = MaterialTheme.colorScheme.primary
            )

            Text(text = "You are doing great today ✨",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                fontSize = 12.sp
                )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            HeaderIconButton(
                icon = Icons.Outlined.Visibility,
                onClick = onVisibilityToggle
            )
            HeaderIconButton(
                icon = Icons.Outlined.DarkMode,
                onClick = onThemeToggle
            )
        }
    }
}
