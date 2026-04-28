package com.practice.reelbreak.ui.focused_mode


import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// Map package names to readable names
private fun packageToAppName(pkg: String): String = when (pkg) {
    "com.instagram.android"    -> "Instagram"
    "com.google.android.youtube" -> "YouTube"
    "com.facebook.katana"      -> "Facebook"
    "com.zhiliaoapp.musically" -> "TikTok"
    "com.snapchat.android"     -> "Snapchat"
    "com.twitter.android"      -> "Twitter / X"
    "com.whatsapp"             -> "WhatsApp"
    else                       -> pkg.substringAfterLast(".")
        .replaceFirstChar { it.uppercase() }
}

@Composable
fun AppBlockedScreen(
    blockedPackage: String,
    remainingFormatted: String,
    focusEndTs: Long,
    onGoBack: () -> Unit
) {
    val appName = packageToAppName(blockedPackage)

    // Live countdown inside this screen
    var liveRemaining by remember { mutableStateOf(remainingFormatted) }
    LaunchedEffect(focusEndTs) {
        while (true) {
            val totalSeconds = ((focusEndTs - System.currentTimeMillis()) / 1000L)
                .coerceAtLeast(0L)
            val hours   = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60
            val seconds = totalSeconds % 60
            liveRemaining = if (hours > 0)
                String.format("%02dh %02dm remaining", hours, minutes)
            else
                String.format("%02dm %02ds remaining", minutes, seconds)
            delay(1_000L)
        }
    }

    // Pulsing animation for the lock icon
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0D0818),
                        Color(0xFF130D26),
                        Color(0xFF0A0515)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {

            // ── Pulsing lock icon ──────────────────────────────────────
            Box(
                modifier = Modifier
                    .scale(pulseScale)
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                Color(0xFF7C3AED).copy(alpha = 0.4f),
                                Color(0xFF4C1D95).copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        )
                    )
                    .border(
                        2.dp,
                        Brush.sweepGradient(
                            listOf(
                                Color(0xFF9333EA),
                                Color(0xFFA855F7),
                                Color(0xFF7C3AED),
                                Color(0xFF9333EA)
                            )
                        ),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "Blocked",
                    tint = Color(0xFFD8B4FE),
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ── App name ───────────────────────────────────────────────
            Text(
                text = appName,
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "is blocked during Focus Mode",
                color = Color.White.copy(alpha = 0.55f),
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // ── Remaining time pill ────────────────────────────────────
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF4C1D95), Color(0xFF1E3A8A))
                        )
                    )
                    .border(
                        1.dp,
                        Color(0xFFB794F4).copy(alpha = 0.6f),
                        RoundedCornerShape(999.dp)
                    )
                    .padding(horizontal = 22.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "⏱", fontSize = 16.sp)
                    Text(
                        text = liveRemaining,
                        color = Color(0xFFE9D5FF),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Motivational text ──────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1C1233))
                    .border(
                        1.dp,
                        Color(0xFF2D1B4E),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = "🧠", fontSize = 24.sp)
                    Text(
                        text = "Stay in the zone.",
                        color = Color(0xFFB794F4),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "You chose to focus. Every distraction you avoid brings you closer to your goal.",
                        color = Color.White.copy(alpha = 0.65f),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 19.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Go back to home button ─────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF6D28D9), Color(0xFF4F46E5))
                        )
                    )
                    .clickable { onGoBack() }
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "← Go Back to Home",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Secondary label ────────────────────────────────────────
            Text(
                text = "Focus session is protecting your time",
                color = Color.White.copy(alpha = 0.3f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}