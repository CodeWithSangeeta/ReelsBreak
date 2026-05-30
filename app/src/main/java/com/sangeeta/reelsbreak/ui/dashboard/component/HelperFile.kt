package com.sangeeta.reelsbreak.ui.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sangeeta.reelsbreak.ui.dashboard.DashboardHomeUiState
import com.sangeeta.reelsbreak.ui.theme.LocalAppColors


@Composable
 fun SurfaceCard(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = LocalAppColors.current

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(colors.cardSurface)
            .border(
                width = 1.dp,
                color = if (colors.isDark) {
                    colors.borderSubtle.copy(alpha = 0.95f)
                } else {
                    colors.borderSubtle.copy(alpha = 0.55f)
                },
                shape = RoundedCornerShape(24.dp)
            )
            .padding(innerPadding),
        content = content
    )
}


@Composable
fun VerticalDivider() {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .height(54.dp)
            .width(1.dp)
            .background(colors.borderSubtle.copy(alpha = 0.9f))
    )
}


fun mindfulRemainingShort(state: DashboardHomeUiState): String {
    val pieces = buildList {
        if (state.mindfulCountEnabled) add("${state.mindfulRemainingCount} reels")
        if (state.mindfulTimeEnabled) add("${state.mindfulRemainingMinutes}m")
    }
    return pieces.ifEmpty { listOf("—") }.joinToString(" • ")
}



