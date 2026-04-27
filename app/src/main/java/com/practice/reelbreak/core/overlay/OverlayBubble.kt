package com.practice.reelbreak.core.overlay


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OverlayBubble(
    reelsCount: Int,
    minutes: Int
) {
    Column(
        modifier = Modifier
            .background(Color.Red, RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Reels: $reelsCount",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Time: ${minutes}m",
            color = Color(0xFFBBBBBB),
            fontSize = 11.sp
        )
    }
}
