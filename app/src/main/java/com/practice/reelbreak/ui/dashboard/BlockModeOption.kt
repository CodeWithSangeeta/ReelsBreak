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

val blockModeOptions = listOf(
    BlockModeOption(
        mode = BlockMode.BLOCK_NOW,
        icon = Icons.Filled.Block,
        title = "Block Instantly",
        subtitle = "Block all reels the moment you open any short-form feed",
        tag = "Strict",
        glowColor = Color(0x44AA00FF)
    ),
    BlockModeOption(
        mode = BlockMode.LIMIT_BASED,
        icon = Icons.Filled.Timer,
        title = "Set a Limit",
        subtitle = "Allow a fixed number of reels, then auto-block for the session",
        tag = "Balanced",
        glowColor = Color(0x443A5BF0)
    )
)