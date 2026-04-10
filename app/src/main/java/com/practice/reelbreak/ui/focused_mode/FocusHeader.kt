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
import com.practice.reelbreak.ui.component.GradientColor
import com.practice.reelbreak.ui.component.MainScaffold

@Composable
 fun FocusHeader(isFocusActive: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 52.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Guard Mode",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Block distractions, stay focused",
                color = GradientColor.TextSecondary,
                fontSize = 13.sp
            )
        }

        // Shield icon badge
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    if (isFocusActive)
                        Brush.radialGradient(listOf(Color(0x442ECC71), Color(0x002ECC71)))
                    else
                        Brush.radialGradient(listOf(Color(0x449B3DFF), Color(0x009B3DFF)))
                )
                .border(
                    1.dp,
                    color = if (isFocusActive) Color(0x882ECC71) else GradientColor.borderPurple,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Shield,
                contentDescription = "Guard",
                tint = if (isFocusActive) GradientColor.SuccessGreen else GradientColor.PurpleSoft,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}