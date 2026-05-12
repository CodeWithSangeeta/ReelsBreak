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
fun premiumHeroBrush(): Brush {
    val colors = LocalAppColors.current
    return if (colors.isDark) {
        Brush.linearGradient(
            listOf(
                colors.purpleDeep.copy(alpha = 0.96f),
                colors.purplePrimary.copy(alpha = 0.72f),
                colors.blueAccent.copy(alpha = 0.30f)
            )
        )
    } else {
        Brush.linearGradient(
            listOf(
                colors.purpleSoft.copy(alpha = 0.18f),
                colors.purplePrimary.copy(alpha = 0.10f),
                colors.blueAccent.copy(alpha = 0.06f)
            )
        )
    }
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
fun PremiumCard(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(18.dp),
    content: @Composable BoxScope.() -> Unit
) {
    val colors = LocalAppColors.current

    Box(
        modifier = modifier
            .shadow(
                elevation = if (colors.isDark) 12.dp else 4.dp,
                shape = PremiumShapes.card,
                ambientColor = colors.glowPurple.copy(alpha = if (colors.isDark) 0.16f else 0.05f),
                spotColor = colors.glowPurple.copy(alpha = if (colors.isDark) 0.16f else 0.05f)
            )
            .clip(PremiumShapes.card)
            .background(colors.cardSurface)
            .border(1.dp, premiumCardBorderColor(), PremiumShapes.card)
            .padding(padding),
        content = content
    )
}

@Composable
fun PremiumGlassCard(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(16.dp),
    content: @Composable BoxScope.() -> Unit
) {
    val colors = LocalAppColors.current

    Box(
        modifier = modifier
            .shadow(
                elevation = if (colors.isDark) 18.dp else 6.dp,
                shape = PremiumShapes.card,
                ambientColor = colors.glowPurple.copy(alpha = if (colors.isDark) 0.22f else 0.08f),
                spotColor = colors.glowPurple.copy(alpha = if (colors.isDark) 0.22f else 0.08f)
            )
            .clip(PremiumShapes.card)
            .background(colors.glassSurface)
            .border(1.dp, premiumGlassBorderColor(), PremiumShapes.card)
            .padding(padding),
        content = content
    )
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

@Composable
fun PremiumHeroHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    actions: @Composable (() -> Unit)? = null,
    bottomContent: @Composable (() -> Unit)? = null
) {
    val colors = LocalAppColors.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(PremiumShapes.hero)
            .background(premiumHeroBrush())
            .padding(horizontal = 20.dp)
            .padding(top = 48.dp, bottom = 22.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = title,
                        color = colors.textPrimary,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subtitle,
                        color = colors.textPrimary.copy(alpha = if (colors.isDark) 0.76f else 0.84f),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
                if (actions != null) actions()
            }

            if (bottomContent != null) bottomContent()
        }
    }
}