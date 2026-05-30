package com.practice.reelbreak.core.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

//@Composable
//fun ReelBreakOverlayCard(
//    appLabel: String,
//    reelsText: String?,
//    timeText: String?,
//    periodLabel: String
//) {
//    Column(
//        verticalArrangement = Arrangement.spacedBy(4.dp),
//        modifier = Modifier
//            .background(
//                color = Color(0xCC111827),
//                shape = RoundedCornerShape(14.dp)
//            )
//            .padding(horizontal = 12.dp, vertical = 10.dp)
//    ) {
//        Text(
//            text = appLabel,
//            style = MaterialTheme.typography.labelLarge,
//            color = Color.White
//        )
//
//        reelsText?.let {
//            Text(
//                text = it,
//                style = MaterialTheme.typography.bodySmall,
//                color = Color(0xFFD1FAE5)
//            )
//        }
//
//        timeText?.let {
//            Text(
//                text = it,
//                style = MaterialTheme.typography.bodySmall,
//                color = Color(0xFFBFDBFE)
//            )
//        }
//
//        Text(
//            text = periodLabel,
//            style = MaterialTheme.typography.labelSmall,
//            color = Color(0xFF9CA3AF)
//        )
//    }
//}



import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Slideshow
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.reelbreak.R

@Composable
fun ReelBreakOverlayCard(
    reelsWatched: Int,
    reelLimit: Int,
    timeDisplay: String,
    showReels: Boolean,
    showTimer: Boolean
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(Color(0xF00D0D0D))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.15f),
                shape = RoundedCornerShape(50.dp)
            )
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // ── App Logo ──────────────────────────────────────
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "ReelBreak",
            tint = Color.Unspecified,
            modifier = Modifier.size(36.dp)
        )

        // ── Reel Counter ──────────────────────────────────
        if (showReels) {
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(36.dp)
                    .background(Color.White.copy(alpha = 0.18f))
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Circular ring icon around slideshow
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .border(
                            width = 2.dp,
                            color = Color.White.copy(alpha = 0.35f),
                            shape = RoundedCornerShape(50.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Slideshow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "$reelsWatched",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 22.sp
                    )
                    Text(
                        text = "REELS",
                        color = Color.White.copy(alpha = 0.50f),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        lineHeight = 10.sp
                    )
                }
            }
        }

        // ── Timer ─────────────────────────────────────────
        if (showTimer) {
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(36.dp)
                    .background(Color.White.copy(alpha = 0.18f))
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Circular ring around stopwatch icon
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .border(
                            width = 2.dp,
                            color = Color.White.copy(alpha = 0.35f),
                            shape = RoundedCornerShape(50.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Timer,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = timeDisplay,          // "MM:SS"
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 22.sp
                    )
                    Text(
                        text = "TIME LEFT",
                        color = Color.White.copy(alpha = 0.50f),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        lineHeight = 10.sp
                    )
                }
            }
        }
    }
}