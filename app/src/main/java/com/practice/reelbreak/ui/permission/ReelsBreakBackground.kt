package com.practice.reelbreak.ui.permission

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun ReelsGuardBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1A1A2E), // Deep dark blue
                        Color(0xFF16213E), // Soft Navy
                        Color(0xFF0F3460)  // Hint of Teal
                    )
                )
            )
    ) {
        // Here we could add floating "glow blobs" later
        content()
    }
}