package com.sangeeta.reelsbreak.ui.dashboard

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.sangeeta.reelsbreak.ui.permission.PermissionSheetType

enum class HomeProtectionMode {
    FLOW, PAUSED, CURIOUS
}

enum class CuriousResetPeriod {
    HOUR, DAY
}

@Immutable
data class DashboardHomeUiState(
    val isProtectionEnabled: Boolean = false,
    val selectedMode: HomeProtectionMode = HomeProtectionMode.PAUSED,
    val accessibilityGranted: Boolean = true,
    val overlayEnabled: Boolean = false,
    val curiousCountEnabled: Boolean = true,
    val curiousTimeEnabled: Boolean = true,
    val curiousReelsLimit: Int = 10,
    val curiousTimeLimitMinutes: Int = 20,
    val curiousResetPeriod: CuriousResetPeriod = CuriousResetPeriod.HOUR,
    val reelsClosedToday: Int = 0,
    val timeBackTodayMinutes: Int = 0,
    val curiousRemainingCount: Int = 7,
    val curiousRemainingMinutes: Int = 12,
)


data class PermissionPagerItem(
    val type: PermissionSheetType,
    val icon: ImageVector,
    val iconTint: Color,
    val title: String,
    val description: String,
    val buttonText: String
)