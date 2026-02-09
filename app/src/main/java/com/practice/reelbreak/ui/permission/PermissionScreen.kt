package com.practice.reelbreak.ui.permission

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.practice.reelbreak.viewmodel.PermissionsViewModel

@Composable
fun PermissionScreen(viewModel: PermissionsViewModel) {
    // 1. Observe the State from ViewModel
    val uiState by viewModel.viewState.collectAsState()

    ReelsGuardBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- HEADER SECTION ---
            PermissionHeader()

            // --- SCROLLABLE CONTENT ---
            // We use a LazyColumn to handle small screens gracefully
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item { PrivacyNote() }

                // Accessibility Card
                item {
                    PermissionCard(
                        model = createAccessibilityModel(uiState.isAccessibilityGranted),
                        onButtonClick = { /* Trigger Intent */ }
                    )
                }

                // Usage Access Card
                item {
                    PermissionCard(
                        model = createUsageModel(uiState.isUsageGranted),
                        onButtonClick = { /* Trigger Intent */ }
                    )
                }

                // Overlay Card (Optional)
                item {
                    PermissionCard(
                        model = createOverlayModel(uiState.isOverlayGranted),
                        onButtonClick = { /* Trigger Intent */ }
                    )
                }

                item { HelpSection() }
            }

            // --- BOTTOM ACTION ---
            FooterSection(
                isContinueEnabled = uiState.isContinueEnabled,
                onContinue = { /* Navigate to Home */ }
            )
        }
    }
}