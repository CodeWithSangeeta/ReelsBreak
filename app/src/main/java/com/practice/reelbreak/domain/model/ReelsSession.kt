package com.practice.reelbreak.domain.model

data class ReelsSession(
    var currentApp: String? = null,
    var reelsMode: Boolean = false,
    var scrollCount: Int = 0,
    var lastScrollTime: Long = 0L,
    var lastBlockTime: Long = 0L,
    var lastReelHash: Int = 0
)