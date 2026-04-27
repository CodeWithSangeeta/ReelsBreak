package com.practice.reelbreak.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.reelbreak.ui.permission.PermissionSheetType
import com.practice.reelbreak.ui.theme.LocalAppColors

@Composable
fun PermissionDetailSheet(
    type: PermissionSheetType,
    isGranted: Boolean,
    onOpenSettings: () -> Unit,
    onDismiss: () -> Unit
) {
    val colors = LocalAppColors.current

    val (title, description, whyNeeded, howToEnable) = when (type) {
        PermissionSheetType.ACCESSIBILITY -> PermissionSheetData(
            title = "Accessibility Service",
            description = "ReelsBreak uses the Accessibility API to detect when you open a reels or shorts screen inside Instagram, YouTube, TikTok, Snapchat, or Facebook.",
            whyNeeded = "This is the ONLY way an Android app can know you're watching reels without screen recording. It reads the UI layout — not your content.",
            howToEnable = "Settings → Accessibility → Installed Services → ReelsBreak → Toggle ON"
        )
        PermissionSheetType.USAGE_ACCESS -> PermissionSheetData(
            title = "Usage Access",
            description = "ReelsBreak reads daily usage time per app to show your screen time stats and enforce time-based limits.",
            whyNeeded = "Without this permission, the app cannot track how much time you've spent and the Limit mode will not work.",
            howToEnable = "Settings → Digital Wellbeing → Usage Access → ReelsBreak → Allow"
        )
        PermissionSheetType.OVERLAY -> PermissionSheetData(
            title = "Display Overlay",
            description = "Allows ReelsBreak to show a full-screen block overlay on top of other apps when your limit is reached or in Strict mode.",
            whyNeeded = "This is optional. Without it, the app redirects you using the Back button instead of showing a custom overlay screen.",
            howToEnable = "Settings → Apps → ReelsBreak → Display over other apps → Allow"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Title row with status
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = title, color = colors.textPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isGranted) colors.successGreen.copy(0.15f)
                        else Color(0xFFFF9800).copy(0.15f)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (isGranted) "✓ Active" else "✗ Not granted",
                    color = if (isGranted) colors.successGreen else Color(0xFFFF9800),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Description
        Text(text = description, color = colors.textSecondary, fontSize = 13.sp, lineHeight = 19.sp)

        // Why needed block
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x22B77CFF))
                .border(1.dp, colors.borderPurple, RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "Why is this needed?", color = colors.purpleSoft, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Text(text = whyNeeded, color = colors.textSecondary, fontSize = 12.sp, lineHeight = 17.sp)
            }
        }

        // How to enable/disable
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x11FFFFFF))
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = if (isGranted) "How to disable:" else "How to enable:",
                    color = colors.textSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(text = howToEnable, color = colors.textMuted, fontSize = 12.sp, lineHeight = 17.sp)
            }
        }

        // Action button — only show if not granted
        if (!isGranted) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .shadow(8.dp, RoundedCornerShape(14.dp), spotColor = colors.glowPurple)
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.button)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onOpenSettings
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Go to Settings →", color = colors.textPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}