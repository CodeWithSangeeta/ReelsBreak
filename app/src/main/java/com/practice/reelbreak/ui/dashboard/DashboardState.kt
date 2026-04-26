package com.practice.reelbreak.ui.dashboard

import com.practice.reelbreak.domain.model.ActiveBlockMode

data class DashboardState(
    val userName: String = "Sangeeta",
    val isDarkMode: Boolean = false,
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

    val isOverlayEnabled: Boolean = false

)
