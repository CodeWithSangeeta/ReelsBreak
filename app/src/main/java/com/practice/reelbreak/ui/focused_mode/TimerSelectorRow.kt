package com.practice.reelbreak.ui.focused_mode

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.practice.reelbreak.ui.component.MainScaffold
import com.practice.reelbreak.ui.theme.LocalAppColors


@Composable
 fun TimerSelectorRow(
    selectedTime: Long,
    onTimeSelected: (Int) -> Unit
) {
    val times = listOf(15, 30, 45, 60)
    val colors = LocalAppColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        times.forEach { minutes ->
            val isSelected = selectedTime == minutes * 60 * 1000L
            val bgColor by animateColorAsState(
                targetValue = if (isSelected) colors.purplePrimary else Color.Transparent,
                animationSpec = tween(200),
                label = "timerBg"
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (isSelected) Brush.verticalGradient(
                            listOf(Color(0xFF9B3DFF), Color(0xFF5A0EA8))
                        ) else colors.cardSurface
                    )
                    .border(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) colors.borderActive else colors.borderSubtle,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onTimeSelected(minutes) }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$minutes",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "min",
                        color = if (isSelected) Color(0xCCFFFFFF) else colors.textMuted,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}
