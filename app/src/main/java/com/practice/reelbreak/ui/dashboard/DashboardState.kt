package com.practice.reelbreak.ui.dashboard

data class DashboardState(
    val userName: String = "Sangeeta",
    val isDarkMode: Boolean = false,
    val reelsCount: Int = 0,
    val percentageIncrease: Int = 0,
    val dailyLimitMinutes: Int = 60,
    val timeSpentMinutes: Int = 0,
    val selectedTab: Int = 0,
    val isCounterVisible: Boolean = false
) {
    val limitProgress: Float
        get() = if (dailyLimitMinutes == 0) 0f else timeSpentMinutes / dailyLimitMinutes.toFloat()

    val isOverLimit: Boolean
        get() = timeSpentMinutes >= dailyLimitMinutes

    val timeSpentFormatted: String
        get() {
            val hours = timeSpentMinutes / 60
            val minutes = timeSpentMinutes % 60
            return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
        }
}
