package com.practice.reelbreak.ui.dashboard

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.practice.reelbreak.ui.permission.PermissionSheetType

enum class HomeProtectionMode {
    DEFAULT, PAUSED, MINDFUL
}

enum class MindfulResetPeriod {
    HOUR, DAY
}

@Immutable
data class DashboardHomeUiState(
    val isProtectionEnabled: Boolean = false,
    val selectedMode: HomeProtectionMode = HomeProtectionMode.DEFAULT,
    val accessibilityGranted: Boolean = true,
    val overlayEnabled: Boolean = false,
    val mindfulCountEnabled: Boolean = true,
    val mindfulTimeEnabled: Boolean = true,
    val mindfulReelsLimit: Int = 10,
    val mindfulTimeLimitMinutes: Int = 20,
    val mindfulResetPeriod: MindfulResetPeriod = MindfulResetPeriod.HOUR,
    val reelsClosedToday: Int = 0,
    val timeBackTodayMinutes: Int = 0,
    val mindfulRemainingCount: Int = 7,
    val mindfulRemainingMinutes: Int = 12,
)


data class PermissionPagerItem(
    val type: PermissionSheetType,
    val icon: ImageVector,
    val iconTint: Color,
    val title: String,
    val description: String,
    val buttonText: String
)