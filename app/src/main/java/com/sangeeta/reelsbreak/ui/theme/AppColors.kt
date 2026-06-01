package com.sangeeta.reelsbreak.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class AppColors(
    val isDark: Boolean,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val background: Brush,
    val cardSurface: Brush,
    val glassSurface: Brush,
    val purplePrimary: Color,
    val purpleDeep: Color,
    val purpleSoft: Color,
    val blueAccent: Color,
    val successGreen: Color,
    val warningOrange: Color,
    val errorRed: Color,
    val button: Brush,
    val buttonDanger: Brush,
    val buttonSuccess: Brush,
    val navSelected: Brush,
    val modeBlock: Brush,
    val modeLimit: Brush,
    val modeSmart: Brush,
    val borderSubtle: Color,
    val borderPurple: Color,
    val borderActive: Color,
    val glowPurple: Color,
    val glowBlue: Color,
    val glowTeal: Color,
    val glowRed: Color,
    val appColor: Brush,
    val borderColor: Brush,
    val screenBlockedBg: Brush,
    val timerTrack: Color,
    val timerArc: Color,
    val switchTrackOn: Color,
    val switchTrackOff: Color,
    val sheetBg: Color,
    val sheetDragHandle: Color,
    val pausedAccent: Color,
    val pausedAccentSoft: Color,
)


fun darkAppColors() = AppColors(
    isDark        = true,
    textPrimary   = Color(0xFFEEECF5),
    textSecondary = Color(0xFF9B97B8),
    textMuted     = Color(0xFF5C5875),

    background    = Brush.linearGradient(
        colors = listOf(
            Color(0xFF0D0618), Color(0xFF130A22),
            Color(0xFF180D2A), Color(0xFF130A22), Color(0xFF0D0618)
        ),
        start = Offset(0f, 0f), end = Offset(1080f, 2400f)
    ),
    cardSurface   = Brush.linearGradient(
        colors = listOf(Color(0xFF1C1230), Color(0xFF160E28)),
        start  = Offset(0f, 0f), end = Offset(400f, 400f)
    ),
    glassSurface  = Brush.linearGradient(
        colors = listOf(Color(0x28A78BFA), Color(0x0CA78BFA))
    ),

    purplePrimary = Color(0xFF8B5CF6),
    purpleDeep    = Color(0xFF6D28D9),
    purpleSoft    = Color(0xFFA78BFA),
    blueAccent    = Color(0xFF60A5FA),
    successGreen  = Color(0xFF34D399),
    warningOrange = Color(0xFFFBBF24),
    pausedAccent = Color(0xFF9AA3B2),
    pausedAccentSoft = Color(0xFF596173),
    errorRed      = Color(0xFFF87171),

    button        = Brush.linearGradient(listOf(Color(0xFF7C3AED), Color(0xFF4C1D95))),
    buttonDanger  = Brush.linearGradient(listOf(Color(0xFFF87171), Color(0xFFB91C1C))),
    buttonSuccess = Brush.linearGradient(listOf(Color(0xFF34D399), Color(0xFF065F46))),
    navSelected   = Brush.verticalGradient(listOf(Color(0xFF8B5CF6), Color(0xFF4C1D95))),

    modeBlock     = Brush.linearGradient(listOf(Color(0xFF2E1065), Color(0xFF1A0840)), start = Offset(0f, 0f), end = Offset(300f, 300f)),
    modeLimit     = Brush.linearGradient(listOf(Color(0xFF1E3A8A), Color(0xFF0F1A3D)), start = Offset(0f, 0f), end = Offset(300f, 300f)),
    modeSmart     = Brush.linearGradient(listOf(Color(0xFF064E3B), Color(0xFF0A1F18)), start = Offset(0f, 0f), end = Offset(300f, 300f)),

    borderSubtle  = Color(0x22A78BFA),
    borderPurple  = Color(0x668B5CF6),
    borderActive  = Color(0xFFA78BFA),
    glowPurple    = Color(0x408B5CF6),
    glowBlue      = Color(0x4060A5FA),
    glowTeal      = Color(0x4034D399),
    glowRed       = Color(0x40F87171),

    appColor       = Brush.verticalGradient(listOf(Color(0xFF7C3AED), Color(0xFF4C1D95))),
    borderColor = Brush.verticalGradient(listOf(Color(0xFFA78BFA), Color(0xFF6D28D9))),
    screenBlockedBg = Brush.verticalGradient(listOf(Color(0xFF0D0818), Color(0xFF130D26), Color(0xFF0A0515))),

    timerTrack    = Color(0xFF2D1B4E),
    timerArc      = Color(0xFF9333EA),
    switchTrackOn  = Color(0xFF7C3AED),
    switchTrackOff = Color(0xFF2A1F40),
    sheetBg         = Color.Transparent,
    sheetDragHandle = Color(0x40FFFFFF),
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
    glassSurface    = Brush.linearGradient(listOf(Color(0x44FFFFFF), Color(0x22FFFFFF))),

    purplePrimary   = Color(0xFF7C22E8),
    purpleDeep      = Color(0xFF5A0EA8),
    purpleSoft      = Color(0xFF9B6FDF),
    blueAccent      = Color(0xFF2A4BD0),
    successGreen    = Color(0xFF1A8048),
    warningOrange   = Color(0xFFD07000),
    pausedAccent = Color(0xFF6B7280),
    pausedAccentSoft = Color(0xFFD7DCE5),
    errorRed        = Color(0xFFCC1A1A),

    button          = Brush.linearGradient(listOf(Color(0xFF9B3DFF), Color(0xFF5A0EA8), Color(0xFF3A4FD0))),
    buttonDanger    = Brush.linearGradient(listOf(Color(0xFFCC1A1A), Color(0xFF800000))),
    buttonSuccess   = Brush.linearGradient(listOf(Color(0xFF1A8048), Color(0xFF0E5030))),
    navSelected     = Brush.verticalGradient(listOf(Color(0xFF9B3DFF), Color(0xFF5A0EA8))),
    modeBlock       = Brush.linearGradient(listOf(Color(0xFF7B1FDC), Color(0xFF4A0088)), start = Offset(0f, 0f), end = Offset(300f, 300f)),
    modeLimit       = Brush.linearGradient(listOf(Color(0xFF3848CF), Color(0xFF2214A0)), start = Offset(0f, 0f), end = Offset(300f, 300f)),
    modeSmart       = Brush.linearGradient(listOf(Color(0xFF0F8B90), Color(0xFF183260)), start = Offset(0f, 0f), end = Offset(300f, 300f)),

    borderSubtle    = Color(0x1A000000),
    borderPurple    = Color(0x887C22E8),
    borderActive    = Color(0xFF7C22E8),
    glowPurple      = Color(0x2A7C22E8),
    glowBlue        = Color(0x2A2A4BD0),
    glowTeal        = Color(0x2A0F8B90),
    glowRed         = Color(0x2ACC1A1A),

    appColor       = Brush.linearGradient(listOf(Color(0xFF6B3FA0), Color(0xFF4A2070))),
    borderColor = Brush.verticalGradient(listOf(Color(0xFFB39DDB), Color(0xFF9C78D4))),
    screenBlockedBg = Brush.verticalGradient(listOf(Color(0xFF0D0818), Color(0xFF130D26), Color(0xFF0A0515))),

    timerTrack      = Color(0xFFEDE8FF),
    timerArc        = Color(0xFF6B3FA0),
    switchTrackOn   = Color(0xFF6B3FA0),
    switchTrackOff  = Color(0xFFEDE8FF),
    sheetBg         = Color.Transparent,
    sheetDragHandle = Color(0x40FFFFFF),
)