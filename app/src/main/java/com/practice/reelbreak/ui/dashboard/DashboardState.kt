package com.practice.reelbreak.ui.dashboard

import com.practice.reelbreak.domain.model.ActiveBlockMode
import com.practice.reelbreak.domain.model.LimitResetPeriod

enum class BlockMode { BLOCK_NOW, LIMIT_BASED }
data class DashboardState(
    val userName: String = "Sangeeta",
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
    val overlayEnabled: Boolean = false
)
