package com.sangeeta.reelsbreak.ui.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.reelsbreak.ui.theme.LocalAppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverlayPreviewBottomSheet(
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    val colors = LocalAppColors.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null,
        containerColor = if (colors.isDark) Color(0xFF12141A) else Color(0xFFF8F7FB)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OverlaySheetDragHandle()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp, end = 32.dp, top = 0.dp, bottom = 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Overlay Preview",
                    color = colors.textPrimary,
                    fontSize = 24.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.Bold
                )
             Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "This is how the overlay will appear when you open reels.",
                    color = colors.textSecondary,
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center
                )
            }

            OverlayPreviewPhoneMockup()

//            HorizontalDivider(
//                color = colors.borderSubtle.copy(alpha = if (colors.isDark) 0.34f else 0.10f),
//                thickness = 1.dp
//            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OverlayInfoRow(
                    icon = Icons.Outlined.NotificationsNone,
                    bgcolor = colors.purplePrimary.copy(alpha = if (colors.isDark) 0.22f else 0.12f),
                    tintcolor = colors.purplePrimary,
                    title = "Shows only on reels",
                    description = "The overlay appears only when a short-video reel screen is detected."
                )

                OverlayInfoRow(
                    icon = Icons.Outlined.Timer,
                    bgcolor = colors.blueAccent.copy(alpha = if (colors.isDark) 0.22f else 0.12f),
                    tintcolor = colors.blueAccent,
                    title = "Tracks your time & reels",
                    description = "It can show your reel count and watch time for the current session."
                )

                OverlayInfoRow(
                    icon = Icons.Outlined.Shield,
                    bgcolor = colors.successGreen.copy(alpha = if (colors.isDark) 0.22f else 0.12f),
                    tintcolor = colors.successGreen,
                    title = "Reminds when your limit is reached",
                    description = "When your set limit is reached, ReelBreak shows a reminder and can close the reel screen based on your selected mode."
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}



@Composable
private fun OverlaySheetDragHandle() {
    val colors = LocalAppColors.current

    Box(
        modifier = Modifier
            .padding(top = 6.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(42.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(colors.sheetDragHandle)
        )
    }
}

@Composable
private fun OverlayInfoRow(
    icon: ImageVector,
    bgcolor: Color,
    tintcolor: Color,
    title: String,
    description: String
) {
    val colors = LocalAppColors.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background( bgcolor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tintcolor,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                color = colors.textPrimary,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = description,
                color = colors.textSecondary,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )
        }
    }
}