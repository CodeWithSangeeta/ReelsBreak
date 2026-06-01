package com.sangeeta.reelsbreak.ui.dashboard

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.reelsbreak.R
import com.sangeeta.reelsbreak.ui.theme.LocalAppColors

@Composable
fun PermissionPagerCard(
    item: PermissionPagerItem,
    isGranted: Boolean,
    onClick: () -> Unit
) {
    if (isGranted) return

    val colors = LocalAppColors.current

    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(colors.cardSurface)
            .border(
                width = 1.dp,
                color = if (colors.isDark) {
                    Color.White.copy(alpha = 0.06f)
                } else {
                    Color.Black.copy(alpha = 0.06f)
                },
                shape = RoundedCornerShape(24.dp)
            )
            .padding(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.End
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ReelBreakAnimatedLogo()

                    Text(
                        text = "Need Accessibility Access",
                        color = colors.textPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 22.sp
                    )
            }
            Text(
                text = "ReelBreak uses Android's Accessibility feature to detect " +
                        "short-video screens and automatically navigate away from them, " +
                        "helping you stay focused and in control of your screen time.",
                color = colors.textSecondary,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        if (colors.isDark) {
                            Color.White.copy(alpha = 0.06f)
                        } else {
                            Color.Black.copy(alpha = 0.06f)
                        }
                    )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.appColor)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.buttonText,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun ReelBreakAnimatedLogo() {
    val infiniteTransition = rememberInfiniteTransition(label = "permission_logo_pulse")

    val scale = infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "permission_logo_scale"
    ).value

    Image(
        painter = painterResource(id = R.drawable.applogo),
        contentDescription = "ReelBreak logo",
        modifier = Modifier
            .size(56.dp)
            .scale(scale)
    )
}