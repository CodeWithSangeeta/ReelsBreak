package com.practice.reelbreak.ui.focused_mode


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practice.reelbreak.ui.focusedmode.FocusViewModel


@Composable
fun FocusScreen(
    modifier: Modifier = Modifier,
    viewModel: FocusViewModel = viewModel()
) {
    val selectedTime by viewModel.selectedTime.collectAsState()
    val blockedApps by viewModel.blockedApps.collectAsState()
    val isFocusActive by viewModel.isFocusActive.collectAsState()

    StartFocusScreen(
        viewModel = viewModel,
        modifier = modifier
    )
}
