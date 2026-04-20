package com.practice.reelbreak.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.reelbreak.ui.theme.LocalAppColors

@Composable
fun PrivacyDataContent() {
    val colors = LocalAppColors.current
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "What ReelsBreak accesses",
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(2.dp))

        PrivacyItem(isAllowed = true,  text = "Which app is currently open (foreground detection)")
        PrivacyItem(isAllowed = true,  text = "Daily usage time per app (via Usage Access)")
        PrivacyItem(isAllowed = true,  text = "Accessibility events to detect reel screens")

        Spacer(Modifier.height(6.dp))
        Text(
            text = "What we never access",
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(2.dp))

        PrivacyItem(isAllowed = false, text = "Messages, DMs, or any content inside apps")
        PrivacyItem(isAllowed = false, text = "Screen recording or screenshots")
        PrivacyItem(isAllowed = false, text = "Location, contacts, or camera")
        PrivacyItem(isAllowed = false, text = "Your personal account information")

        Spacer(Modifier.height(8.dp))
        Text(
            text = "All data stays on your device. Nothing is sent to any server.",
            color = colors.textMuted,
            fontSize = 11.sp,
            lineHeight = 15.sp
        )
    }
}