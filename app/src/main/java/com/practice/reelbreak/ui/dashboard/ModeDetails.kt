package com.practice.reelbreak.ui.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.reelbreak.ui.theme.LocalAppColors
@Composable
fun StrictDetails() {
    val colors = LocalAppColors.current
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "What Strict mode does",
            color = colors.textPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text =
                "• Instantly closes reels, shorts and TikToks as soon as you open them.\n" +
                        "• Lets you still use Instagram, YouTube, Facebook, etc. without the reels tab.\n" +
                        "• For reels‑only apps (like Moj, Josh) it blocks the entire app in Strict mode.",
            color = colors.textSecondary,
            fontSize = 12.sp,
            lineHeight = 17.sp
        )
    }
}

@Composable
fun LimitDetails() {
    val colors = LocalAppColors.current
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "What Limit mode does",
            color = colors.textPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text =
                "• Let yourself watch within a daily limit (reels or minutes).\n" +
                        "• After you hit the limit, reels are auto‑blocked for the rest of the day.",
            color = colors.textSecondary,
            fontSize = 12.sp,
            lineHeight = 17.sp
        )
    }
}

@Composable
fun SmartFilterDetails() {
    val colors = LocalAppColors.current
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "What Smart Filter does",
            color = colors.textPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text =
                "• Only allow reels from the creators you follow or whitelist.\n" +
                        "• Blocks all other random, addictive content from the explore feed.",
            color = colors.textSecondary,
            fontSize = 12.sp,
            lineHeight = 17.sp
        )
    }
}