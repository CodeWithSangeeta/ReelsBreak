package com.practice.reelbreak.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class AppColors(
    val isDark: Boolean,
    // ── Text
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    // ── Surfaces
    val background: Brush,
    val cardSurface: Brush,
    val glassSurface: Brush,
    // ── Brand
    val purplePrimary: Color,
    val purpleDeep: Color,
    val purpleSoft: Color,
    val blueAccent: Color,
    val successGreen: Color,
    val warningOrange: Color,
    val errorRed: Color,
    // ── Buttons
    val button: Brush,
    val buttonDanger: Brush,
    val buttonSuccess: Brush,
    // ── Nav
    val navSelected: Brush,
    // ── Mode cards
    val modeBlock: Brush,
    val modeLimit: Brush,
    val modeSmart: Brush,
    // ── Borders & glows
    val borderSubtle: Color,
    val borderPurple: Color,
    val borderActive: Color,
    val glowPurple: Color,
    val glowBlue: Color,
    val glowTeal: Color,
    val glowRed: Color
)

//fun darkAppColors() = AppColors(
//    isDark          = true,
//    textPrimary     = Color(0xFFFFFFFF),
//    textSecondary   = Color(0xFFB0B0CC),
//    textMuted       = Color(0xFF6B6880),
//    background      = Brush.linearGradient(
//        colors = listOf(Color(0xFF000000), Color(0xFF1A0530), Color(0xFF29083D), Color(0xFF1A0530), Color(0xFF000000)),
//        start  = Offset(0f, 0f), end = Offset(1080f, 2400f)
//    ),
//    cardSurface     = Brush.linearGradient(
//        colors = listOf(Color(0xFF1A1228), Color(0xFF120D1E)),
//        start  = Offset(0f, 0f), end = Offset(400f, 400f)
//    ),
//    glassSurface    = Brush.linearGradient(colors = listOf(Color(0x22FFFFFF), Color(0x0AFFFFFF))),
//    purplePrimary   = Color(0xFF9B3DFF),
//    purpleDeep      = Color(0xFF5A0EA8),
//    purpleSoft      = Color(0xFFB77CFF),
//    blueAccent      = Color(0xFF3A5BF0),
//    successGreen    = Color(0xFF2ECC71),
//    warningOrange   = Color(0xFFFF9800),
//    errorRed        = Color(0xFFE53935),
//    button          = Brush.linearGradient(colors = listOf(Color(0xFF9B3DFF), Color(0xFF5A0EA8), Color(0xFF3A4FD0))),
//    buttonDanger    = Brush.linearGradient(colors = listOf(Color(0xFFE53935), Color(0xFF8B0000))),
//    buttonSuccess   = Brush.linearGradient(colors = listOf(Color(0xFF2ECC71), Color(0xFF1A7A44))),
//    navSelected     = Brush.verticalGradient(colors = listOf(Color(0xFFB490FF), Color(0xFF5A0EA8))),
//    modeBlock       = Brush.linearGradient(colors = listOf(Color(0xFF6B0FCC), Color(0xFF3D0066)), start = Offset(0f, 0f), end = Offset(300f, 300f)),
//    modeLimit       = Brush.linearGradient(colors = listOf(Color(0xFF2C3EBF), Color(0xFF1A0E5E)), start = Offset(0f, 0f), end = Offset(300f, 300f)),
//    modeSmart       = Brush.linearGradient(colors = listOf(Color(0xFF0D7377), Color(0xFF14213D)), start = Offset(0f, 0f), end = Offset(300f, 300f)),
//    borderSubtle    = Color(0x22FFFFFF),
//    borderPurple    = Color(0x66B77CFF),
//    borderActive    = Color(0xFFB77CFF),
//    glowPurple      = Color(0x449B3DFF),
//    glowBlue        = Color(0x443A5BF0),
//    glowTeal        = Color(0x440D7377),
//    glowRed         = Color(0x44E53935)
//)


fun darkAppColors() = AppColors(
    isDark        = true,
    textPrimary   = Color(0xFFEEECF5),
    textSecondary = Color(0xFF9B97B8),
    textMuted     = Color(0xFF5C5875),

    // ── Deep purple-black, NOT pure black ──
    background    = Brush.linearGradient(
        colors = listOf(
            Color(0xFF0D0618),   // very deep purple-black
            Color(0xFF130A22),   // hint of violet
            Color(0xFF180D2A),   // slightly warmer
            Color(0xFF130A22),
            Color(0xFF0D0618)
        ),
        start = Offset(0f, 0f),
        end   = Offset(1080f, 2400f)
    ),

    // ── Cards pop clearly off background ──
    cardSurface   = Brush.linearGradient(
        colors = listOf(Color(0xFF1C1230), Color(0xFF160E28)),
        start  = Offset(0f, 0f),
        end    = Offset(400f, 400f)
    ),

    glassSurface  = Brush.linearGradient(
        colors = listOf(Color(0x28A78BFA), Color(0x0CA78BFA))
    ),

    // ── Modern violet palette (not old garish purple) ──
    purplePrimary = Color(0xFF8B5CF6),   // violet-500
    purpleDeep    = Color(0xFF6D28D9),   // violet-700
    purpleSoft    = Color(0xFFA78BFA),   // violet-400

    blueAccent    = Color(0xFF60A5FA),   // blue-400 — fresh pairing with violet
    successGreen  = Color(0xFF34D399),   // emerald-400
    warningOrange = Color(0xFFFBBF24),   // amber-400
    errorRed      = Color(0xFFF87171),   // red-400

    // ── Buttons ──
    button        = Brush.linearGradient(
        colors = listOf(Color(0xFF7C3AED), Color(0xFF4C1D95))
    ),
    buttonDanger  = Brush.linearGradient(
        colors = listOf(Color(0xFFF87171), Color(0xFFB91C1C))
    ),
    buttonSuccess = Brush.linearGradient(
        colors = listOf(Color(0xFF34D399), Color(0xFF065F46))
    ),

    navSelected   = Brush.verticalGradient(
        colors = listOf(Color(0xFF8B5CF6), Color(0xFF4C1D95))
    ),

    // ── Mode cards — tinted, clearly visible ──
    modeBlock     = Brush.linearGradient(
        colors = listOf(Color(0xFF2E1065), Color(0xFF1A0840)),
        start  = Offset(0f, 0f), end = Offset(300f, 300f)
    ),
    modeLimit     = Brush.linearGradient(
        colors = listOf(Color(0xFF1E3A8A), Color(0xFF0F1A3D)),
        start  = Offset(0f, 0f), end = Offset(300f, 300f)
    ),
    modeSmart     = Brush.linearGradient(
        colors = listOf(Color(0xFF064E3B), Color(0xFF0A1F18)),
        start  = Offset(0f, 0f), end = Offset(300f, 300f)
    ),

    // ── Borders with visible purple tint ──
    borderSubtle  = Color(0x22A78BFA),   // soft lavender
    borderPurple  = Color(0x668B5CF6),
    borderActive  = Color(0xFFA78BFA),
    glowPurple    = Color(0x408B5CF6),
    glowBlue      = Color(0x4060A5FA),
    glowTeal      = Color(0x4034D399),
    glowRed       = Color(0x40F87171)
)

fun lightAppColors() = AppColors(
    isDark          = false,
    textPrimary     = Color(0xFF0E0520),
    textSecondary   = Color(0xFF3A2852),
    textMuted       = Color(0xFF5A5070),
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
    button          = Brush.linearGradient(colors = listOf(Color(0xFF9B3DFF), Color(0xFF5A0EA8), Color(0xFF3A4FD0))),
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