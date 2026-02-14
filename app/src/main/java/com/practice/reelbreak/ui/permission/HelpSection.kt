package com.practice.reelbreak.ui.permission

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HelpSection() {
    var showSteps by remember { mutableStateOf(false) }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Icon(Icons.Default.HelpOutline, "Need Help?", tint = Color(0xFFFF6B6B), modifier = Modifier.size(20.dp))
            Text("Need Help?", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.White)
        }
        Text(
            text = "If the app closes after enabling permission...",
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.8f)
        )
        Text(
            text = "Some devices restart apps after enabling Accessibility. Simply reopen ReelsGuard – your settings will be saved.",
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.8f),
            modifier = Modifier.padding(top = 4.dp)
        )
        TextButton(onClick = { showSteps = !showSteps }) {
            Text(if (showSteps) "Hide steps" else "Show steps", color = Color(0xFF8E44AD))
        }
        if (showSteps) {
            Column(modifier = Modifier.padding(start = 16.dp, top = 8.dp)) {
                listOf(
                    "1. Tap Enable",
                    "2. Find ReelsGuard in Accessibility list",
                    "3. Turn ON the toggle",
                    "4. Press back to return to the app"
                ).forEach { step ->
                    Text(step, fontSize = 13.sp, color = Color.White.copy(alpha = 0.9f))
                }
            }
        }
    }
}