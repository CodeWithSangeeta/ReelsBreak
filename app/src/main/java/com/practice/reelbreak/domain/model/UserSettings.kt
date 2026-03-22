package com.practice.reelbreak.domain.model

data class UserSettings(
    val scrollLimit: Int = 3,
    val cooldownMillis: Long = 5000L,
    val breakModeEnabled: Boolean = true
)