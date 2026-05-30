package com.sangeeta.reelsbreak.ui.dashboard.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScaffold(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val selectedRoute = when (selectedTab) {
        0 -> "home"
        1 -> "focus"
        2 -> "settings"
        else -> "home"
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize().padding(bottom = 88.dp)) {
            content(PaddingValues(bottom = 12.dp))
        }

        FloatingButtonGroup(
            selectedRoute = selectedRoute,
            onItemSelected = { route ->
                val index = when (route) {
                    "home" -> 0
                    "focus" -> 1
                    "settings" -> 2
                    else -> 0
                }
                onTabSelected(index)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}