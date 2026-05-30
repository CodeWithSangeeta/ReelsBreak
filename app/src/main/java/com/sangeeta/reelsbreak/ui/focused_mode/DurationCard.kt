package com.practice.reelbreak.ui.focused_mode

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.reelbreak.ui.theme.LocalAppColors

private data class DurationChip(val minutes: Int, val label: String)

private val durationChips = listOf(
    DurationChip(15,   "15m"),   DurationChip(30,   "30m"),
    DurationChip(60,   "1h"),    DurationChip(120,  "2h"),
    DurationChip(240,  "4h"),    DurationChip(480,  "8h"),
    DurationChip(1440, "1d")
)

@Composable
 fun DurationCard(
    selectedMinutes: Int,
    enabled: Boolean,
    onSelect: (Int) -> Unit
) {
    val colors = LocalAppColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        padding  = PaddingValues(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            CardSectionTitle("Session length")

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(durationChips) { chip ->
                    val isSelected = chip.minutes == selectedMinutes
                    val bg = if (isSelected) colors.appColor else colors.cardSurface

                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(bg)
                            .border(
                                width = if (isSelected) 0.dp else 1.dp,
                                color = colors.borderSubtle,
                                shape = RoundedCornerShape(999.dp)
                            )
                            .then(
                                if (enabled) Modifier.clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication        = null
                                ) { onSelect(chip.minutes) } else Modifier
                            )
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text       = chip.label,
                            color      = if (isSelected) Color.White else colors.textSecondary,
                            fontSize   = 14.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}