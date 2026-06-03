package com.sangeeta.reelsbreak.ui.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.LottieDynamicProperties
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.sangeeta.reelsbreak.R
import com.sangeeta.reelsbreak.ui.dashboard.DashboardHomeUiState
import com.sangeeta.reelsbreak.ui.dashboard.HomeProtectionMode
import com.sangeeta.reelsbreak.ui.theme.LocalAppColors

private data class PowerButtonPalette(
    val outerRing: Color,
    val midRing: Color,
    val innerFill: Color,
    val iconColor: Color
)

private val flowPaletteDark = PowerButtonPalette(
    outerRing = Color(0xFF236543),
    midRing = Color(0xFF1DAA6A),
    innerFill = Color(0xFF7DD4A3),
    iconColor = Color(0xFF08110C)
)

private val flowPaletteLight = PowerButtonPalette(
    outerRing = Color(0xFF1D6A43),
    midRing = Color(0xFF31A96B),
    innerFill = Color(0xFFC9F1DA),
    iconColor = Color(0xFF10281A)
)

private val curiousPaletteDark = PowerButtonPalette(
    outerRing = Color(0xFF2C5B9A),
    midRing = Color(0xFF5385D9),
    innerFill = Color(0xFFA8C3F1),
    iconColor = Color(0xFF0D1522)
)

private val curiousPaletteLight = PowerButtonPalette(
    outerRing = Color(0xFF2F5FA8),
    midRing = Color(0xFF4E88E8),
    innerFill = Color(0xFFD7E6FF),
    iconColor = Color(0xFF13233D)
)

private val pausedPaletteDark = PowerButtonPalette(
    outerRing = Color(0xFF4F5568),
    midRing = Color(0xFF7C849A),
    innerFill = Color(0xFFCDD3E1),
    iconColor = Color(0xFF151922)
)

private val pausedPaletteLight = PowerButtonPalette(
    outerRing = Color(0xFF7E869B),
    midRing = Color(0xFFA7AFC2),
    innerFill = Color(0xFFEEF1F7),
    iconColor = Color(0xFF2A3140)
)

@Composable
fun ProtectionCard(
    state: DashboardHomeUiState,
    onToggle: () -> Unit,
    onModeSelected: (HomeProtectionMode) -> Unit
) {
    val colors = LocalAppColors.current

    val accent = when {
        !state.isProtectionEnabled -> colors.textMuted
        state.selectedMode == HomeProtectionMode.FLOW -> colors.successGreen
        state.selectedMode == HomeProtectionMode.CURIOUS -> colors.blueAccent
        else -> colors.pausedAccent
    }

    val palette = when (state.selectedMode) {
        HomeProtectionMode.FLOW ->
            if (colors.isDark) flowPaletteDark else flowPaletteLight

        HomeProtectionMode.CURIOUS ->
            if (colors.isDark) curiousPaletteDark else curiousPaletteLight

        HomeProtectionMode.PAUSED ->
            if (colors.isDark) pausedPaletteDark else pausedPaletteLight
    }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.protection_animation)
    )

    val isPaused = state.selectedMode == HomeProtectionMode.PAUSED

    val lottieAnimatable = rememberLottieAnimatable()

    LaunchedEffect(
        composition,
        state.isProtectionEnabled,
        state.selectedMode
    ) {
        if (composition == null) return@LaunchedEffect

        if (!state.isProtectionEnabled) {
            lottieAnimatable.snapTo(
                composition = composition,
                progress = 0f
            )
            return@LaunchedEffect
        }

        if (isPaused) {
            lottieAnimatable.snapTo(
                composition = composition,
                progress = 0.90f
            )
            return@LaunchedEffect
        }

        while (true) {
            lottieAnimatable.animate(
                composition = composition,
                initialProgress = 0f,
                speed = when (state.selectedMode) {
                    HomeProtectionMode.FLOW -> 1f
                    HomeProtectionMode.CURIOUS -> 0.7f
                    HomeProtectionMode.PAUSED -> 0f
                }
            )

            lottieAnimatable.animate(
                composition = composition,
                initialProgress = 1f,
                speed = when (state.selectedMode) {
                    HomeProtectionMode.FLOW -> -1f
                    HomeProtectionMode.CURIOUS -> -0.7f
                    HomeProtectionMode.PAUSED -> 0f
                }
            )
        }
    }
    val dynamicProperties = rememberPowerButtonDynamicProperties(palette)

    SurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        innerPadding = PaddingValues(horizontal = 18.dp, vertical = 12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PROTECTION",
                    color = colors.textPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(colors.cardSurface)
                        .border(
                            width = 1.dp,
                            color = if (state.isProtectionEnabled) accent.copy(alpha = 0.35f)
                            else colors.borderSubtle,
                            shape = RoundedCornerShape(999.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(
                                    if (state.isProtectionEnabled) accent
                                    else colors.textMuted.copy(alpha = 0.7f)
                                )
                        )
                        Text(
                            text = if (!state.isProtectionEnabled) "Inactive" else if(state.selectedMode == HomeProtectionMode.PAUSED)"Paused" else "Active",
                            color = if (state.isProtectionEnabled) accent else colors.textSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

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
                        progress = { lottieAnimatable.progress },
                        dynamicProperties = dynamicProperties,
                        modifier = Modifier
                            .size(200.dp)
                            .alpha(if (isPaused) 0.82f else 1f)
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


                Text(
                    text = when {
                        !state.isProtectionEnabled -> "Protection OFF"
                        state.selectedMode == HomeProtectionMode.PAUSED -> "Protection Paused"
                        else -> "Protection ON"
                    },
                    color = if (state.isProtectionEnabled) accent else colors.textSecondary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

            Spacer(modifier = Modifier.height(4.dp))

            ModeSelection(
                selectedMode = state.selectedMode,
                isProtectionEnabled = state.isProtectionEnabled,
                onModeSelected = onModeSelected
            )
        }
    }
}


@Composable
private fun rememberPowerButtonDynamicProperties(
    palette: PowerButtonPalette
): LottieDynamicProperties {
    return rememberLottieDynamicProperties(

        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value    = palette.iconColor.toArgb(),
            keyPath  = arrayOf("Layer 7 Outlines", "Group 1", "Fill 1")
        ),

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

        rememberLottieDynamicProperty(
            property = LottieProperty.STROKE_COLOR,
            value    = palette.midRing.toArgb(),
            keyPath  = arrayOf("Shape Layer 5", "Ellipse 1", "Stroke 1")
        ),

        rememberLottieDynamicProperty(
            property = LottieProperty.STROKE_COLOR,
            value    = palette.outerRing.toArgb(),
            keyPath  = arrayOf("Shape Layer 6", "Ellipse 1", "Stroke 1")
        ),

        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value    = palette.innerFill.toArgb(),
            keyPath  = arrayOf("Shape Layer 6", "Ellipse 1", "Fill 1")
        ),
    )
}




//package com.sangeeta.reelsbreak.ui.dashboard.component
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.outlined.Shield
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.alpha
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.toArgb
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.airbnb.lottie.LottieProperty
//import com.airbnb.lottie.compose.*
//import com.sangeeta.reelsbreak.R
//import com.sangeeta.reelsbreak.ui.dashboard.DashboardHomeUiState
//import com.sangeeta.reelsbreak.ui.dashboard.HomeProtectionMode
//import com.sangeeta.reelsbreak.ui.theme.LocalAppColors
//
//private data class PowerButtonPalette(
//    val outerRing: Color,
//    val midRing: Color,
//    val innerFill: Color,
//    val iconColor: Color
//)
//
//private val flowPaletteDark = PowerButtonPalette(Color(0xFF14532D), Color(0xFF22C55E), Color(0xFF86EFAC), Color(0xFF022C22))
//private val flowPaletteLight = PowerButtonPalette(Color(0xFFC9F1DA), Color(0xFF22C55E), Color(0xFFDCFCE7), Color(0xFF14532D))
//private val curiousPaletteDark = PowerButtonPalette(Color(0xFF1E3A8A), Color(0xFF3B82F6), Color(0xFF93C5FD), Color(0xFF031E2F))
//private val curiousPaletteLight = PowerButtonPalette(Color(0xFFD7E6FF), Color(0xFF3B82F6), Color(0xFFE0F2FE), Color(0xFF1E3A8A))
//private val pausedPaletteDark = PowerButtonPalette(Color(0xFF334155), Color(0xFF64748B), Color(0xFFCBD5E1), Color(0xFF0F172A))
//private val pausedPaletteLight = PowerButtonPalette(Color(0xFFF1F5F9), Color(0xFF64748B), Color(0xFFE2E8F0), Color(0xFF334155))
//
//@Composable
//fun ProtectionCard(
//    state: DashboardHomeUiState,
//    onToggle: () -> Unit,
//    onModeSelected: (HomeProtectionMode) -> Unit
//) {
//    val colors = LocalAppColors.current
//
//    val accent = when {
//        !state.isProtectionEnabled -> colors.textMuted
//        state.selectedMode == HomeProtectionMode.FLOW -> colors.successGreen
//        state.selectedMode == HomeProtectionMode.CURIOUS -> colors.blueAccent
//        else -> colors.pausedAccent
//    }
//
//    val palette = when (state.selectedMode) {
//        HomeProtectionMode.FLOW -> if (colors.isDark) flowPaletteDark else flowPaletteLight
//        HomeProtectionMode.CURIOUS -> if (colors.isDark) curiousPaletteDark else curiousPaletteLight
//        HomeProtectionMode.PAUSED -> if (colors.isDark) pausedPaletteDark else pausedPaletteLight
//    }
//
//    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.protection_animation))
//    val isPaused = state.selectedMode == HomeProtectionMode.PAUSED
//    val lottieAnimatable = rememberLottieAnimatable()
//
//    LaunchedEffect(composition, state.isProtectionEnabled, state.selectedMode) {
//        if (composition == null) return@LaunchedEffect
//        if (!state.isProtectionEnabled) {
//            lottieAnimatable.snapTo(composition = composition, progress = 0f)
//            return@LaunchedEffect
//        }
//        if (isPaused) {
//            lottieAnimatable.snapTo(composition = composition, progress = 0.90f)
//            return@LaunchedEffect
//        }
//        while (true) {
//            lottieAnimatable.animate(composition = composition, initialProgress = 0f, speed = if (state.selectedMode == HomeProtectionMode.FLOW) 1f else 0.7f)
//            lottieAnimatable.animate(composition = composition, initialProgress = 1f, speed = if (state.selectedMode == HomeProtectionMode.FLOW) -1f else -0.7f)
//        }
//    }
//
//    val dynamicProperties = rememberPowerButtonDynamicProperties(palette)
//
//    // Redesigned: Fluid, non-contained system layout that gets rid of the redundant card bounds
//    Column(
//        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = "ENGINE STATUS",
//                color = colors.textPrimary,
//                fontSize = 13.sp,
//                fontWeight = FontWeight.ExtraBold,
//                letterSpacing = 1.5.sp
//            )
//
//            Box(
//                modifier = Modifier
//                    .clip(RoundedCornerShape(999.dp))
//                    .background(if (state.isProtectionEnabled) accent.copy(alpha = 0.12f) else colors.textMuted.copy(alpha = 0.08f))
//                    .border(1.dp, if (state.isProtectionEnabled) accent.copy(alpha = 0.3f) else colors.borderSubtle, RoundedCornerShape(999.dp))
//                    .padding(horizontal = 12.dp, vertical = 6.dp)
//            ) {
//                Text(
//                    text = if (!state.isProtectionEnabled) "Inactive" else if (state.selectedMode == HomeProtectionMode.PAUSED) "Paused" else "Active",
//                    color = if (state.isProtectionEnabled) (if (colors.isDark) accent else colors.textPrimary) else colors.textSecondary,
//                    fontSize = 11.sp,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//        }
//
//        // Lottie Interaction Sphere
//        Box(
//            modifier = Modifier
//                .size(190.dp)
//                .clip(CircleShape)
//                .background(Brush.radialGradient(colors = listOf(accent.copy(alpha = 0.15f), accent.copy(alpha = 0.02f), Color.Transparent)))
//                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onToggle),
//            contentAlignment = Alignment.Center
//        ) {
//            if (composition != null) {
//                LottieAnimation(
//                    composition = composition,
//                    progress = { lottieAnimatable.progress },
//                    dynamicProperties = dynamicProperties,
//                    modifier = Modifier.size(190.dp).alpha(if (isPaused) 0.75f else 1f)
//                )
//            } else {
//                Icon(imageVector = Icons.Outlined.Shield, contentDescription = null, tint = accent, modifier = Modifier.size(44.dp))
//            }
//        }
//
//        Text(
//            text = when {
//                !state.isProtectionEnabled -> "Protection is Disabled"
//                state.selectedMode == HomeProtectionMode.PAUSED -> "Protection is Paused"
//                state.selectedMode == HomeProtectionMode.FLOW -> "Flow Mode Active"
//                else -> "Curious Mode Active"
//            },
//            color = if (state.isProtectionEnabled) (if (colors.isDark) accent else colors.textPrimary) else colors.textSecondary,
//            fontSize = 17.sp,
//            fontWeight = FontWeight.Bold,
//            letterSpacing = 0.3.sp
//        )
//
//        Spacer(modifier = Modifier.height(4.dp))
//
//        ModeSelection(
//            selectedMode = state.selectedMode,
//            isProtectionEnabled = state.isProtectionEnabled,
//            onModeSelected = onModeSelected
//        )
//    }
//}
//
//@Composable
//private fun rememberPowerButtonDynamicProperties(palette: PowerButtonPalette): LottieDynamicProperties {
//    return rememberLottieDynamicProperties(
//        rememberLottieDynamicProperty(property = LottieProperty.COLOR, value = palette.iconColor.toArgb(), keyPath = arrayOf("Layer 7 Outlines", "Group 1", "Fill 1")),
//        rememberLottieDynamicProperty(property = LottieProperty.STROKE_COLOR, value = palette.outerRing.toArgb(), keyPath = arrayOf("Shape Layer 1", "Ellipse 1", "Stroke 1")),
//        rememberLottieDynamicProperty(property = LottieProperty.STROKE_COLOR, value = palette.outerRing.toArgb(), keyPath = arrayOf("Shape Layer 2", "Ellipse 1", "Stroke 1")),
//        rememberLottieDynamicProperty(property = LottieProperty.STROKE_COLOR, value = palette.midRing.toArgb(), keyPath = arrayOf("Shape Layer 3", "Ellipse 1", "Stroke 1")),
//        rememberLottieDynamicProperty(property = LottieProperty.STROKE_COLOR, value = palette.midRing.toArgb(), keyPath = arrayOf("Shape Layer 4", "Ellipse 1", "Stroke 1")),
//        rememberLottieDynamicProperty(property = LottieProperty.STROKE_COLOR, value = palette.midRing.toArgb(), keyPath = arrayOf("Shape Layer 5", "Ellipse 1", "Stroke 1")),
//        rememberLottieDynamicProperty(property = LottieProperty.STROKE_COLOR, value = palette.outerRing.toArgb(), keyPath = arrayOf("Shape Layer 6", "Ellipse 1", "Stroke 1")),
//        rememberLottieDynamicProperty(property = LottieProperty.COLOR, value = palette.innerFill.toArgb(), keyPath = arrayOf("Shape Layer 6", "Ellipse 1", "Fill 1")),
//    )
//}
//
