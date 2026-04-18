//package com.practice.reelbreak.ui.component
//
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//
////object GradientColor {
////    val background = Brush.Companion.linearGradient(
////        colors = listOf(
////            Color(0xFF000000),
////            Color(0xFF29083D),
////            Color(0xFF000000),
////        ),
////        start = Offset(0f, 0f),
////        end = Offset(1400f, 2000f)
////    )
////
////    val button = Brush.Companion.linearGradient(
////        colors = listOf(
////            Color(0xFF8B2CE8),
////            Color(0xFF581176),
////            Color(0xFF263CB3)
////        )
////    )
////}
//
//
//
//
//
//
//object GradientColor {
//
//    // ─── Core Brand Colors ───────────────────────────────────────────────────
//    val PurplePrimary    = Color(0xFF9B3DFF)
//    val PurpleDeep       = Color(0xFF5A0EA8)
//    val PurpleSoft       = Color(0xFFB77CFF)
//    val PurpleGlow       = Color(0xFF7B2FE0)
//    val BlueAccent       = Color(0xFF3A5BF0)
//    val BlackDark        = Color(0xFF000000)
//    val BlackSurface     = Color(0xFF0D0D14)
//    val BlackCard        = Color(0xFF12101A)
//    val BlackCardLight   = Color(0xFF1A1626)
//    val TextPrimary      = Color(0xFFFFFFFF)
//    val TextSecondary    = Color(0xFFB0B0CC)
//    val TextMuted        = Color(0xFF6B6880)
//    val SuccessGreen     = Color(0xFF2ECC71)
//    val WarningOrange    = Color(0xFFFF9800)
//    val ErrorRed         = Color(0xFFE53935)
//
//    // ─── Backgrounds ─────────────────────────────────────────────────────────
//    /** Main screen background — diagonal black-to-purple-to-black */
//    val background = Brush.linearGradient(
//        colors = listOf(
//            Color(0xFF000000),
//            Color(0xFF1A0530),
//            Color(0xFF29083D),
//            Color(0xFF1A0530),
//            Color(0xFF000000),
//        ),
//        start = Offset(0f, 0f),
//        end = Offset(1080f, 2400f)
//    )
//
//    /** Subtle card surface gradient */
//    val cardSurface = Brush.linearGradient(
//        colors = listOf(
//            Color(0xFF1A1228),
//            Color(0xFF120D1E),
//        ),
//        start = Offset(0f, 0f),
//        end = Offset(400f, 400f)
//    )
//
//    /** Glass card effect (use with alpha container) */
//    val glassSurface = Brush.linearGradient(
//        colors = listOf(
//            Color(0x22FFFFFF),
//            Color(0x0AFFFFFF),
//        )
//    )
//
//    // ─── Buttons ─────────────────────────────────────────────────────────────
//    /** Primary CTA button */
//    val button = Brush.linearGradient(
//        colors = listOf(
//            Color(0xFF9B3DFF),
//            Color(0xFF5A0EA8),
//            Color(0xFF3A4FD0)
//        )
//    )
//
//    /** Danger/Block button */
//    val buttonDanger = Brush.linearGradient(
//        colors = listOf(
//            Color(0xFFE53935),
//            Color(0xFF8B0000)
//        )
//    )
//
//    /** Success/Active button */
//    val buttonSuccess = Brush.linearGradient(
//        colors = listOf(
//            Color(0xFF2ECC71),
//            Color(0xFF1A7A44)
//        )
//    )
//
//    // ─── Nav Bar ─────────────────────────────────────────────────────────────
//    /** Floating nav selected item */
//    val navSelected = Brush.verticalGradient(
//        colors = listOf(
//            Color(0xFFB490FF),
//            Color(0xFF5A0EA8)
//        )
//    )
//
//    // ─── Block Mode Cards ────────────────────────────────────────────────────
//    /** Mode 1: Block Immediately — red-purple */
//    val modeBlock = Brush.linearGradient(
//        colors = listOf(
//            Color(0xFF6B0FCC),
//            Color(0xFF3D0066)
//        ),
//        start = Offset(0f, 0f),
//        end = Offset(300f, 300f)
//    )
//
//    /** Mode 2: Limit Based — purple-blue */
//    val modeLimit = Brush.linearGradient(
//        colors = listOf(
//            Color(0xFF2C3EBF),
//            Color(0xFF1A0E5E)
//        ),
//        start = Offset(0f, 0f),
//        end = Offset(300f, 300f)
//    )
//
//    /** Mode 3: Smart Filter — teal-purple */
//    val modeSmart = Brush.linearGradient(
//        colors = listOf(
//            Color(0xFF0D7377),
//            Color(0xFF14213D)
//        ),
//        start = Offset(0f, 0f),
//        end = Offset(300f, 300f)
//    )
//
//    // ─── Glows (for Box shadows via BlurMaskFilter or shadow modifier) ────────
//    val glowPurple  = Color(0x449B3DFF)
//    val glowBlue    = Color(0x443A5BF0)
//    val glowTeal    = Color(0x440D7377)
//    val glowRed     = Color(0x44E53935)
//
//    // ─── Border Strokes ───────────────────────────────────────────────────────
//    val borderSubtle   = Color(0x22FFFFFF)
//    val borderPurple   = Color(0x66B77CFF)
//    val borderActive   = Color(0xFFB77CFF)
//}