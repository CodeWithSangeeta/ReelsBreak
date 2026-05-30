package com.sangeeta.reelsbreak.ui.focused_mode

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.reelsbreak.ui.theme.LocalAppColors


@Composable
fun Card(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(18.dp),
    content: @Composable BoxScope.() -> Unit
) {
    val colors = LocalAppColors.current

    Box(
        modifier = modifier
            .shadow(
                elevation = if (colors.isDark) 12.dp else 4.dp,
                shape =  RoundedCornerShape(20.dp),
                ambientColor = colors.glowPurple.copy(alpha = if (colors.isDark) 0.16f else 0.05f),
                spotColor = colors.glowPurple.copy(alpha = if (colors.isDark) 0.16f else 0.05f)
            )
            .clip(shape = RoundedCornerShape(20.dp))
            .background(colors.cardSurface)
            .border(1.dp, colors.borderColor, shape = RoundedCornerShape(20.dp))
            .padding(padding),
        content = content
    )
}


@Composable
fun CardSectionTitle(title: String) {
    val colors = LocalAppColors.current
    Text(
        text       = title,
        color      = colors.textPrimary,
        fontSize   = 17.sp,
        fontWeight = FontWeight.SemiBold
    )
}