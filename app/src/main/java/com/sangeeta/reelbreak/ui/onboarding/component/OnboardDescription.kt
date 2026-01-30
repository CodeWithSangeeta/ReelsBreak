package com.sangeeta.reelbreak.ui.onboarding.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun OnboardDescription(text : String) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        color = Color(0xFFAAA4A4),
        textAlign = TextAlign.Center
    )
}