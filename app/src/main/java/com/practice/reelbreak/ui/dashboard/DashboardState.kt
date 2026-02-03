package com.practice.reelbreak.ui.dashboard

//
//data class DashboardState(
//    val userName: String = "Sangeeta",
//    val reelsCount: Int = 202,
//    val percentageIncrease: Int = 23,
//    val limitProgress: Float = 0.75f,
//    val timeSpent: String = "2h 22m",
//    val dailyLimit: String = "60m",
//    val isOverLimit: Boolean = true,
//    val selectedTab: Int = 0
//)



data class DashboardState(
    val userName: String,
    val isDarkMode: Boolean,

    // Reel stats
    val reelsCount: Int,
    val percentageIncrease: Int,

    // Limit & progress
    val dailyLimitMinutes: Int,
    val timeSpentMinutes: Int,

    // UI derived state
    val selectedTab: Int,
    val isCounterVisible: Boolean
) {
    // Derived values (NO logic in UI)
    val limitProgress: Float
        get() =
            if (dailyLimitMinutes == 0) 0f
            else timeSpentMinutes / dailyLimitMinutes.toFloat()

    val isOverLimit: Boolean
        get() = timeSpentMinutes > dailyLimitMinutes

    val timeSpentFormatted: String
        get() {
            val hours = timeSpentMinutes / 60
            val minutes = timeSpentMinutes % 60
            return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
        }
}
