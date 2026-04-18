package com.practice.reelbreak.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// ── CompositionLocal — access anywhere via LocalAppColors.current ──
val LocalAppColors = staticCompositionLocalOf { darkAppColors() }

// ── Material3 schemes (for MaterialTheme.colorScheme.xxx fallback) ─
private fun darkMaterial() = darkColorScheme(
    background       = Color(0xFF0F0425),
    surface          = Color(0xFF1D0E42),
    primary          = Color(0xFFB490FF),
    onBackground     = Color.White,
    onSurface        = Color.White,
    secondary        = Color(0xFF7B5FCC),
    onPrimary        = Color.White,
    surfaceVariant   = Color(0xFF241B38),
    onSurfaceVariant = Color(0xFFB0B0CC)
)

private fun lightMaterial() = lightColorScheme(
    background       = Color(0xFFF8F7FF),
    surface          = Color(0xFFFFFFFF),
    primary          = Color(0xFF7C22E8),
    onBackground     = Color(0xFF0E0520),
    onSurface        = Color(0xFF0E0520),
    secondary        = Color(0xFF5A0EA8),
    onPrimary        = Color.White,
    surfaceVariant   = Color(0xFFF4F0FE),
    onSurfaceVariant = Color(0xFF5A5070)
)

/**
 * ReelBreakTheme — THE ONLY theme wrapper for the entire app.
 * Used once in MainActivity. Replaces Theme.kt and DashboardTheme.kt.
 */
@Composable
fun ReelBreakTheme(
    isDarkMode: Boolean,
    content: @Composable () -> Unit
) {
    val appColors = if (isDarkMode) darkAppColors() else lightAppColors()

    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = if (isDarkMode) darkMaterial() else lightMaterial(),
            typography  = Typography,
            content     = content
        )
    }
}