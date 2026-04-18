package com.practice.reelbreak.ui.focused_mode

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.reelbreak.ui.theme.LocalAppColors

@Composable
fun QuoteSection() {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(colors.cardSurface)
            .border(1.dp, colors.borderSubtle, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "💡", fontSize = 24.sp)
            Text(
                text = "\"The secret of getting ahead is getting started.\"",
                color = colors.textSecondary,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
