package com.practice.reelbreak.data


object FocusStateHolder {
    var isFocusActive: Boolean = false
    var blockedPackages: Set<String> = emptySet()
    var focusEndTimestamp: Long = 0L

    fun getRemainingMillis(): Long =
        (focusEndTimestamp - System.currentTimeMillis()).coerceAtLeast(0L)

    fun getRemainingFormatted(): String {
        val totalSeconds = getRemainingMillis() / 1000L
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return if (hours > 0)
            String.format("%02dh %02dm", hours, minutes)
        else
            String.format("%02dm %02ds", minutes, seconds)
    }
}