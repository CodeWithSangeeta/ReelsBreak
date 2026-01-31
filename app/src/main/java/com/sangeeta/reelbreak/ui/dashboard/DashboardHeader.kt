package com.sangeeta.reelbreak.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardHeader(
    userName: String,
    isDarkMode: Boolean,
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
                color = if (isDarkMode) Color.White else Color.Black
            )
            Text(
                text = "Stay mindful, $userName.",
                color = if (isDarkMode) Color(0xFFB490FF) else Color(0xFF6200EE)
            )
            Text(text = "You are doing great today ✨", color = Color.Gray, fontSize = 12.sp)
        }

        // Theme Toggle Buttons
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // eye icon button...
            // theme icon button (Sun/Moon)...
        }
    }
}
