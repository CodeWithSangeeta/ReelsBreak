package com.sangeeta.reelsbreak.ui.dashboard

import com.sangeeta.reelsbreak.domain.model.ActiveBlockMode
import com.sangeeta.reelsbreak.domain.model.LimitResetPeriod
import com.sangeeta.reelsbreak.domain.model.ProtectionMode

enum class BlockMode { BLOCK_NOW, LIMIT_BASED }
data class DashboardState(
    val userName: String = "sangeeta",
    val isDarkMode: Boolean = true,
    val reelsCount: Int = 0,
    val percentageIncrease: Int = 0,
    val dailyLimitMinutes: Int = 60,
    val timeSpentMinutes: Int = 0,
    val selectedTab: Int = 0,
    val isCounterVisible: Boolean = false,
    val isStrictMode: Boolean = true,
   val expandedMode: BlockMode? = null,
    val activeMode: ActiveBlockMode = ActiveBlockMode.STRICT,
    val dailyReelLimit: Int = 0,
    val dailyTimeLimitMinutes: Int = 0,
    val mindfulRemainingCount: Int = 0,
    val mindfulRemainingMinutes: Int = 0,
    val limitResetPeriod: LimitResetPeriod = LimitResetPeriod.DAY,
    val overlayEnabled: Boolean = false,
    val protectionMode: ProtectionMode = ProtectionMode.DEFAULT
)
