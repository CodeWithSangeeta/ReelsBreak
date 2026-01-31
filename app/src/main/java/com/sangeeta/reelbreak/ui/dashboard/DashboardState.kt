package com.sangeeta.reelbreak.ui.dashboard


data class DashboardState(
    val userName: String = "Sangeeta",
    val reelsCount: Int = 202,
    val percentageIncrease: Int = 23,
    val limitProgress: Float = 0.75f,
    val timeSpent: String = "2h 22m",
    val dailyLimit: String = "60m",
    val isOverLimit: Boolean = true,
    val selectedTab: Int = 0
)