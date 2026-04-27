package com.practice.reelbreak.ui.onboarding.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.practice.reelbreak.ui.theme.LocalAppColors

@Composable
fun OnboardHeading(text :String) {
    val colors = LocalAppColors.current
    Text(
        text = text,
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = colors.textPrimary,
        modifier = Modifier
            .fillMaxWidth()
            .semantics { heading() },
        lineHeight = 40.sp
    )
}