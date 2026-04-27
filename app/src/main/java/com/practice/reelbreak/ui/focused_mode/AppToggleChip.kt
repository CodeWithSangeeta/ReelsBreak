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
fun AppToggleChip(
    app: AppItem,
    isBlocked: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    Box(
        modifier = modifier
            .height(72.dp)
            .shadow(
                elevation = if (isBlocked) 8.dp else 2.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = if (isBlocked) colors.glowPurple else Color.Transparent
            )
            .clip(RoundedCornerShape(16.dp))
            .background(if (isBlocked) app.gradient else colors.cardSurface)
            .border(
                width = if (isBlocked) 1.5.dp else 1.dp,
                color = if (isBlocked) colors.borderActive else colors.borderSubtle,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onToggle
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = app.emoji, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = app.name,
                color = if (isBlocked) colors.textPrimary else colors.textSecondary,
                fontSize = 10.sp,
                fontWeight = if (isBlocked) FontWeight.SemiBold else FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        }
        // Checkmark badge when blocked
        if (isBlocked) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(5.dp)
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(colors.textPrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Blocked",
                    tint = colors.purpleDeep,
                    modifier = Modifier.size(10.dp)
                )
            }
        }
    }
}
