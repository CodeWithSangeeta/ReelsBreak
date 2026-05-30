package com.practice.reelbreak.ui.dashboard.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.LottieDynamicProperties
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.practice.reelbreak.R
import com.practice.reelbreak.ui.dashboard.DashboardHomeUiState
import com.practice.reelbreak.ui.dashboard.HomeProtectionMode
import com.practice.reelbreak.ui.theme.LocalAppColors



private data class PowerButtonPalette(
    val outerRing: Color,
    val midRing: Color,
    val innerFill: Color,
    val iconColor: Color
)

private val defaultPalette = PowerButtonPalette(
    outerRing  = Color(0xFF14532D),
    midRing    = Color(0xFF22C55E),
    innerFill  = Color(0xFF86EFAC),
    iconColor  = Color(0xFF0F172A)
)

private val pausedPalette = PowerButtonPalette(
    outerRing  = Color(0xFF854D0E),
    midRing    = Color(0xFFEAB308),
    innerFill  = Color(0xFFFEF08A),
    iconColor  = Color(0xFF292524)
)

private val mindfulPalette = PowerButtonPalette(
    outerRing  = Color(0xFF1E3A8A),
    midRing    = Color(0xFF3B82F6),
    innerFill  = Color(0xFF93C5FD),
    iconColor  = Color(0xFF0F172A)
)
@Composable
 fun ProtectionCard(
    state: DashboardHomeUiState,
    onToggle: () -> Unit
) {
    val colors = LocalAppColors.current

    val accent = when {
        !state.isProtectionEnabled -> colors.textMuted
        state.selectedMode == HomeProtectionMode.DEFAULT -> colors.successGreen
        state.selectedMode == HomeProtectionMode.MINDFUL -> colors.blueAccent
        else -> colors.warningOrange
    }

    val palette = when (state.selectedMode) {
        HomeProtectionMode.DEFAULT -> defaultPalette
        HomeProtectionMode.PAUSED -> pausedPalette
        HomeProtectionMode.MINDFUL -> mindfulPalette
    }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.protection_animation)
    )

    val isPaused = state.selectedMode == HomeProtectionMode.PAUSED

    val animProgress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = state.isProtectionEnabled && !isPaused,
        iterations = LottieConstants.IterateForever,
        speed = when (state.selectedMode) {
            HomeProtectionMode.DEFAULT -> 1.0f
            HomeProtectionMode.MINDFUL -> 0.70f
            HomeProtectionMode.PAUSED -> 0f
        }
    )

    val finalProgress = if (isPaused) 0.90f else animProgress
    val dynamicProperties = rememberPowerButtonDynamicProperties(palette)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            accent.copy(alpha = 0.14f),
                            accent.copy(alpha = 0.05f),
                            Color.Transparent
                        )
                    )
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onToggle
                ),
            contentAlignment = Alignment.Center
        ) {
            if (composition != null) {
                LottieAnimation(
                    composition = composition,
                    progress = { finalProgress },
                    dynamicProperties = dynamicProperties,
                    modifier = Modifier
                        .size(224.dp)
                        .alpha(if (isPaused) 0.80f else 1f)
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Shield,
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(44.dp)
                )
            }
        }

        LiveStatusPill(
            text = rememberStatusText(state),
            color = accent,
            isEnabled = state.isProtectionEnabled
        )
    }
}






@Composable
private fun HeroInfoChip(
    title: String,
    value: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(
                if (colors.isDark) Color.White.copy(alpha = 0.04f)
                else accent.copy(alpha = 0.06f)
            )
            .border(
                width = 1.dp,
                color = accent.copy(alpha = 0.20f),
                shape = RoundedCornerShape(18.dp)
            )
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            color = colors.textPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = title,
            color = colors.textMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}



@Composable
private fun rememberPowerButtonDynamicProperties(
    palette: PowerButtonPalette
): LottieDynamicProperties {
    return rememberLottieDynamicProperties(

        // Center power icon fill
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value    = palette.iconColor.toArgb(),
            keyPath  = arrayOf("Layer 7 Outlines", "Group 1", "Fill 1")
        ),

        // Outer rings (Shape Layer 1 and 2)
        rememberLottieDynamicProperty(
            property = LottieProperty.STROKE_COLOR,
            value    = palette.outerRing.toArgb(),
            keyPath  = arrayOf("Shape Layer 1", "Ellipse 1", "Stroke 1")
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.STROKE_COLOR,
            value    = palette.outerRing.toArgb(),
            keyPath  = arrayOf("Shape Layer 2", "Ellipse 1", "Stroke 1")
        ),

        // Mid rings (Shape Layer 3 and 4)
        rememberLottieDynamicProperty(
            property = LottieProperty.STROKE_COLOR,
            value    = palette.midRing.toArgb(),
            keyPath  = arrayOf("Shape Layer 3", "Ellipse 1", "Stroke 1")
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.STROKE_COLOR,
            value    = palette.midRing.toArgb(),
            keyPath  = arrayOf("Shape Layer 4", "Ellipse 1", "Stroke 1")
        ),

        // Inner stroke ring (Shape Layer 5)
        rememberLottieDynamicProperty(
            property = LottieProperty.STROKE_COLOR,
            value    = palette.midRing.toArgb(),
            keyPath  = arrayOf("Shape Layer 5", "Ellipse 1", "Stroke 1")
        ),

        // Innermost ring stroke (Shape Layer 6)
        rememberLottieDynamicProperty(
            property = LottieProperty.STROKE_COLOR,
            value    = palette.outerRing.toArgb(),
            keyPath  = arrayOf("Shape Layer 6", "Ellipse 1", "Stroke 1")
        ),

        // Inner circle fill (Shape Layer 6 Fill)
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value    = palette.innerFill.toArgb(),
            keyPath  = arrayOf("Shape Layer 6", "Ellipse 1", "Fill 1")
        ),
    )
}



@Composable
private fun LiveStatusPill(
    text: String,
    color: Color,
    isEnabled: Boolean
) {
    val colors = LocalAppColors.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(
                if (colors.isDark) Color.White.copy(alpha = 0.05f)
                else color.copy(alpha = 0.08f)
            )
            .border(
                1.dp,
                if (isEnabled) color.copy(alpha = 0.35f) else colors.borderSubtle,
                RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(if (isEnabled) color else colors.textMuted.copy(alpha = 0.7f))
        )

        Text(
            text = text,
            color = if (isEnabled) color else colors.textSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}


private fun rememberStatusText(state: DashboardHomeUiState): String {
    return when {
        !state.isProtectionEnabled -> "Protection is off."
        state.selectedMode == HomeProtectionMode.DEFAULT ->
            "Reels will close automatically."
        state.selectedMode == HomeProtectionMode.PAUSED ->
            "Protection is paused for now."
        state.selectedMode == HomeProtectionMode.MINDFUL ->
            "Remaining: ${mindfulRemainingShort(state)}"
        else -> "Protection is ready."
    }
}
