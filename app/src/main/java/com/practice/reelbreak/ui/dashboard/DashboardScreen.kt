package com.practice.reelbreak.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.practice.reelbreak.core.overlay.OverlayService
import com.practice.reelbreak.domain.model.ActiveBlockMode
import com.practice.reelbreak.ui.component.MainScaffold
import com.practice.reelbreak.ui.limit.LimitSettingsContent
import com.practice.reelbreak.ui.permission.PermissionBottomSheet
import com.practice.reelbreak.ui.permission.PermissionSheetType
import com.practice.reelbreak.ui.theme.LocalAppColors
import com.practice.reelbreak.viewmodel.DashboardViewModel
import com.practice.reelbreak.viewmodel.PermissionsViewModel


private data class PermissionChip(
    val type: PermissionSheetType,
    val title: String,
    val subtitle: String
)

data class PermissionPagerItem(
    val type: PermissionSheetType,
    val title: String,
    val description: String,
    val buttonText: String
)



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    dashboardViewModel: DashboardViewModel = viewModel(),
    permissionsViewModel: PermissionsViewModel = viewModel(),
    selectedTab: Int = 0,
    onTabSelected: (Int) -> Unit
) {
    val dashboardState by dashboardViewModel.uiState.collectAsState()
    val colors = LocalAppColors.current
    val context = LocalContext.current

    val sheetState by permissionsViewModel.sheetState.collectAsState()
    val permModalState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    val permissionUiState by permissionsViewModel.uiState.collectAsState()
    val permissionState = permissionUiState.permissionState


    LaunchedEffect(dashboardState.isOverlayEnabled, permissionState.overlayGranted) {
        if (dashboardState.isOverlayEnabled && permissionState.overlayGranted) {
            OverlayService.start(context)
        } else {
            OverlayService.stop(context)
        }
    }


    // Base list of all possible permission cards
    val basePermissionPagerItems = listOf(
        PermissionPagerItem(
            type = PermissionSheetType.ACCESSIBILITY,
            title = "Accessibility Service Required",
            description = "ReelBreak needs Accessibility Service to detect reels and block distracting content.",
            buttonText = "Turn On"
        ),
        PermissionPagerItem(
            type = PermissionSheetType.USAGE_ACCESS,
            title = "Usage Access Needed",
            description = "Grant usage access to calculate how long you spend on Shorts, Reels and TikTok.",
            buttonText = "Grant Access"
        ),
        PermissionPagerItem(
            type = PermissionSheetType.OVERLAY,
            title = "Overlay Permission (Optional)",
            description = "Allow a tiny bubble overlay to show live counters and helpful nudges.",
            buttonText = "Enable Overlay"
        )
    )

    // Only keep cards for permissions that are NOT granted
    val missingPermissionItems = basePermissionPagerItems.filter { item ->
        when (item.type) {
            PermissionSheetType.ACCESSIBILITY -> !permissionState.accessibilityGranted
            PermissionSheetType.USAGE_ACCESS  -> !permissionState.usageStatsGranted
            PermissionSheetType.OVERLAY       -> !permissionState.overlayGranted
        }
    }

    // Check permissions every time Dashboard opens (initial UX nudging)
    LaunchedEffect(Unit) {
        permissionsViewModel.checkAndShowSheetIfNeeded(context)
    }

    // Show bottom sheet when triggered
    if (sheetState.isVisible && sheetState.type != null) {
        PermissionBottomSheet(
            type = sheetState.type!!,
            sheetState = permModalState,
            onDismiss = { permissionsViewModel.dismissSheet() },
            onAgree = { permissionsViewModel.onPermissionSheetAgree(context, sheetState.type!!) },
        )
    }

    MainScaffold(selectedTab = selectedTab, onTabSelected = onTabSelected) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(colors.background)
                .padding(horizontal = 24.dp)
        ) {
            DashboardHeader(
                onVisibilityToggle = { dashboardViewModel.toggleCounterVisibility() },
                onThemeToggle = { dashboardViewModel.toggleTheme() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ── Permission pager: only for missing permissions ─────────────────────
            val shouldShowPermissionPager = missingPermissionItems.isNotEmpty()

            if (shouldShowPermissionPager) {
                val pagerState = rememberPagerState(
                    initialPage = 0,
                    pageCount = { missingPermissionItems.size }
                )

                Spacer(Modifier.height(12.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                    ) { page ->
                        val item = missingPermissionItems[page]
                        val isGranted = when (item.type) {
                            PermissionSheetType.ACCESSIBILITY -> permissionState.accessibilityGranted
                            PermissionSheetType.USAGE_ACCESS  -> permissionState.usageStatsGranted
                            PermissionSheetType.OVERLAY       -> permissionState.overlayGranted
                        }

                        PermissionPagerCard(
                            item = item,
                            isGranted = isGranted,
                            onClick = {
                                permissionsViewModel.showSheet(item.type)
                            }
                        )
                    }

                    PermissionPagerIndicator(
                        currentPage = pagerState.currentPage,
                        pageCount = missingPermissionItems.size
                    )
                }

                Spacer(Modifier.height(12.dp))
            }

            // ── Main content list ───────────────────────────────────────────────────
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {
                item {
                    SectionTitle(
                        title = "BLOCKING MODE",
                        subtitle = "Select how ReelBreak protects your focus"
                    )
                }

                    item {
                        OverlayToggleRow(
                            isEnabled = dashboardState.isOverlayEnabled,
                            hasPermission = permissionState.overlayGranted,
                            onToggle = {
                                if (!permissionState.overlayGranted) {
                                    permissionsViewModel.showSheet(PermissionSheetType.OVERLAY)
                                } else {
                                    dashboardViewModel.toggleOverlayEnabled()
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }


                // Block Mode Cards
                blockModeOptions.forEach { option ->
                    item {

                        val isOn = when (option.mode) {
                            BlockMode.BLOCK_NOW    -> dashboardState.activeMode == ActiveBlockMode.STRICT
                            BlockMode.LIMIT_BASED  -> dashboardState.activeMode == ActiveBlockMode.LIMIT
                            BlockMode.SMART_FILTER -> dashboardState.activeMode == ActiveBlockMode.SMART
                        }

                        BlockModeCard(
                            option = option,
                            isSelected = dashboardState.expandedMode == option.mode,
                            isExpanded = dashboardState.expandedMode == option.mode,
                            isOn = isOn,
                            onClick = {
                                val hasAccessibility =
                                    permissionState.accessibilityGranted

                                if (!hasAccessibility) {
                                    // Accessibility is mandatory before selecting any mode
                                    permissionsViewModel.showSheet(PermissionSheetType.ACCESSIBILITY)
                                } else {
                                    dashboardViewModel.onBlockModeCardClicked(option.mode)
                                }
                            },
                            detailContent = {
                                when (option.mode) {
                                    BlockMode.BLOCK_NOW   -> StrictDetails()
                                    BlockMode.LIMIT_BASED -> LimitSettingsContent()
                                    BlockMode.SMART_FILTER -> SmartFilterDetails()
                                }
                            }
                        )

                        Spacer(Modifier.height(12.dp))
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
