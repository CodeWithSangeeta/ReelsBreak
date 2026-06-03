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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.reelsbreak.ui.dashboard.CuriousResetPeriod
import com.sangeeta.reelsbreak.ui.theme.LocalAppColors
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuriousSetupBottomSheet(
    reelsLimit: Int,
    timeLimitMinutes: Int,
    resetPeriod: CuriousResetPeriod,
    onReelsLimitChange: (Int) -> Unit,
    onTimeLimitChange: (Int) -> Unit,
    onResetPeriodChange: (CuriousResetPeriod) -> Unit,
    onDismiss: () -> Unit,
    onSaveClick: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    val colors = LocalAppColors.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .background(colors.sheetDragHandle, RoundedCornerShape(999.dp))
                    .fillMaxWidth(0.14f)
                    .padding(vertical = 2.dp)
            )
        },
        containerColor = androidx.compose.ui.graphics.Color.Transparent
    ) {
        SurfaceCard(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            innerPadding = PaddingValues(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                Text(
                    text = "Curious Mode",
                    color = colors.textPrimary,
                    fontSize = 24.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Watch intentionally with self-imposed limits.",
                    color = colors.textSecondary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                SliderSettingBlock(
                    title = "Daily Reel Limit",
                    valueText = "$reelsLimit reels"
                ) {
                    Slider(
                        value = reelsLimit.toFloat(),
                        onValueChange = { onReelsLimitChange(it.roundToInt().coerceAtLeast(1)) },
                        valueRange = 1f..100f,
                        colors = sliderColors()
                    )
                }

                SliderSettingBlock(
                    title = "Daily Time Limit",
                    valueText = "$timeLimitMinutes min"
                ) {
                    Slider(
                        value = timeLimitMinutes.toFloat(),
                        onValueChange = { onTimeLimitChange(it.roundToInt().coerceAtLeast(5)) },
                        valueRange = 5f..180f,
                        colors = sliderColors()
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Reset Period",
                        color = colors.textPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ResetChip(
                            title = "Hourly",
                            selected = resetPeriod == CuriousResetPeriod.HOUR,
                            onClick = { onResetPeriodChange(CuriousResetPeriod.HOUR) },
                            modifier = Modifier.weight(1f)
                        )
                        ResetChip(
                            title = "Daily",
                            selected = resetPeriod == CuriousResetPeriod.DAY,
                            onClick = { onResetPeriodChange(CuriousResetPeriod.DAY) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                PrimarySheetButton(
                    text = "Save Settings",
                    onClick = onSaveClick
                )
            }
        }
    }
}

@Composable
private fun SliderSettingBlock(
    title: String,
    valueText: String,
    content: @Composable () -> Unit
) {
    val colors = LocalAppColors.current
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = colors.textPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = valueText,
                color = colors.textSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
        content()
    }
}

@Composable
private fun ResetChip(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    Box(
        modifier = modifier
            .background(
                color = if (selected) {
                    colors.purplePrimary.copy(alpha = if (colors.isDark) 0.18f else 0.10f)
                } else {
                    androidx.compose.ui.graphics.Color.Transparent
                },
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = if (selected) {
                    colors.purplePrimary.copy(alpha = 0.34f)
                } else {
                    colors.borderSubtle.copy(alpha = if (colors.isDark) 0.36f else 0.12f)
                },
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = colors.textPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun sliderColors() = SliderDefaults.colors(
    thumbColor = LocalAppColors.current.purplePrimary,
    activeTrackColor = LocalAppColors.current.purplePrimary,
    inactiveTrackColor = LocalAppColors.current.borderSubtle.copy(alpha = 0.25f)
)