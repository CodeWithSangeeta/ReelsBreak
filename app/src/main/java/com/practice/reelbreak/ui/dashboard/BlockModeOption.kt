package com.practice.reelbreak.ui.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.practice.reelbreak.ui.component.GradientColor


enum class BlockMode { BLOCK_NOW, LIMIT_BASED, SMART_FILTER }

data class BlockModeOption(
    val mode: BlockMode,
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val tag: String,
    val gradient: Brush,
    val glowColor: Color
)

val blockModeOptions = listOf(
    BlockModeOption(
        mode = BlockMode.BLOCK_NOW,
        icon = Icons.Filled.Block,
        title = "Block Instantly",
        subtitle = "Block all reels the moment you open any short-form feed",
        tag = "Strict",
        gradient = GradientColor.modeBlock,
        glowColor = Color(0x44AA00FF)
    ),
    BlockModeOption(
        mode = BlockMode.LIMIT_BASED,
        icon = Icons.Filled.Timer,
        title = "Set a Limit",
        subtitle = "Allow a fixed number of reels, then auto-block for the session",
        tag = "Balanced",
        gradient = GradientColor.modeLimit,
        glowColor = Color(0x443A5BF0)
    ),
    BlockModeOption(
        mode = BlockMode.SMART_FILTER,
        icon = Icons.Filled.Tune,
        title = "Smart Filter",
        subtitle = "Only allow reels from accounts you follow or have liked before",
        tag = "Smart",
        gradient = GradientColor.modeSmart,
        glowColor = Color(0x440D7377)
    )
)