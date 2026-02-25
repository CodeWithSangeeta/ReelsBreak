package com.practice.reelbreak.ui.focused_mode

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Reusable: TimerOptionRow.kt
@Composable
fun TimerOptionRow(
    selectedTime: Long,
    onTimeSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val times = listOf(15, 30, 45, 60)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        times.forEach { minutes ->
            val isSelected = selectedTime == minutes * 60 * 1000L
            OutlinedButton(
                onClick = { onTimeSelected(minutes) },
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(20.dp)),
                border = BorderStroke(
                    if (isSelected) 2.dp else 1.dp,
                    Color(0xFFB77CFF)
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isSelected) Color(0xFFB77CFF).copy(alpha = 0.2f) else Color.Transparent
                )
            ) {
                Text(
                    text = "${minutes}m",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }
    }
}
