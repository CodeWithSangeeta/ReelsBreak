package com.practice.reelbreak.ui.dashboard

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.reelbreak.ui.navigation.Routes
import com.practice.reelbreak.ui.theme.LocalAppColors

data class NavItem(
    val icon: ImageVector,
    val label: String,
    val route: String
)

val navItems = listOf(
    NavItem(Icons.Filled.Home, "Home", Routes.DASHBOARD),
    NavItem(Icons.Filled.Shield, "Focus", Routes.FOCUS),
    NavItem(Icons.Filled.Settings, "Settings", Routes.SETTINGS)
)

@Composable
fun FloatingButtonGroup(
    selectedRoute: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(74.dp)
                .shadow(
                    elevation = 24.dp,
                    shape = RoundedCornerShape(30.dp),
                    ambientColor = colors.glowPurple,
                    spotColor = colors.glowPurple
                )
                .clip(RoundedCornerShape(30.dp))
                .background(
                    brush = colors.glassSurface
                )
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        listOf(
                            colors.borderSubtle,
                            colors.borderPurple,
                            colors.borderSubtle
                        )
                    ),
                    shape = RoundedCornerShape(30.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(74.dp)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                navItems.forEach { item ->
                    GlassNavItem(
                        item = item,
                        isSelected = selectedRoute == item.route,
                        onClick = { onItemSelected(item.route) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
@Composable
private fun GlassNavItem(
    item: NavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current

    val pillHeight by animateDpAsState(
        targetValue = if (isSelected) 50.dp else 46.dp,
        animationSpec = tween(220, easing = FastOutSlowInEasing),
        label = "pillHeight"
    )

    val iconSize by animateDpAsState(
        targetValue = if (isSelected) 18.dp else 22.dp,
        animationSpec = tween(220, easing = FastOutSlowInEasing),
        label = "iconSize"
    )

    val activeAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(220, easing = FastOutSlowInEasing),
        label = "activeAlpha"
    )

    val iconTint by animateColorAsState(
        targetValue = if (isSelected) colors.textPrimary else colors.textMuted,
        animationSpec = tween(220),
        label = "iconTint"
    )

    Box(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(pillHeight)
                .padding(horizontal = 2.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(
                    if (isSelected) {
                        Brush.verticalGradient(
                            listOf(
                                Color.White.copy(alpha = if (colors.isDark) 0.16f else 0.92f),
                                Color.White.copy(alpha = if (colors.isDark) 0.08f else 0.65f)
                            )
                        )
                    } else {
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color.Transparent
                            )
                        )
                    }
                )
                .border(
                    width = if (isSelected) 1.dp else 0.dp,
                    brush = if (isSelected) {
                        Brush.verticalGradient(
                            listOf(
                                colors.borderPurple.copy(alpha = 0.95f),
                                colors.borderSubtle.copy(alpha = 0.65f)
                            )
                        )
                    } else {
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Transparent)
                        )
                    },
                    shape = RoundedCornerShape(22.dp)
                )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(pillHeight)
                .padding(horizontal = 2.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            colors.purpleSoft.copy(alpha = 0.14f * activeAlpha),
                            colors.purpleDeep.copy(alpha = 0.18f * activeAlpha)
                        )
                    )
                )
        )

        if (isSelected) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = iconTint,
                    modifier = Modifier.size(iconSize)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.label,
                    color = colors.textPrimary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
            }
        } else {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = iconTint,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}