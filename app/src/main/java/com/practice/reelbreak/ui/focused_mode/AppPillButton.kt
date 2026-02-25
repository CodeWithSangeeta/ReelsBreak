package com.practice.reelbreak.ui.focused_mode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Reusable: AppPillButton.kt
@Composable
fun AppPillButton(
    appName: String,
    isBlocked: Boolean,
    onToggle: () -> Unit
) {
    val gradient = when (appName) {
        "Instagram" -> Brush.linearGradient(0f to Color(0xFFE1306C), 1f to Color(0xFFF58529))
        "YouTube" -> Brush.linearGradient(0f to Color(0xFFFF0000), 1f to Color(0xFFCC0000))
        "Facebook" -> Brush.linearGradient(0f to Color(0xFF1877F2), 1f to Color(0xFF42A5F5))
        "TikTok" -> Brush.linearGradient(0f to Color.Black, 1f to Color(0xFF00F5D4))
        else -> Brush.linearGradient(0f to Color.Gray, 1f to Color.LightGray)
    }

    Button(
        onClick = onToggle,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .clip(RoundedCornerShape(24.dp))
        ) {
            Text(
                text = appName,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
