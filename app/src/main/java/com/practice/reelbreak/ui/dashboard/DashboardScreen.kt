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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessibilityNew
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.practice.reelbreak.domain.model.ActiveBlockMode
import com.practice.reelbreak.ui.component.MainScaffold
import com.practice.reelbreak.ui.permission.PermissionBottomSheet
import com.practice.reelbreak.ui.permission.PermissionSheetType
import com.practice.reelbreak.ui.theme.LocalAppColors
import com.practice.reelbreak.viewmodel.DashboardViewModel
import com.practice.reelbreak.viewmodel.PermissionsViewModel
import androidx.compose.ui.graphics.Color




data class PermissionPagerItem(
    val type: PermissionSheetType,
    val icon: ImageVector,
    val iconTint: Color,
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


    LaunchedEffect(
        dashboardState.isOverlayEnabled,
        permissionState.overlayGranted,
        dashboardState.activeMode
    ) {
        val shouldShowOverlay =
            dashboardState.isOverlayEnabled &&
                    permissionState.overlayGranted &&
                    dashboardState.activeMode == ActiveBlockMode.LIMIT

        android.util.Log.d(
            "OVERLAY_DEBUG",
            "shouldShowOverlay=$shouldShowOverlay enabled=${dashboardState.isOverlayEnabled} " +
                    "granted=${permissionState.overlayGranted} mode=${dashboardState.activeMode}"
        )

    }


    // Base list of all possible permission cards
    val basePermissionPagerItems = listOf(
        PermissionPagerItem(
            type = PermissionSheetType.ACCESSIBILITY,
            icon = Icons.Outlined.AccessibilityNew,
            iconTint = androidx.compose.ui.graphics.Color(0xFF9B3DFF),
            title = "Accessibility Access",
            description = "Required to detect & block reels in real time.",
            buttonText = "Turn On"
        ),
        PermissionPagerItem(
            type = PermissionSheetType.USAGE_ACCESS,
            icon = Icons.Outlined.BarChart,
            iconTint = androidx.compose.ui.graphics.Color(0xFF3B82F6),
            title = "Usage Access",
            description = "Required to track time spent on short-video apps.",
            buttonText = "Grant Access"
        ),
        PermissionPagerItem(
            type = PermissionSheetType.OVERLAY,
            icon = Icons.Outlined.Layers,
            iconTint = androidx.compose.ui.graphics.Color(0xFF2ECC71),
            title = "Overlay Permission",
            description = "Optional — shows a live reel counter over other apps.",
            buttonText = "Enable"
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
                isOverlayGranted = permissionState.overlayGranted,
                isOverlayEnabled = dashboardState.isOverlayEnabled,
                isDarkMode = dashboardState.isDarkMode,
                onVisibilityClick = {
                    if (!permissionState.overlayGranted) {
                        // User wants overlay but no permission yet: open sheet.
                        permissionsViewModel.showSheet(PermissionSheetType.OVERLAY)
                    } else {
                        // Permission already granted: simple toggle.
                        dashboardViewModel.toggleOverlayEnabled()
                    }
                },
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
                           // .height(140.dp)
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

            // ── Main content list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {
                item {
                    SectionTitle(
                        title = "BLOCKING MODE",
                        subtitle = "Select how ReelsBreak protects your focus"
                    )
                }


                // Block Mode Cards
                blockModeOptions.forEach { option ->
                    item {

                        val isOn = when (option.mode) {
                            BlockMode.BLOCK_NOW    -> dashboardState.activeMode == ActiveBlockMode.STRICT &&
                                    permissionState.accessibilityGranted
                            BlockMode.LIMIT_BASED  -> dashboardState.activeMode == ActiveBlockMode.LIMIT
                            BlockMode.SMART_FILTER -> dashboardState.activeMode == ActiveBlockMode.SMART
                        }

//
                        BlockModeCard(
                            option = option,
                            isSelected = dashboardState.activeMode == when (option.mode) {
                                BlockMode.BLOCK_NOW    -> ActiveBlockMode.STRICT
                                BlockMode.LIMIT_BASED  -> ActiveBlockMode.LIMIT
                                BlockMode.SMART_FILTER -> ActiveBlockMode.SMART
                            },
                            isExpanded = dashboardState.expandedMode == option.mode,
                            isOn = isOn,

                            // ── Turns mode On/Off — permission checked ──
                            onClick = {
                                if (!permissionState.accessibilityGranted) {
                                    permissionsViewModel.showSheet(PermissionSheetType.ACCESSIBILITY)
                                } else {
                                    dashboardViewModel.onBlockModeCardClicked(option.mode)
                                }
                            },

                            // ── Expands details — NO permission check at all ──
                            onExpandToggle = {
                                dashboardViewModel.onExpandToggle(option.mode)
                            },

                            detailContent = {
                                when (option.mode) {
                                    BlockMode.BLOCK_NOW   -> StrictDetails()

                                    BlockMode.LIMIT_BASED -> LimitSettingsContent(
                                        dailyTimeLimitMinutes = dashboardState.dailyTimeLimitMinutes,
                                        dailyReelLimit = dashboardState.dailyReelLimit,
                                        onTimeDecrement = {
                                            dashboardViewModel.decrementDailyTimeLimit()
                                        },
                                        onTimeIncrement = {
                                            dashboardViewModel.incrementDailyTimeLimit()
                                        },
                                        onReelDecrement = {
                                            dashboardViewModel.decrementDailyReelLimit()
                                        },
                                        onReelIncrement = {
                                            dashboardViewModel.incrementDailyReelLimit()
                                        }
                                    )

                                    BlockMode.SMART_FILTER -> SmartFilterDetails()
                                }
                            }
                        )

                        Spacer(Modifier.height(12.dp))
                    }
                }
//                        )
//
//                        Spacer(Modifier.height(12.dp))
//                    }
//                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
