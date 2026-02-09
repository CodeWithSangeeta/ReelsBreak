package com.practice.reelbreak.ui.permission

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.glassmorphism(
    cornerRadius: Dp = 24.dp,
    borderWidth: Dp = 1.dp
) = this.then(
    Modifier
        .graphicsLayer {
            clip = true
            shape = RoundedCornerShape(cornerRadius)
            // Note: Real-time blur (RenderEffect) requires Android 12+.
            // For older versions, we use high transparency.
        }
        .background(
            Brush.verticalGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.15f),
                    Color.White.copy(alpha = 0.05f)
                )
            )
        )
        .border(
            width = borderWidth,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.3f),
                    Color.Transparent
                )
            ),
            shape = RoundedCornerShape(cornerRadius)
        )
)