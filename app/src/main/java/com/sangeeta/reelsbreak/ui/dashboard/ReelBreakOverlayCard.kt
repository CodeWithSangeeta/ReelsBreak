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
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
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
    // Premium, deep translucent charcoal glass layer (85% opacity)
    // Safe for Play Store guidelines as it doesn't aggressively blind or fully obstruct content
    val glassBg = Color(0xD90D0D0D)

    // Soft, premium neon purple hue for subtitle metrics to retain app identity elegantly
    val brandingPurple = Color(0xFFA78BFA).copy(alpha = 0.75f)

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(glassBg)
            .border(
                width = 1.dp,
                // Soft translucent border that catches light without looking harsh
                color = Color.White.copy(alpha = 0.15f),
                shape = RoundedCornerShape(50.dp)
            )
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Icon(
            painter = painterResource(id = R.drawable.applogo),
            contentDescription = "ReelBreak",
            tint = Color.Unspecified,
            modifier = Modifier.size(34.dp)
        )

      //  if (showReels) {
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(32.dp)
                    .background(Color.White.copy(alpha = 0.15f))
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "$reelsWatched",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 20.sp
                    )
                    Text(
                        text = "REELS",
                        color = brandingPurple,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        lineHeight = 10.sp
                    )
                }
            }
   //     }

  //      if (showTimer) {
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(32.dp)
                    .background(Color.White.copy(alpha = 0.15f))
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = timeDisplay,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 20.sp
                    )
                    Text(
                        text = "TIMER",
                        color = brandingPurple,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        lineHeight = 10.sp
                    )
     //           }
            }
        }
    }
}