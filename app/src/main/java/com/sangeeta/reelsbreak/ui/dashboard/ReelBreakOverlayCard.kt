package com.sangeeta.reelsbreak.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Slideshow
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sangeeta.reelsbreak.R

@Composable
fun ReelBreakOverlayCard(
    reelsWatched: Int,
    reelLimit: Int,
    timeDisplay: String,
    showReels: Boolean,
    showTimer: Boolean
) {
   val  appColor = Brush.linearGradient(listOf(Color(0xFF6B3FA0), Color(0xFF4A2070)))
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(appColor)
            .border(
                width = 1.dp,
              //  color = Color.White.copy(alpha = 0.15f),
                color = Color.White,
                shape = RoundedCornerShape(50.dp)
            )
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Icon(
            painter = painterResource(id = R.drawable.applogo),
            contentDescription = "ReelBreak",
            tint = Color.Unspecified,
            modifier = Modifier.size(36.dp)
        )

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
//                Box(
//                    modifier = Modifier
//                        .size(36.dp)
//                        .border(
//                            width = 2.dp,
//                            color = Color.White.copy(alpha = 0.35f),
//                            shape = RoundedCornerShape(50.dp)
//                        ),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Icon(
//                        imageVector = Icons.Outlined.Slideshow,
//                        contentDescription = null,
//                        tint = Color.White,
//                        modifier = Modifier.size(20.dp)
//                    )
//                }

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
//                Box(
//                    modifier = Modifier
//                        .size(36.dp)
//                        .border(
//                            width = 2.dp,
//                            color = Color.White.copy(alpha = 0.35f),
//                            shape = RoundedCornerShape(50.dp)
//                        ),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Icon(
//                        imageVector = Icons.Outlined.Timer,
//                        contentDescription = null,
//                        tint = Color.White,
//                        modifier = Modifier.size(20.dp)
//                    )
//                }

                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = timeDisplay,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 22.sp
                    )
                    Text(
                        text = "TIMER",
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