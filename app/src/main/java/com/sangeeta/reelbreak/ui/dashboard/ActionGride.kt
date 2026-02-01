package com.sangeeta.reelbreak.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ActionGrid(isDarkMode: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            ActionCard(
                title = "Analytics",
                icon = Icons.Outlined.BarChart,
                iconColor = Color(0xFF4A90E2),
                isDarkMode = isDarkMode,
                onClick = { },
                modifier = Modifier.weight(1f)
            )
            ActionCard(
                title = "Focus Mode",
                icon = Icons.Outlined.Shield,
                iconColor = Color(0xFF4CAF50),
                isDarkMode = isDarkMode,
                onClick = { },
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            ActionCard(
                title = "Set Limit",
                icon = Icons.Outlined.TrackChanges,
                iconColor = Color(0xFF33D1FF),
                isDarkMode = isDarkMode,
                onClick = {},
                modifier = Modifier.weight(1f)
            )
            ActionCard(
                title = "Smart Alerts",
                icon = Icons.Outlined.NotificationsActive,
                iconColor = Color(0xFFFF9800),
                isDarkMode = isDarkMode,
                onClick = {},
                modifier = Modifier.weight(1f)
            )
        }
    }
}