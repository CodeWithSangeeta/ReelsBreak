package com.practice.reelbreak.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * AppColors — SINGLE SOURCE OF TRUTH for all colors in ReelBreak.
 *
 * WHY this file?
 * - Replaces GradientColor.kt, DashboardTheme.kt, Color.kt
 * - One place to change any color across the whole app
 * - isDark flag lets composables adapt without passing booleans everywhere
 */
data class AppColors(
    val isDark: Boolean,
    // ── Text ────────────────────────────────────────────────
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    // ── Surfaces ────────────────────────────────────────────
    val background: Brush,
    val cardSurface: Brush,
    val glassSurface: Brush,
    // ── Brand ───────────────────────────────────────────────
    val purplePrimary: Color,
    val purpleDeep: Color,
    val purpleSoft: Color,
    val blueAccent: Color,
    val successGreen: Color,
    val warningOrange: Color,
    val errorRed: Color,
    // ── Buttons ─────────────────────────────────────────────
    val button: Brush,
    val buttonDanger: Brush,
    val buttonSuccess: Brush,
    // ── Nav ─────────────────────────────────────────────────
    val navSelected: Brush,
    // ── Mode cards ──────────────────────────────────────────
    val modeBlock: Brush,
    val modeLimit: Brush,
    val modeSmart: Brush,
    // ── Borders & glows ─────────────────────────────────────
    val borderSubtle: Color,
    val borderPurple: Color,
    val borderActive: Color,
    val glowPurple: Color,
    val glowBlue: Color,
    val glowTeal: Color,
    val glowRed: Color
)

fun darkAppColors() = AppColors(
    isDark          = true,
    textPrimary     = Color(0xFFFFFFFF),
    textSecondary   = Color(0xFFB0B0CC),
    textMuted       = Color(0xFF6B6880),
    background      = Brush.linearGradient(
        colors = listOf(Color(0xFF000000), Color(0xFF1A0530), Color(0xFF29083D), Color(0xFF1A0530), Color(0xFF000000)),
        start  = Offset(0f, 0f), end = Offset(1080f, 2400f)
    ),
    cardSurface     = Brush.linearGradient(
        colors = listOf(Color(0xFF1A1228), Color(0xFF120D1E)),
        start  = Offset(0f, 0f), end = Offset(400f, 400f)
    ),
    glassSurface    = Brush.linearGradient(colors = listOf(Color(0x22FFFFFF), Color(0x0AFFFFFF))),
    purplePrimary   = Color(0xFF9B3DFF),
    purpleDeep      = Color(0xFF5A0EA8),
    purpleSoft      = Color(0xFFB77CFF),
    blueAccent      = Color(0xFF3A5BF0),
    successGreen    = Color(0xFF2ECC71),
    warningOrange   = Color(0xFFFF9800),
    errorRed        = Color(0xFFE53935),
    button          = Brush.linearGradient(colors = listOf(Color(0xFF9B3DFF), Color(0xFF5A0EA8), Color(0xFF3A4FD0))),
    buttonDanger    = Brush.linearGradient(colors = listOf(Color(0xFFE53935), Color(0xFF8B0000))),
    buttonSuccess   = Brush.linearGradient(colors = listOf(Color(0xFF2ECC71), Color(0xFF1A7A44))),
    navSelected     = Brush.verticalGradient(colors = listOf(Color(0xFFB490FF), Color(0xFF5A0EA8))),
    modeBlock       = Brush.linearGradient(colors = listOf(Color(0xFF6B0FCC), Color(0xFF3D0066)), start = Offset(0f, 0f), end = Offset(300f, 300f)),
    modeLimit       = Brush.linearGradient(colors = listOf(Color(0xFF2C3EBF), Color(0xFF1A0E5E)), start = Offset(0f, 0f), end = Offset(300f, 300f)),
    modeSmart       = Brush.linearGradient(colors = listOf(Color(0xFF0D7377), Color(0xFF14213D)), start = Offset(0f, 0f), end = Offset(300f, 300f)),
    borderSubtle    = Color(0x22FFFFFF),
    borderPurple    = Color(0x66B77CFF),
    borderActive    = Color(0xFFB77CFF),
    glowPurple      = Color(0x449B3DFF),
    glowBlue        = Color(0x443A5BF0),
    glowTeal        = Color(0x440D7377),
    glowRed         = Color(0x44E53935)
)

fun lightAppColors() = AppColors(
    isDark          = false,
    textPrimary     = Color(0xFF0E0520),   // near-black with purple tint
    textSecondary   = Color(0xFF3A2852),
    textMuted       = Color(0xFF5A5070),   // WCAG AA on white ✅
    background      = Brush.linearGradient(
        colors = listOf(Color(0xFFF8F7FF), Color(0xFFF0EBFF), Color(0xFFEDE5FF), Color(0xFFF0EBFF), Color(0xFFF8F7FF)),
        start  = Offset(0f, 0f), end = Offset(1080f, 2400f)
    ),
    cardSurface     = Brush.linearGradient(
        colors = listOf(Color(0xFFFFFFFF), Color(0xFFF4F0FE)),
        start  = Offset(0f, 0f), end = Offset(400f, 400f)
    ),
    glassSurface    = Brush.linearGradient(colors = listOf(Color(0x44FFFFFF), Color(0x22FFFFFF))),
    purplePrimary   = Color(0xFF7C22E8),   // darkened for WCAG on white
    purpleDeep      = Color(0xFF5A0EA8),
    purpleSoft      = Color(0xFF9B6FDF),
    blueAccent      = Color(0xFF2A4BD0),
    successGreen    = Color(0xFF1A8048),
    warningOrange   = Color(0xFFD07000),
    errorRed        = Color(0xFFCC1A1A),
    button          = Brush.linearGradient(colors = listOf(Color(0xFF9B3DFF), Color(0xFF5A0EA8), Color(0xFF3A4FD0))), // same as dark ✅
    buttonDanger    = Brush.linearGradient(colors = listOf(Color(0xFFCC1A1A), Color(0xFF800000))),
    buttonSuccess   = Brush.linearGradient(colors = listOf(Color(0xFF1A8048), Color(0xFF0E5030))),
    navSelected     = Brush.verticalGradient(colors = listOf(Color(0xFF9B3DFF), Color(0xFF5A0EA8))), // same ✅
    modeBlock       = Brush.linearGradient(colors = listOf(Color(0xFF7B1FDC), Color(0xFF4A0088)), start = Offset(0f, 0f), end = Offset(300f, 300f)),
    modeLimit       = Brush.linearGradient(colors = listOf(Color(0xFF3848CF), Color(0xFF2214A0)), start = Offset(0f, 0f), end = Offset(300f, 300f)),
    modeSmart       = Brush.linearGradient(colors = listOf(Color(0xFF0F8B90), Color(0xFF183260)), start = Offset(0f, 0f), end = Offset(300f, 300f)),
    borderSubtle    = Color(0x1A000000),   // black 10%
    borderPurple    = Color(0x887C22E8),
    borderActive    = Color(0xFF7C22E8),
    glowPurple      = Color(0x2A7C22E8),   // softer but alive
    glowBlue        = Color(0x2A2A4BD0),
    glowTeal        = Color(0x2A0F8B90),
    glowRed         = Color(0x2ACC1A1A)
)