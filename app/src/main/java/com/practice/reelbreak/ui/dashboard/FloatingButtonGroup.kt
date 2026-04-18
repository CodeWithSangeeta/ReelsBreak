package com.practice.reelbreak.ui.dashboard


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


//@Composable
//fun FloatingButtonGroup(
//    selectedTab: Int,
//    onTabSelected: (Int) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val navItems = listOf(
//        Icons.Outlined.Shield,
//        Icons.Outlined.BarChart,
//        Icons.Outlined.ElectricBolt,
//        Icons.Outlined.CalendarMonth,
//    )
//
//    Surface(
//        color = Color.Black.copy(alpha = 0.8f),
//        shape = RoundedCornerShape(28.dp),
//        border = BorderStroke(1.dp,Color.White.copy(alpha = 0.1f)),
//        shadowElevation = 12.dp,
//        modifier = modifier
//            .width(340.dp)
//            .height(72.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 8.dp),
//            horizontalArrangement = Arrangement.SpaceEvenly,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            navItems.forEachIndexed { index, icon ->
//                FloatingNavButton(
//                    icon = icon,
//                    isSelected = selectedTab == index,
//                    onClick = { onTabSelected(index) }
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun FloatingNavButton(
//    icon: ImageVector,
//    isSelected: Boolean,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val gradient = Brush.verticalGradient(
//        colors = listOf(
//            Color(0xFFB490FF),
//            Color(0xFF3F51B5)
//        )
//    )
//    val gradient2 = Brush.verticalGradient(
//        colors = listOf(Color.Transparent, Color.Transparent))
//
//    Box(
//        modifier = modifier
//            .size(50.dp)
//            .clip(RoundedCornerShape(16.dp))
//            .background(
//                if (isSelected) gradient
//                else  gradient2
//            )
//            .clickable { onClick() },
//        contentAlignment = Alignment.Center
//    ) {
//        Icon(
//            imageVector = icon,
//            contentDescription = null,
//            tint = if (isSelected) Color.White else  Color.White.copy(alpha = 0.7f),
//            modifier = Modifier.size(28.dp)
//        )
//    }
//}



import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.practice.reelbreak.ui.theme.LocalAppColors

data class NavItem(
    val icon: ImageVector,
    val label: String,
    val route: String
)

val navItems = listOf(
    NavItem(Icons.Filled.Home,     "Home",     "dashboard"),
    NavItem(Icons.Filled.Shield,   "Guard",    "focus"),
    NavItem(Icons.Filled.Settings, "Settings", "settings")
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
            .padding(horizontal = 24.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Outer glow shadow layer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .shadow(
                    elevation = 24.dp,
                    shape = RoundedCornerShape(36.dp),
                    ambientColor = colors.glowPurple,
                    spotColor = colors.glowPurple
                )
        )

        // Glass card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0x99120D1E),
                            Color(0xBB1A1228),
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                         colors.borderPurple,
                          colors.borderSubtle,
                            colors.borderPurple
                        )
                    ),
                    shape = RoundedCornerShape(36.dp)
                )
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEach { item ->
                NavBarItem(
                    item = item,
                    isSelected = selectedRoute == item.route,
                    onClick = { onItemSelected(item.route) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun NavBarItem(
    item: NavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val iconColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else colors.textMuted,
        animationSpec = tween(durationMillis = 250),
        label = "iconColor"
    )

    val pillHeight by animateDpAsState(
        targetValue = if (isSelected) 48.dp else 40.dp,
        animationSpec = tween(durationMillis = 250),
        label = "pillHeight"
    )

    Box(
        modifier = modifier
            .height(pillHeight)
            .clip(RoundedCornerShape(24.dp))
            .background(
                if (isSelected) {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF9B3DFF),
                            Color(0xFF5A0EA8)
                        )
                    )
                } else {
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Transparent)
                    )
                }
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            // Selected: icon + label stacked
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = iconColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.label,
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        } else {
            // Unselected: icon only
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
