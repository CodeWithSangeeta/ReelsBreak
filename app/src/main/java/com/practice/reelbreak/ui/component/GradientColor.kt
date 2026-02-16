package com.practice.reelbreak.ui.component

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object GradientColor {
    val background = Brush.Companion.linearGradient(
        colors = listOf(
            Color(0xFF000000),
            Color(0xFF29083D),
            Color(0xFF000000),
        ),
        start = Offset(0f, 0f),
        end = Offset(1400f, 2000f)
    )

    val button = Brush.Companion.linearGradient(
        colors = listOf(
            Color(0xFF8B2CE8),
            Color(0xFF581176),
            Color(0xFF263CB3)
        )
    )
}