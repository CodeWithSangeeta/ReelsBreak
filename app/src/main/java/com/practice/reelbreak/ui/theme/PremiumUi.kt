package com.practice.reelbreak.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object PremiumShapes {
    val hero = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)
    val card = RoundedCornerShape(22.dp)
    val innerCard = RoundedCornerShape(16.dp)
    val pill = RoundedCornerShape(999.dp)
}


@Composable
fun premiumCardBorderColor(): Color {
    val colors = LocalAppColors.current
    return if (colors.isDark) colors.borderSubtle else colors.borderPurple.copy(alpha = 0.18f)
}

@Composable

fun premiumGlassBorderColor(): Color {
    val colors = LocalAppColors.current
    return if (colors.isDark) colors.borderPurple.copy(alpha = 0.56f) else colors.borderPurple.copy(alpha = 0.20f)
}

@Composable
fun premiumIconBubbleColor(): Color {
    val colors = LocalAppColors.current
    return if (colors.isDark) {
        Color.White.copy(alpha = 0.08f)
    } else {
        colors.purplePrimary.copy(alpha = 0.08f)
    }
}

@Composable
fun premiumChipColor(): Color {
    val colors = LocalAppColors.current
    return if (colors.isDark) {
        Color.White.copy(alpha = 0.07f)
    } else {
        colors.purplePrimary.copy(alpha = 0.07f)
    }
}




@Composable
fun PremiumIconBubble(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(premiumIconBubbleColor())
            .border(1.dp, premiumGlassBorderColor(), CircleShape),
        contentAlignment = Alignment.Center,
        content = content
    )
}

@Composable
fun PremiumPill(
    modifier: Modifier = Modifier,
    background: Color,
    border: Color,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(PremiumShapes.pill)
            .background(background)
            .border(1.dp, border, PremiumShapes.pill),
        contentAlignment = Alignment.Center,
        content = content
    )
}

@Composable
fun PremiumSectionTitle(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            color = colors.textPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = subtitle,
            color = colors.textSecondary,
            fontSize = 12.sp,
            lineHeight = 18.sp
        )
    }
}

