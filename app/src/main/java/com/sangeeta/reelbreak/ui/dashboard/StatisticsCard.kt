package com.sangeeta.reelbreak.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.format.TextStyle

@Composable
fun StatisticsCard(
    state: DashboardState,
    isDarkMode: Boolean
) {
    // 1. Define the Brushes
    val textGradient = Brush.horizontalGradient(listOf(Color(0xFFB490FF), Color(0xFF53D9FF)))

    // 2. Dynamic Colors based on Theme
    val cardBg = if (isDarkMode) Color(0xFF1D0E42) else Color.White
    val subCardBg = if (isDarkMode) Color.White.copy(alpha = 0.05f) else Color(0xFFF5F5F5)
    val textColor = if (isDarkMode) Color.White else Color.Black

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(if (isDarkMode) 0.dp else 4.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left side: Count and Percentage
                Column {
                    Text("Today's Reels", color = Color.Gray, fontSize = 14.sp)
                    Text(
                        text = "${state.reelsCount}",
                        style = TextStyle(brush = textGradient),
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "↑ ${state.percentageIncrease}% vs yesterday",
                        color = Color(0xFFE57373),
                        fontSize = 12.sp
                    )
                }

                // Right side: Circular Progress (Box to stack layers)
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { 1f },
                        modifier = Modifier.size(100.dp),
                        color = textColor.copy(alpha = 0.1f),
                        strokeWidth = 8.dp,
                        strokeCap = StrokeCap.Round
                    )
                    CircularProgressIndicator(
                        progress = { (state.limitProgress).coerceAtMost(1f) },
                        modifier = Modifier.size(100.dp),
                        color = Color(0xFF53D9FF),
                        strokeWidth = 8.dp,
                        strokeCap = StrokeCap.Round
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("${(state.limitProgress * 100).toInt()}%", color = textColor, fontWeight = FontWeight.Bold)
                        Text("of limit", color = Color.Gray, fontSize = 10.sp)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Nested Time Spent Section
            Surface(
                color = subCardBg,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Schedule, null, tint = Color(0xFFB490FF), modifier = Modifier.size(32.dp))
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Time Spent Today", color = Color.Gray, fontSize = 12.sp)
                        Text(state.timeSpent, color = textColor, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.weight(1f))
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Limit: ${state.dailyLimit}", color = Color.Gray, fontSize = 12.sp)
                        if (state.isOverLimit) Text("Over limit!", color = Color(0xFFE57373), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}