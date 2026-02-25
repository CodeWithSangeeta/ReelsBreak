package com.practice.reelbreak.ui.focused_mode

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Reusable: AppBlockingCard.kt
@Composable
fun AppBlockingCard(
    blockedApps: Set<String>,
    onAppToggled: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .padding(vertical = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4A1C7A).copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(25.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Block these apps:",
                color = Color(0xFFB0B0D0),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            AppGrid(apps = blockedApps, onAppToggled = onAppToggled)
        }
    }
}

@Composable
private fun AppGrid(
    apps: Set<String>,
    onAppToggled: (String) -> Unit
) {
    val appList = listOf("Instagram", "YouTube", "Facebook", "TikTok")
//
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(2),
//        horizontalArrangement = Arrangement.spacedBy(12.dp),
//        verticalArrangement = Arrangement.spacedBy(12.dp),
//        modifier = Modifier.padding(8.dp)
//    ) {
//        items(appList) { app ->
//            AppPillButton(
//                appName = app,
//                isBlocked = apps.contains(app),
//                onToggle = { onAppToggled(app) }
//            )
//        }
//    }
}
