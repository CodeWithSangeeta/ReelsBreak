package com.practice.reelbreak.core.accessibility

import android.app.usage.UsageStatsManager
import android.content.Context

fun getForegroundPackageName(context: Context): String? {
    val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    val time = System.currentTimeMillis()
    // Query stats over the last 10 seconds to locate the active window
    val stats = usageStatsManager.queryUsageStats(
        UsageStatsManager.INTERVAL_DAILY,
        time - 10000,
        time
    )

    if (!stats.isNullOrEmpty()) {
        val sortedStats = stats.sortedByDescending { it.lastTimeUsed }
        return sortedStats.first().packageName
    }
    return null
}