package com.sangeeta.reelsbreak.ui.focused_mode

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.reelsbreak.ui.theme.LocalAppColors

@Composable
 fun TimerCard(
    remainingMillis: Long,
    isActive: Boolean,
    progress: Float,
    isFocusActive: Boolean,
    onToggle: () -> Unit
) {
    val colors = LocalAppColors.current


    Card(
        modifier = Modifier.fillMaxWidth(),
        padding  = PaddingValues(20.dp)
    ) {
        Column(
            modifier            = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            TimerCircle(
                remainingMillis = remainingMillis,
                isActive        = isActive,
                progress        = progress
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .shadow(
                        elevation    = 8.dp,
                        shape        = RoundedCornerShape(999.dp),
                        ambientColor = if (isFocusActive) colors.successGreen.copy(alpha = 0.20f) else colors.glowPurple,
                        spotColor    = if (isFocusActive) colors.successGreen.copy(alpha = 0.20f) else colors.glowPurple
                    )
                    .clip(RoundedCornerShape(999.dp))
                    .background(if(isFocusActive) colors.borderColor else colors.appColor)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication        = null,
                        onClick           = onToggle
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector        = if (isFocusActive) Icons.Filled.Stop else Icons.Filled.PlayArrow,
                        contentDescription = null,
                        tint               = Color.White,
                        modifier           = Modifier.size(18.dp)
                    )
                    Text(
                        text       = if (isFocusActive) "End focus session" else "Start focus session",
                        color      = Color.White,
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}



@Composable
private fun TimerCircle(
    remainingMillis: Long,
    isActive: Boolean,
    progress: Float
) {
    val colors = LocalAppColors.current
    val animatedProgress by animateFloatAsState(
        targetValue   = progress,
        animationSpec = tween(800),
        label         = "timerProgress"
    )

    val totalSeconds = remainingMillis / 1000L
    val hours        = totalSeconds / 3600
    val minutes      = (totalSeconds % 3600) / 60
    val seconds      = totalSeconds % 60

    val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    Box(
        modifier         = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier         = Modifier.size(160.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(160.dp)) {
                val strokeW = 11.dp.toPx()
                val arcSz   = size.width - strokeW

                drawArc(
                    brush = Brush.verticalGradient(
                        colors = if (colors.isDark)
                            listOf(
                                Color(0xFF4C1D95).copy(alpha = 0.22f),
                                Color(0xFF2E1065).copy(alpha = 0.22f)
                            )
                                //Color(0xFF7C3AED).copy(alpha = 0.25f), Color(0xFF4C1D95).copy(alpha = 0.25f))
                        else
                            listOf(
                                Color(0xFF4A2070).copy(alpha = 0.16f),
                                Color(0xFF2E1346).copy(alpha = 0.16f)
                            )
                              //  Color(0xFF6B3FA0).copy(alpha = 0.18f), Color(0xFF4A2070).copy(alpha = 0.18f))
                    ),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter  = false,
                    topLeft    = Offset(strokeW / 2, strokeW / 2),
                    size       = Size(arcSz, arcSz),
                    style      = Stroke(width = strokeW, cap = StrokeCap.Round)
                )

                drawArc(
                    brush = Brush.verticalGradient(
                        colors = if (colors.isDark)
                            listOf(
                                Color(0xFFA78BFA),
                                Color(0xFF7C3AED)
                            )
                        else
                            listOf(
                                Color(0xFFB39DDB),
                                Color(0xFF6B3FA0)
                            )
                    ),
                    startAngle = -90f,
                    sweepAngle = 360f * animatedProgress,
                    useCenter  = false,
                    topLeft    = Offset(strokeW / 2, strokeW / 2),
                    size       = Size(arcSz, arcSz),
                    style      = Stroke(width = strokeW, cap = StrokeCap.Round)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text          = if (isActive) timeString else "00:00:00",
                    color         = if (isActive) colors.textPrimary else colors.textPrimary.copy(alpha = 0.35f),
                    fontSize      = 24.sp,
                    fontWeight    = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Text(
                    text       = if (isActive) "Time remaining" else "Ready to start",
                    color      = colors.textSecondary,
                    fontSize   = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}