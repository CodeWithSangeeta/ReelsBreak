package com.practice.reelbreak.ui.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector


enum class BlockMode { BLOCK_NOW, LIMIT_BASED }

data class BlockModeOption(
    val mode: BlockMode,
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val tag: String,
    val glowColor: Color
)

