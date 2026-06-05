package com.sangeeta.reelsbreak.ui.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.reelsbreak.R


@Composable
fun OverlayPreviewCard(
    reelsWatched: Int,
    timeDisplay: String,
    showReels: Boolean,
    showTimer: Boolean,
    modifier: Modifier = Modifier
) {
    val glassBg = Color(0xD90D0D0D)
    val brandingPurple = Color(0xFFA78BFA).copy(alpha = 0.78f)
    val borderColor = Color.White.copy(alpha = 0.14f)
    val dividerColor = Color.White.copy(alpha = 0.12f)

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(36.dp))
            .background(glassBg)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(36.dp)
            )
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.applogo),
            contentDescription = "ReelBreak",
            tint = Color.Unspecified,
            modifier = Modifier.size(26.dp)
        )

        if (showReels) {
            OverlayPreviewDivider(color = dividerColor)

            OverlayPreviewMetric(
                value = reelsWatched.toString(),
                label = "REELS",
                labelColor = brandingPurple
            )
        }

        if (showTimer) {
            OverlayPreviewDivider(color = dividerColor)

            OverlayPreviewMetric(
                value = timeDisplay,
                label = "TIMER",
                labelColor = brandingPurple
            )
        }
    }
}


@Composable
private fun OverlayPreviewMetric(
    value: String,
    label: String,
    labelColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = value,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 16.sp
        )

        Text(
            text = label,
            color = labelColor,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            lineHeight = 9.sp
        )
    }
}

@Composable
private fun OverlayPreviewDivider(
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(1.dp)
            .height(24.dp)
            .background(color)
    )
}