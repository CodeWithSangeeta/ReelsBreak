//package com.practice.reelbreak.ui.focused_mode
//
//
//import androidx.compose.animation.core.FastOutSlowInEasing
//import androidx.compose.animation.core.RepeatMode
//import androidx.compose.animation.core.animateFloat
//import androidx.compose.animation.core.infiniteRepeatable
//import androidx.compose.animation.core.rememberInfiniteTransition
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Lock
//import androidx.compose.material.icons.outlined.Schedule
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.scale
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.practice.reelbreak.core.registry.FocusModeSupportedAppsCatalog
//import com.practice.reelbreak.ui.theme.LocalAppColors
//import kotlinx.coroutines.delay
//
//
//
//
////@Composable
////fun AppBlockedScreen(
////    blockedPackage: String,
////    remainingFormatted: String,
////    focusEndTs: Long,
////    onGoBack: () -> Unit
////) {
////    val colors = LocalAppColors.current
////    val appName = FocusModeSupportedAppsCatalog.displayNameOf(blockedPackage)
////
////    // Live countdown inside this screen
////    var liveRemaining by remember { mutableStateOf(remainingFormatted) }
////    LaunchedEffect(focusEndTs) {
////        while (true) {
////            val totalSeconds = ((focusEndTs - System.currentTimeMillis()) / 1000L)
////                .coerceAtLeast(0L)
////            val hours   = totalSeconds / 3600
////            val minutes = (totalSeconds % 3600) / 60
////            val seconds = totalSeconds % 60
////            liveRemaining = if (hours > 0)
////                String.format("%02dh %02dm remaining", hours, minutes)
////            else
////                String.format("%02dm %02ds remaining", minutes, seconds)
////            delay(1_000L)
////        }
////    }
////
////    // Pulsing animation for the lock icon
////    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
////    val pulseScale by infiniteTransition.animateFloat(
////        initialValue = 1f,
////        targetValue = 1.12f,
////        animationSpec = infiniteRepeatable(
////            animation = tween(900, easing = FastOutSlowInEasing),
////            repeatMode = RepeatMode.Reverse
////        ),
////        label = "pulseScale"
////    )
////
////    Box(
////        modifier = Modifier
////            .fillMaxSize()
////            .background(
////                brush = colors.background
//////                Brush.verticalGradient(
//////                    listOf(
//////                        Color(0xFF0D0818),
//////                        Color(0xFF130D26),
//////                        Color(0xFF0A0515)
//////                    )
//////                )
////            ),
////        contentAlignment = Alignment.Center
////    ) {
////
////        Column(
////            horizontalAlignment = Alignment.CenterHorizontally,
////            verticalArrangement = Arrangement.spacedBy(0.dp),
////            modifier = Modifier
////                .fillMaxWidth()
////                .padding(horizontal = 32.dp)
////        ) {
////
////            // ── Pulsing lock icon ──────────────────────────────────────
////            Box(
////                modifier = Modifier
////                    .scale(pulseScale)
////                    .size(100.dp)
////                    .clip(CircleShape)
////                    .background(
////                        Brush.radialGradient(
////                            listOf(
////                                colors.purplePrimary.copy(alpha = 0.35f),
////                                colors.purplePrimary.copy(alpha = 0.12f),
//////                                Color(0xFF7C3AED).copy(alpha = 0.4f),
//////                                Color(0xFF4C1D95).copy(alpha = 0.2f),
////                                Color.Transparent
////                            )
////                        )
////                    )
////                    .border(
////                        2.dp,
////                        Brush.sweepGradient(
////                            listOf(
////                                colors.purplePrimary,
////                                colors.purpleSoft,
////                                colors.purplePrimary
//////                                Color(0xFF9333EA),
//////                                Color(0xFFA855F7),
//////                                Color(0xFF7C3AED),
//////                                Color(0xFF9333EA)
////                            )
////                        ),
////                        CircleShape
////                    ),
////                contentAlignment = Alignment.Center
////            ) {
////                Icon(
////                    imageVector = Icons.Filled.Lock,
////                    contentDescription = "Blocked",
////                    tint = colors.purpleSoft,
////                    modifier = Modifier.size(44.dp)
////                )
////            }
////
////            Spacer(modifier = Modifier.height(28.dp))
////
////            Text(
////                text = appName,
////                color = colors.textPrimary,
////                fontSize = 30.sp,
////                fontWeight = FontWeight.Bold,
////                textAlign = TextAlign.Center
////            )
////
////            Spacer(modifier = Modifier.height(8.dp))
////
////            Text(
////                text = "is blocked during Focus Mode",
////                color = Color.White.copy(alpha = 0.55f),
////                fontSize = 15.sp,
////                textAlign = TextAlign.Center
////            )
////
////            Spacer(modifier = Modifier.height(28.dp))
////
////            Box(
////                modifier = Modifier
////                    .clip(RoundedCornerShape(999.dp))
////                    .background(
////                       colors.cardSurface
////                    )
////                    .border(
////                        1.dp,
////                        colors.purplePrimary.copy(alpha = 0.55f),
////                        RoundedCornerShape(999.dp)
////                    )
////                    .padding(horizontal = 22.dp, vertical = 12.dp)
////            ) {
////                Row(
////                    verticalAlignment = Alignment.CenterVertically,
////                    horizontalArrangement = Arrangement.spacedBy(8.dp)
////                ) {
////                    Text(text = "⏱", fontSize = 16.sp)
////                    Text(
////                        text = liveRemaining,
////                        color = colors.purpleSoft,
////                        fontSize = 16.sp,
////                        fontWeight = FontWeight.SemiBold
////                    )
////                }
////            }
////
////            Spacer(modifier = Modifier.height(32.dp))
////
////            // ── Motivational text ──────────────────────────────────────
////            Box(
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .clip(RoundedCornerShape(16.dp))
////                    .background(colors.cardSurface)
////                    .border(
////                        1.dp,
////                        colors.borderColor,
////                        RoundedCornerShape(16.dp)
////                    )
////                    .padding(horizontal = 20.dp, vertical = 16.dp)
////            ) {
////                Column(
////                    horizontalAlignment = Alignment.CenterHorizontally,
////                    verticalArrangement = Arrangement.spacedBy(6.dp)
////                ) {
////                    Text(text = "🧠", fontSize = 24.sp)
////                    Text(
////                        text = "Stay in the zone.",
////                        color = colors.purpleSoft,
////                        fontSize = 14.sp,
////                        fontWeight = FontWeight.SemiBold,
////                        textAlign = TextAlign.Center
////                    )
////                    Text(
////                        text = "You chose to focus. Every distraction you avoid brings you closer to your goal.",
////                        color = colors.textSecondary,
////                        fontSize = 13.sp,
////                        textAlign = TextAlign.Center,
////                        lineHeight = 19.sp
////                    )
////                }
////            }
////
////            Spacer(modifier = Modifier.height(32.dp))
////
////            // ── Go back to home button ─────────────────────────────────
////            Box(
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .clip(RoundedCornerShape(16.dp))
////                    .background(
////                       colors.appColor
////                    )
////                    .clickable { onGoBack() }
////                    .padding(vertical = 16.dp),
////                contentAlignment = Alignment.Center
////            ) {
////                Text(
////                    text = "← Go Back to Home",
////                    color = Color.White,
////                    fontSize = 15.sp,
////                    fontWeight = FontWeight.SemiBold
////                )
////            }
////
////            Spacer(modifier = Modifier.height(12.dp))
////
////            // ── Secondary label ────────────────────────────────────────
////            Text(
////                text = "Focus session is protecting your time",
////                color = Color.White.copy(alpha = 0.3f),
////                fontSize = 12.sp,
////                textAlign = TextAlign.Center
////            )
////        }
////    }
////}
//
//
//
//@Composable
//fun AppBlockedScreen(
//    blockedPackage: String,
//    remainingFormatted: String,
//    focusEndTs: Long,
//    onCloseApp: () -> Unit,
//    onOpenFocusMode: () -> Unit
//) {
//    val colors = LocalAppColors.current
//    val appInfo = FocusModeSupportedAppsCatalog.findByPackage(blockedPackage)
//    val appName = appInfo?.displayName ?: FocusModeSupportedAppsCatalog.displayNameOf(blockedPackage)
//
//    var liveRemaining by remember { mutableStateOf(remainingFormatted) }
//    LaunchedEffect(focusEndTs) {
//        while (true) {
//            val totalSeconds = ((focusEndTs - System.currentTimeMillis()) / 1000L).coerceAtLeast(0L)
//            val hours = totalSeconds / 3600
//            val minutes = (totalSeconds % 3600) / 60
//            val seconds = totalSeconds % 60
//
//            liveRemaining = if (hours > 0) {
//                String.format("%02dh %02dm left", hours, minutes)
//            } else {
//                String.format("%02dm %02ds left", minutes, seconds)
//            }
//
//            delay(1000L)
//        }
//    }
//
//    val infiniteTransition = rememberInfiniteTransition(label = "blocked_pulse")
//    val pulseScale by infiniteTransition.animateFloat(
//        initialValue = 1f,
//        targetValue = 1.06f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(900, easing = FastOutSlowInEasing),
//            repeatMode = RepeatMode.Reverse
//        ),
//        label = "pulse_scale"
//    )
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(colors.background),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 24.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clip(RoundedCornerShape(28.dp))
//                    .background(colors.cardSurface)
//                    .border(1.dp, colors.borderColor, RoundedCornerShape(28.dp))
//                    .padding(horizontal = 24.dp, vertical = 28.dp)
//            ) {
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.spacedBy(18.dp),
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Box(
//                        modifier = Modifier.size(92.dp),
//                        contentAlignment = Alignment.TopEnd
//                    ) {
//                        Box(
//                            modifier = Modifier
//                                .scale(pulseScale)
//                                .size(76.dp)
//                                .align(Alignment.Center)
//                                .clip(RoundedCornerShape(22.dp))
//                                .background(
//                                    colors.purplePrimary.copy(
//                                        alpha = if (colors.isDark) 0.16f else 0.10f
//                                    )
//                                )
//                                .border(
//                                    1.5.dp,
//                                    colors.purplePrimary.copy(alpha = 0.35f),
//                                    RoundedCornerShape(22.dp)
//                                ),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            if (appInfo != null) {
//                                Image(
//                                    painter = painterResource(appInfo.iconRes),
//                                    contentDescription = appName,
//                                    modifier = Modifier.size(42.dp)
//                                )
//                            } else {
//                                Icon(
//                                    imageVector = Icons.Filled.Lock,
//                                    contentDescription = null,
//                                    tint = colors.purpleSoft,
//                                    modifier = Modifier.size(34.dp)
//                                )
//                            }
//                        }
//
//                        Box(
//                            modifier = Modifier
//                                .size(26.dp)
//                                .clip(CircleShape)
//                                .background(colors.errorRed)
//                                .border(2.dp, colors.cardSurface, CircleShape),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Icon(
//                                imageVector = Icons.Filled.Lock,
//                                contentDescription = null,
//                                tint = Color.White,
//                                modifier = Modifier.size(14.dp)
//                            )
//                        }
//                    }
//
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.spacedBy(6.dp)
//                    ) {
//                        Text(
//                            text = "$appName is blocked",
//                            color = colors.textPrimary,
//                            fontSize = 24.sp,
//                            fontWeight = FontWeight.Bold,
//                            textAlign = TextAlign.Center
//                        )
//
//                        Text(
//                            text = "ReelBreak closed this app because your focus session is active.",
//                            color = colors.textSecondary,
//                            fontSize = 14.sp,
//                            lineHeight = 20.sp,
//                            textAlign = TextAlign.Center
//                        )
//                    }
//
//                    Box(
//                        modifier = Modifier
//                            .clip(RoundedCornerShape(999.dp))
//                            .background(colors.cardSurface)
//                            .border(
//                                1.dp,
//                                colors.purplePrimary.copy(alpha = 0.28f),
//                                RoundedCornerShape(999.dp)
//                            )
//                            .padding(horizontal = 16.dp, vertical = 10.dp)
//                    ) {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.spacedBy(8.dp)
//                        ) {
//                            Icon(
//                                imageVector = Icons.Outlined.Schedule,
//                                contentDescription = null,
//                                tint = colors.purpleSoft,
//                                modifier = Modifier.size(16.dp)
//                            )
//                            Text(
//                                text = "Session ends in $liveRemaining",
//                                color = colors.purpleSoft,
//                                fontSize = 14.sp,
//                                fontWeight = FontWeight.SemiBold
//                            )
//                        }
//                    }
//
//                    Text(
//                        text = "You can return when your session ends, or open Focus Mode to change blocked apps or duration.",
//                        color = colors.textMuted,
//                        fontSize = 12.sp,
//                        lineHeight = 18.sp,
//                        textAlign = TextAlign.Center
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(18.dp))
//
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(52.dp)
//                    .clip(RoundedCornerShape(16.dp))
//                    .background(colors.appColor)
//                    .clickable { onOpenFocusMode() },
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = "Open Focus Mode",
//                    color = Color.White,
//                    fontSize = 15.sp,
//                    fontWeight = FontWeight.SemiBold
//                )
//            }
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(52.dp)
//                    .clip(RoundedCornerShape(16.dp))
//                    .background(colors.cardSurface)
//                    .border(1.dp, colors.borderColor, RoundedCornerShape(16.dp))
//                    .clickable { onCloseApp() },
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = "Close app",
//                    color = colors.textPrimary,
//                    fontSize = 15.sp,
//                    fontWeight = FontWeight.Medium
//                )
//            }
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            Text(
//                text = "Your focus session is still running.",
//                color = colors.textMuted,
//                fontSize = 12.sp,
//                textAlign = TextAlign.Center
//            )
//        }
//    }
//}


package com.sangeeta.reelsbreak.ui.focused_mode

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.reelsbreak.R
import com.sangeeta.reelsbreak.core.registry.FocusModeSupportedAppsCatalog
import com.sangeeta.reelsbreak.ui.theme.LocalAppColors
import kotlinx.coroutines.delay

@Composable
fun AppBlockedScreen(
    blockedPackage: String,
    remainingFormatted: String,
    focusEndTs: Long,
    onCloseApp: () -> Unit,
    onOpenFocusMode: () -> Unit
) {
    val colors = LocalAppColors.current
    val appInfo = FocusModeSupportedAppsCatalog.findByPackage(blockedPackage)
    val appName = appInfo?.displayName ?: FocusModeSupportedAppsCatalog.displayNameOf(blockedPackage)

    var liveRemaining by remember { mutableStateOf(remainingFormatted) }

    LaunchedEffect(focusEndTs) {
        while (true) {
            val totalSeconds = ((focusEndTs - System.currentTimeMillis()) / 1000L)
                .coerceAtLeast(0L)

            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60
            val seconds = totalSeconds % 60

            liveRemaining = if (hours > 0) {
                String.format("%02dh %02dm left", hours, minutes)
            } else {
                String.format("%02dm %02ds left", minutes, seconds)
            }

            delay(1000L)
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "blocked_screen_animation")

    val appIconScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "app_icon_scale"
    )

    val brandLogoScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "brand_logo_scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = colors.background)
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(colors.cardSurface)
                    .border(
                        1.dp,
                        colors.borderColor,
                        RoundedCornerShape(28.dp)
                    )
                    .padding(horizontal = 22.dp, vertical = 26.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {

                    // Main blocked app hero
                    Box(
                        modifier = Modifier.size(96.dp),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .scale(appIconScale)
                                .size(76.dp)
                                .clip(RoundedCornerShape(22.dp))
                                .background(
                                    if (colors.isDark)
                                        colors.purplePrimary.copy(alpha = 0.16f)
                                    else
                                        colors.purplePrimary.copy(alpha = 0.08f)
                                )
                                .border(
                                    1.4.dp,
                                    colors.purplePrimary.copy(alpha = 0.30f),
                                    RoundedCornerShape(22.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (appInfo != null) {
                                Image(
                                    painter = painterResource(id = appInfo.iconRes),
                                    contentDescription = appName,
                                    modifier = Modifier.size(42.dp)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Filled.Lock,
                                    contentDescription = appName,
                                    tint = colors.purpleSoft,
                                    modifier = Modifier.size(34.dp)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(colors.errorRed)
                                .border(2.dp, colors.cardSurface, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = "Blocked",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    // Main title
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "$appName is blocked",
                            color = colors.textPrimary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "This app is unavailable until your focus session ends.",
                            color = colors.textSecondary,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    // ReelBreak branded row
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(
                                if (colors.isDark)
                                    Color.White.copy(alpha = 0.04f)
                                else
                                    colors.purplePrimary.copy(alpha = 0.05f)
                            )
                            .border(
                                1.dp,
                                colors.borderSubtle,
                                RoundedCornerShape(999.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .scale(brandLogoScale)
                                .size(22.dp)
                                .clip(RoundedCornerShape(7.dp))
                                .background(colors.appColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.applogo),
                                contentDescription = "ReelBreak",
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        Text(
                            text = "Closed by ReelBreak during Focus Mode",
                            color = colors.textSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Remaining time pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(
                                if (colors.isDark)
                                    colors.purplePrimary.copy(alpha = 0.12f)
                                else
                                    colors.purplePrimary.copy(alpha = 0.08f)
                            )
                            .border(
                                1.dp,
                                colors.purplePrimary.copy(alpha = 0.28f),
                                RoundedCornerShape(999.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = null,
                            tint = colors.purpleSoft,
                            modifier = Modifier.size(16.dp)
                        )

                        Text(
                            text = "Session ends in $liveRemaining",
                            color = colors.purpleSoft,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Helper text
                    Text(
                        text = "Open Focus Mode to change blocked apps or session duration.",
                        color = colors.textMuted,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            PrimaryActionButton(
                text = "Open Focus Mode",
                onClick = onOpenFocusMode
            )

            Spacer(modifier = Modifier.height(12.dp))

            SecondaryActionButton(
                text = "Close app",
                onClick = onCloseApp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Your focus session is still running.",
                color = colors.textMuted,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PrimaryActionButton(
    text: String,
    onClick: () -> Unit
) {
    val colors = LocalAppColors.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(colors.appColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun SecondaryActionButton(
    text: String,
    onClick: () -> Unit
) {
    val colors = LocalAppColors.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(colors.cardSurface)
            .border(1.dp, colors.borderColor, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = colors.textPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
    }
}