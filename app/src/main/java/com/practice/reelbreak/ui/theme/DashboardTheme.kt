package com.practice.reelbreak.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val DarkDashboardColors = darkColorScheme(
    background = Color(0xFF0F0425),
    surface = Color(0xFF1D0E42),
    primary = Color(0xFFB490FF),
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightDashboardColors = lightColorScheme(
    background = Color.White,
    surface = Color(0xFFF5F5F5),
    primary = Color(0xFF6200EE),
    onBackground = Color.Black,
    onSurface = Color.Black
)
@Composable
fun DashboardTheme(
    isDarkMode: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (isDarkMode) DarkDashboardColors else LightDashboardColors,
        typography = MaterialTheme.typography,
        content = content
    )
}
