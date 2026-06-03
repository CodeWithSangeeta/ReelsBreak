package com.sangeeta.reelsbreak.ui.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.reelsbreak.ui.dashboard.CuriousResetPeriod
import com.sangeeta.reelsbreak.ui.dashboard.DashboardHomeUiState
import com.sangeeta.reelsbreak.ui.theme.LocalAppColors

@Composable
fun CuriousSettingsSummaryCard(
    state: DashboardHomeUiState,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current

    SurfaceCard(
        modifier = modifier.fillMaxWidth(),
        innerPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "Curious Settings",
                    color = colors.textPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = buildCuriousSummary(state),
                    color = colors.textSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(1.dp))

            Row(
                modifier = Modifier
                    .height(34.dp)
                    .background(
                        color = colors.purplePrimary.copy(alpha = if (colors.isDark) 0.18f else 0.10f),
                        shape = RoundedCornerShape(999.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = colors.purplePrimary.copy(alpha = 0.24f),
                        shape = RoundedCornerShape(999.dp)
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onEditClick
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = "Edit",
                    color = colors.textPrimary,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxWidth(0.001f)
                )
            }

            Text(
                text = "Edit",
                color = colors.textPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .background(
                        color = colors.purplePrimary.copy(alpha = if (colors.isDark) 0.18f else 0.10f),
                        shape = RoundedCornerShape(999.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = colors.purplePrimary.copy(alpha = 0.24f),
                        shape = RoundedCornerShape(999.dp)
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onEditClick
                    )
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            )
        }
    }
}

private fun buildCuriousSummary(state: DashboardHomeUiState): String {
    val countPart = if (state.curiousCountEnabled) "${state.curiousReelsLimit} reels" else null
    val timePart = if (state.curiousTimeEnabled) "${state.curiousTimeLimitMinutes} min" else null
    val resetPart = when (state.curiousResetPeriod) {
        CuriousResetPeriod.HOUR -> "Hourly"
        CuriousResetPeriod.DAY -> "Daily"
    }

    return listOfNotNull(countPart, timePart, resetPart).joinToString(" • ")
}