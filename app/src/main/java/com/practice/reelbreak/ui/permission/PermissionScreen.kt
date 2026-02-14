package com.practice.reelbreak.ui.permission

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.practice.reelbreak.ui.component.GradientColor
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


//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(GradientColor.background)
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(
//                    brush = Brush.verticalGradient(
//                        colors = listOf(
//                            Color(0xFF1E0E4F),  // Deep purple
//                            Color(0xFF2A1B5C),  // Purple-blue
//                            Color(0xFF3A2A6D)   // Teal-purple
//                        )
//                    )
//                )
//        )

//        Card(
//            modifier = Modifier
//                .fillMaxSize()
//                .align(Alignment.Center)
//                .padding(12.dp),
//            shape = RoundedCornerShape(32.dp),
//            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
//            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
//          //  elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GradientColor.background)
                    .glassmorphism(cornerRadius = 32.dp)
                    .padding(22.dp)
            ) {
                PermissionHeader()

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f),
                ) {
                    items(uiState.permissionCards.size) { index ->
                        val card = uiState.permissionCards[index]
                        val isExpanded = uiState.expandedCardId == card.id
                        PermissionCard(
                            model = card,
                            isExpanded = isExpanded,
                            onExpandedToggle = {
                                viewModel.onCardExpansionToggled(card.id)
                            },
                            onEnableClick = {
                                viewModel.onPermissionEnableClicked(card.id)
                            }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }
//                    HelpSection()
//
//                    Spacer(modifier = Modifier.height(24.dp))
//
//                    PermissionFooter(
//                        isContinueEnabled = uiState.isContinueEnabled,
//                        onContinue = onContinue
//                    )

                }
                PermissionFooter(
                    isContinueEnabled = uiState.isContinueEnabled,
                    onContinue = onContinue
                )
            }
        }

