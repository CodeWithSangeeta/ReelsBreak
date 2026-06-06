package com.sangeeta.reelsbreak.ui.overlay

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
import com.sangeeta.reelsbreak.ui.theme.LocalAppColors

@Composable
fun ReelBreakOverlayCard(
    reelsWatched: Int,
    reelLimit: Int,
    timeDisplay: String,
) {
    val colors = LocalAppColors.current
    val glassBg = Color(0xD90D0D0D)


    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(glassBg)
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.15f),
                shape = RoundedCornerShape(50.dp)
            )
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Icon(
            painter = painterResource(id = R.drawable.applogo),
            contentDescription = "ReelsBreak",
            tint = Color.Unspecified,
            modifier = Modifier.size(34.dp)
        )

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
                        color = colors.purpleSoft.copy(alpha = 0.75f),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        lineHeight = 10.sp
                    )
                }
            }

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
                        color = colors.purpleSoft.copy(alpha = 0.75f),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        lineHeight = 10.sp
                    )
            }
        }
    }
}

