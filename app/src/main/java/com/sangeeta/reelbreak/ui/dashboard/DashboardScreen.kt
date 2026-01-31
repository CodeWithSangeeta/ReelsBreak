package com.sangeeta.reelbreak.ui.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

@Composable
fun DashboardScreen() {
   // Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            DashboardHeader(
                userName = "Sangeeta",
                onToggleTheme = {},
                onToggleCounter = {}
            )

            // TODO: ReelsSummaryCard
            // TODO: TimeSpentCard

            Row(horizontalArrangement = Arrangement.Center) {
                // Example usage in your Dashboard
                Row(modifier = Modifier.fillMaxWidth()) {
                    ActionCard(
                        title = "Analytics",
                        icon = Icons.Default.ThumbUp,
                        onClick = {  },
                        modifier = Modifier.weight(1f)
                    )

                    ActionCard(
                        title = "Analytics",
                        icon = Icons.Default.Star,
                        onClick = { },
                        modifier = Modifier.weight(1f)
                    )
                }

            }

            Row(modifier = Modifier.fillMaxWidth()) {
                ActionCard(
                    title = "Analytics",
                    icon = Icons.Default.ThumbUp,
                    onClick = { },
                    modifier = Modifier.weight(1f)
                )

                ActionCard(
                    title = "Analytics",
                    icon = Icons.Default.Star,
                    onClick = {},
                    modifier = Modifier.weight(1f)
                )
            }

       FloatingNavBar()
    }
}
