package com.practice.reelbreak.ui.focused_mode

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// Duration option model
data class DurationOption(
val minutes: Int,
val label: String,
val sublabel: String
)

private val durationOptions = listOf(
    DurationOption(15,   "15",   "min"),
    DurationOption(30,   "30",   "min"),
    DurationOption(45,   "45",   "min"),
    DurationOption(60,   "1",    "hour"),
    DurationOption(90,   "1.5",  "hours"),
    DurationOption(120,  "2",    "hours"),
    DurationOption(180,  "3",    "hours"),
    DurationOption(300,  "5",    "hours"),
    DurationOption(480,  "8",    "hours"),
    DurationOption(720,  "12",   "hours"),
    DurationOption(1440, "1",    "day"),
    DurationOption(2880, "2",    "days"),
    DurationOption(7200, "5",    "days"),
)

@Composable
fun TimerSelectorRow(
    selectedMinutes: Int,
    enabled: Boolean,
    onSelect: (Int) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 2.dp)
    ) {
        items(durationOptions) { option ->
            val isSelected = option.minutes == selectedMinutes

            Box(
                modifier = Modifier
                    .width(68.dp)
                    .height(72.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (isSelected)
                            Brush.linearGradient(
                                listOf(Color(0xFF7C3AED), Color(0xFF4F46E5))
                            )
                        else
                            Brush.linearGradient(
                                listOf(Color(0xFF1C1233), Color(0xFF140B26))
                            )
                    )
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) Color(0xFFB794F4)
                        else Color(0xFF2D1B4E),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .then(
                        if (enabled) Modifier.clickable { onSelect(option.minutes) }
                        else Modifier
                    )
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = option.label,
                        color = if (isSelected) Color.White
                        else Color.White.copy(alpha = 0.4f),
                        fontSize = 20.sp,
                        fontWeight = if (isSelected) FontWeight.Bold
                        else FontWeight.Normal
                    )
                    Text(
                        text = option.sublabel,
                        color = if (isSelected) Color(0xFFE9D5FF)
                        else Color.White.copy(alpha = 0.3f),
                        fontSize = 11.sp
                    )
                }

                // Dot indicator at bottom when selected
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 5.dp)
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFD8B4FE))
                    )
                }
            }
        }
    }
}