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
 fun StartFocusButton(
    isFocusActive: Boolean,
    onToggle: () -> Unit
) {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(56.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(18.dp),
                spotColor = if (isFocusActive) Color(0x442ECC71) else colors.glowPurple,
                ambientColor = if (isFocusActive) Color(0x442ECC71) else colors.glowPurple
            )
            .clip(RoundedCornerShape(18.dp))
            .background(
                if (isFocusActive)
                    Brush.linearGradient(listOf(Color(0xFF1A7A44), Color(0xFF2ECC71)))
                else
                  colors.button
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onToggle
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = if (isFocusActive) Icons.Filled.Stop else Icons.Filled.PlayArrow,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = if (isFocusActive) "Stop Focus Session" else "Start Focus Session",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}