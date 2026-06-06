package com.sangeeta.reelsbreak.ui.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sangeeta.reelsbreak.ui.theme.LocalAppColors

@Composable
fun SurfaceCard(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(16.dp),
    useTransparentBorder: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = LocalAppColors.current

    val borderColor = if (colors.isDark) {
        Color.White.copy(alpha = 0.06f)
    } else {
        Color.Black.copy(alpha = 0.06f)
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(colors.cardSurface)
            .border(
                width = if (useTransparentBorder) 0.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(innerPadding),
        content = content
    )
}