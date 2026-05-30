//package com.practice.reelbreak.ui.dashboard
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.practice.reelbreak.ui.theme.LocalAppColors
//
//@Composable
//fun PermissionPagerCard(
//    item: PermissionPagerItem,
//    isGranted: Boolean,
//    onClick: () -> Unit
//) {
//    val colors = LocalAppColors.current
//    val activeColor = if (isGranted) colors.successGreen else item.iconTint
//
//    Box(
//        modifier = Modifier
//            .padding(horizontal = 4.dp)
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(30.dp))
//            .background(colors.cardSurface)
//            .border(
//                width = 1.3.dp,
//                color = activeColor.copy(alpha = if (isGranted) 0.9f else 0.6f),
//                shape = RoundedCornerShape(30.dp)
//            )
//            .padding(horizontal = 18.dp, vertical = 18.dp)
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//
//            Box(
//                contentAlignment = Alignment.Center,
//                modifier = Modifier
//                    .size(56.dp)
//                    .clip(CircleShape)
//                    .background(
//                        Brush.radialGradient(
//                            colors = listOf(
//                                activeColor.copy(alpha = 0.22f),
//                                activeColor.copy(alpha = 0.06f)
//                            )
//                        )
//                    )
//                    .border(
//                        width = 1.2.dp,
//                        color = activeColor.copy(alpha = 0.4f),
//                        shape = CircleShape
//                    )
//            ) {
//                Icon(
//                    imageVector = item.icon,
//                    contentDescription = item.title,
//                    tint = activeColor,
//                    modifier = Modifier.size(26.dp)
//                )
//            }
//
//            Column(
//                modifier = Modifier.weight(1f),
//                verticalArrangement = Arrangement.spacedBy(6.dp)
//            ) {
//                Text(
//                    text = item.title,
//                    color = colors.textPrimary,
//                    fontSize = 15.sp,
//                    fontWeight = FontWeight.SemiBold,
//                    lineHeight = 20.sp
//                )
//                Text(
//                    text = item.description,
//                    color = colors.textSecondary,
//                    fontSize = 13.sp,
//                    lineHeight = 18.sp
//                )
//
//                Box(
//                    modifier = Modifier
//                        .align(Alignment.End)
//                        .clip(RoundedCornerShape(999.dp))
//                        .background(activeColor.copy(alpha = 0.14f))
//                        .border(
//                            width = 1.dp,
//                            color = activeColor.copy(alpha = 0.5f),
//                            shape = RoundedCornerShape(999.dp)
//                        )
//                        .clickable(
//                            interactionSource = remember { MutableInteractionSource() },
//                            indication = null,
//                            onClick = onClick
//                        )
//                        .padding(horizontal = 14.dp, vertical = 6.dp)
//                ) {
//                    Text(
//                        text = if (isGranted) "Enabled" else item.buttonText,
//                        color = activeColor,
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                }
//            }
//        }
//    }
//}



package com.practice.reelbreak.ui.dashboard

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.reelbreak.ui.theme.LocalAppColors

@Composable
fun PermissionPagerCard(
    item: PermissionPagerItem,
    isGranted: Boolean,
    onClick: () -> Unit
) {
    val colors = LocalAppColors.current

    // Smooth adaptive coloring: warning orange/purple if required, soft elegant green if active
    val activeColor = if (isGranted) colors.successGreen else colors.warningOrange
    val cardBorderColor = if (isGranted) colors.successGreen.copy(alpha = 0.25f) else colors.purplePrimary.copy(alpha = 0.20f)

    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(colors.cardSurface)
            .border(
                width = 1.dp,
                color = cardBorderColor,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically // perfectly centers items horizontally across the layout line
        ) {
            // 1. Icon Context Ring
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(activeColor.copy(alpha = 0.10f))
                    .border(
                        width = 1.dp,
                        color = activeColor.copy(alpha = 0.25f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = activeColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            // 2. Text Information Stack (Clean, Safe Wording)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = if (isGranted) "Shield Active" else "Setup Required",
                    color = colors.textPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 20.sp
                )

                Text(
                    text = if (isGranted)
                        "ReelBreak is actively guarding your screen time layout."
                    else
                        "Enable accessibility tracking to automate your mindful scrolling limits.",
                    color = colors.textSecondary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }

            // 3. Compact Action Button (Anchored securely to the right side)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isGranted) colors.successGreen.copy(alpha = 0.12f)
                        else colors.purplePrimary.copy(alpha = 0.14f)
                    )
                    .border(
                        width = 1.dp,
                        color = if (isGranted) colors.successGreen.copy(alpha = 0.3f) else colors.purplePrimary.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .then(
                        if (!isGranted) Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onClick
                        ) else Modifier
                    )
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isGranted) "Active" else "Fix Now",
                    color = if (isGranted) colors.successGreen else colors.purpleSoft,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}