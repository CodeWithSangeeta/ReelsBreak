package com.practice.reelbreak.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

//val reelBreakNavItems = listOf(
//    BottomNavItem("Home", Icons.Filled.Home),
//    BottomNavItem("Focus", Icons.Filled.Shield),
//    BottomNavItem("Settings", Icons.Filled.Settings)
//)

data class ReelBreakNavItem(
    val route: String,
    val icon: ImageVector
)

val reelBreakNavItems = listOf(
    ReelBreakNavItem(
        route = "dashboard",
        icon = Icons.Filled.Home
    ),
    ReelBreakNavItem(
        route = "focus",
        icon = Icons.Filled.Shield
    ),
    ReelBreakNavItem(
        route = "settings",
        icon = Icons.Filled.Settings
    )
)