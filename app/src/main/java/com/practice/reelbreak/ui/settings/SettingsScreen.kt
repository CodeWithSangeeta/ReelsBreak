package com.practice.reelbreak.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.practice.reelbreak.ui.component.MainScaffold
import com.practice.reelbreak.ui.theme.LocalAppColors

@Composable
fun SettingsScreen(
    navController: NavController,
    selectedTab: Int = 2,
    onTabSelected: (Int) -> Unit,
    viewModel: SettingsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshPermissions()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    MainScaffold(selectedTab = selectedTab, onTabSelected = onTabSelected) { paddingValues ->
        SettingsContent(
            uiState = uiState,
            paddingValues = paddingValues,
            onOpenPermissionSheet = { type -> viewModel.openPermissionSheet(type) },
            onOpenPermissionSettings = viewModel::openPermissionSettings,
            onToggleNotifications = viewModel::toggleNotifications,
            onToggleWeekendRelax = viewModel::toggleWeekendRelax,
            onTogglePrivacySection = viewModel::togglePrivacySection,
            onToggleHelpSection = viewModel::toggleHelpSection,
            onShareApp = viewModel::shareApp,
            onRateApp = viewModel::rateApp,
            onSendFeedback = viewModel::sendFeedback,
            onContactUs = viewModel::contactUs,
            onPrivacyPolicy = viewModel::openPrivacyPolicy,
            onCloseSheet = viewModel::closePermissionSheet
        )
    }
}



 data class PermissionSheetData(
    val title: String,
    val description: String,
    val whyNeeded: String,
    val howToEnable: String
)


@Composable
 fun RowDivider(horizontal: androidx.compose.ui.unit.Dp = 0.dp) {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontal)
            .height(1.dp)
            .background(colors.borderSubtle)
    )
}