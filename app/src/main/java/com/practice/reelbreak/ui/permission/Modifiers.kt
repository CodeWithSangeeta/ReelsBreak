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
import com.practice.reelbreak.ui.theme.AppColors

fun Modifier.glassmorphism(
    appColors: AppColors,
    cornerRadius: Dp = 16.dp,
    borderWidth: Dp = 1.dp
) = this.then(

    Modifier
        .graphicsLayer {
            clip = true
            shape = RoundedCornerShape(cornerRadius)
        }
        .background(
            Brush.verticalGradient(
                colors = listOf(
                    appColors.textPrimary.copy(alpha = 0.15f),
                    appColors.textPrimary.copy(alpha = 0.05f)
                )
            )
        )
        .border(
            width = borderWidth,
            brush = Brush.verticalGradient(
                colors = listOf(
                    appColors.textPrimary.copy(alpha = 0.3f),
                    Color.Transparent
                )
            ),
            shape = RoundedCornerShape(cornerRadius)
        )
)