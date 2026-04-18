package com.practice.reelbreak.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.practice.reelbreak.ui.dashboard.FloatingButtonGroup

@Composable
fun MainScaffold(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    // Map Int index → route string for the new FloatingButtonGroup
    val selectedRoute = when (selectedTab) {
        0 -> "dashboard"
        1 -> "focus"
        2 -> "settings"
        else -> "dashboard"
    }

    Box(modifier = Modifier.fillMaxSize()) {
        content(PaddingValues())

        FloatingButtonGroup(
            selectedRoute = selectedRoute,
            onItemSelected = { route ->
                // Map route string → Int index for the existing callers
                val index = when (route) {
                    "dashboard" -> 0
                    "focus"     -> 1
                    "settings"  -> 2
                    else        -> 0
                }
                onTabSelected(index)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(top= 20.dp, bottom = 24.dp)
        )
    }
}