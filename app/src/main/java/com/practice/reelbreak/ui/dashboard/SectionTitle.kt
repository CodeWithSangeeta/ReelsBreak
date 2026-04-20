package com.practice.reelbreak.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.reelbreak.ui.theme.LocalAppColors

@Composable
fun SectionTitle(title: String, subtitle: String) {
    val colors = LocalAppColors.current
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = subtitle,
            color = colors.textSecondary,
            fontSize = 12.sp
        )
    }
}