package com.practice.reelbreak.ui.dashboard


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
import com.practice.reelbreak.ui.component.GradientColor
import com.practice.reelbreak.ui.dashboard.StatCard

@Composable
 fun QuickStatsRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            value = "0",
            label = "Blocks\nToday",
            icon = Icons.Filled.Shield,
            iconTint = GradientColor.PurpleSoft
        )
        StatCard(
            modifier = Modifier.weight(1f),
            value = "0",
            label = "Reels\nBlocked",
           // icon = Icons.Filled.VideoOff,
            icon = Icons.Filled.AcUnit,
            iconTint = GradientColor.BlueAccent
        )
        StatCard(
            modifier = Modifier.weight(1f),
            value = "0m",
            label = "Time\nSaved",
            icon = Icons.Filled.AccessTime,
            iconTint = GradientColor.SuccessGreen
        )
    }
}