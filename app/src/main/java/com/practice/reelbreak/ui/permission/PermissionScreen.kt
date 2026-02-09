package com.practice.reelbreak.ui.permission

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.practice.reelbreak.core.permission.AccessibilityPermissionChecker
import com.practice.reelbreak.core.permission.UsagePermissionChecker
import com.practice.reelbreak.ui.onboarding.component.GradientColor
import com.practice.reelbreak.viewmodel.PermissionsViewModel

@Composable
fun PermissionScreen(viewModel: PermissionsViewModel,
                     onContinue : () -> Unit) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refreshPermissionState(context)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { action ->
            when (action) {
                PermissionAction.OpenAccessibilitySettings ->
                    AccessibilityPermissionChecker.openAccessibilitySettings(context)

                PermissionAction.OpenUsageAccessSettings ->
                    UsagePermissionChecker.openUsageAccessSettings(context)

                PermissionAction.OpenOverlaySettings -> {
                    // TODO implement open overlay settings
                }
            }
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshPermissionState(context)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PermissionHeader()

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                // item { PrivacyNote() }

                items(uiState.permissionCards.size) { index ->
                    val card = uiState.permissionCards[index]

                    PermissionCard(
                        model = card,
                        onEnableClick = {
                            viewModel.onPermissionEnableClicked(card.id)
                        }
                    )
                }
            }


            //   item { HelpSection() }

            FooterSection(
                isContinueEnabled = uiState.isContinueEnabled,
                onContinue = onContinue
            )
        }
    }
}
