package com.practice.reelbreak.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.reelbreak.ui.theme.LocalAppColors

@Composable
fun AboutSection(
    appVersion: String,
    onShare: () -> Unit,
    onRate: () -> Unit,
    onFeedback: () -> Unit,
    onContact: () -> Unit,
    onPrivacyPolicy: () -> Unit
) {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(colors.cardSurface)
            .border(1.dp, colors.borderSubtle, RoundedCornerShape(18.dp))
    ) {
        Column {
            AboutRow(icon = Icons.Filled.Share,    iconTint = colors.blueAccent,  title = "Share ReelsBreak",    subtitle = "Invite friends to take a break", onClick = onShare)
            RowDivider(horizontal = 16.dp)
            AboutRow(icon = Icons.Filled.Star,     iconTint = Color(0xFFFFCC00),  title = "Rate on Play Store",  subtitle = "Your review means a lot ⭐",    onClick = onRate)
            RowDivider(horizontal = 16.dp)
            AboutRow(icon = Icons.Filled.Feedback, iconTint = colors.purpleSoft,  title = "Send Feedback",       subtitle = "Suggest features or improvements", onClick = onFeedback)
            RowDivider(horizontal = 16.dp)
            AboutRow(icon = Icons.Filled.Mail,     iconTint = colors.successGreen,title = "Contact Us",          subtitle = "Get help or report an issue",    onClick = onContact)
            RowDivider(horizontal = 16.dp)
            AboutRow(icon = Icons.Filled.Security, iconTint = colors.textSecondary, title = "Privacy Policy",   subtitle = "How we handle your data",         onClick = onPrivacyPolicy)
            RowDivider(horizontal = 16.dp)

            // Version row — non-tappable
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "App Version", color = colors.textSecondary, fontSize = 13.sp)
                Text(text = "v$appVersion", color = colors.textMuted, fontSize = 13.sp)
            }
        }
    }
}