package com.sangeeta.reelsbreak.ui.dashboard

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sangeeta.reelsbreak.domain.model.ActiveBlockMode
import com.sangeeta.reelsbreak.domain.model.LimitResetPeriod
import com.sangeeta.reelsbreak.domain.model.ProtectionMode
import com.sangeeta.reelsbreak.ui.dashboard.component.CuriousSetupBottomSheet
import com.sangeeta.reelsbreak.ui.dashboard.component.DashboardHeader
import com.sangeeta.reelsbreak.ui.app_component.MainScaffold
import com.sangeeta.reelsbreak.ui.dashboard.component.ModeInfoBottomSheet
import com.sangeeta.reelsbreak.ui.overlay.OverlayPreviewBottomSheet
import com.sangeeta.reelsbreak.ui.dashboard.component.DashboardHomeSection
import com.sangeeta.reelsbreak.ui.permission.PermissionBottomSheet
import com.sangeeta.reelsbreak.ui.permission.PermissionSheetType
import com.sangeeta.reelsbreak.ui.theme.LocalAppColors
import com.sangeeta.reelsbreak.viewmodel.DashboardViewModel
import com.sangeeta.reelsbreak.viewmodel.PermissionsViewModel
import kotlinx.coroutines.delay

private enum class DashboardHomeSheet {
    MODE_INFO,
    CURIOUS_SETUP,
    OVERLAY_PREVIEW
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    dashboardViewModel: DashboardViewModel = viewModel(),
    permissionsViewModel: PermissionsViewModel = viewModel(),
    selectedTab: Int = 0,
    onTabSelected: (Int) -> Unit
) {
    val dashboardState by dashboardViewModel.uiState.collectAsState()
    val colors = LocalAppColors.current
    val context = LocalContext.current

    val sheetState by permissionsViewModel.sheetState.collectAsState()
    val permModalState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val permissionUiState by permissionsViewModel.uiState.collectAsState()
    val permissionState = permissionUiState.permissionState

    val hasSeenFlowSheet = dashboardState.hasSeenFlowModeInfo
    val hasSeenPauseSheet = dashboardState.hasSeenPauseModeInfo
    val hasSeenCuriousSheet = dashboardState.hasSeenCuriousModeInfo

    var activeHomeSheet by remember { mutableStateOf<DashboardHomeSheet?>(null) }
    var selectedSheetMode by remember { mutableStateOf<HomeProtectionMode?>(null) }

    val basePermissionPagerItems = listOf(
        PermissionPagerItem(
            type = PermissionSheetType.ACCESSIBILITY,
            icon = Icons.Outlined.AccessibilityNew,
            iconTint = colors.purplePrimary,
            title = "Accessibility Access",
            description = "Needed to detect short-video screens and block them when protection is on.",
            buttonText = "Enable Access"
        )
    )

    val missingPermissionItems = basePermissionPagerItems.filter { item ->
        when (item.type) {
            PermissionSheetType.ACCESSIBILITY -> !permissionState.accessibilityGranted
        }
    }

    val homeUiState = DashboardHomeUiState(
        isProtectionEnabled = dashboardState.protectionMode != ProtectionMode.PAUSED,
        selectedMode = when (dashboardState.protectionMode) {
            ProtectionMode.PAUSED -> HomeProtectionMode.PAUSED
            ProtectionMode.FLOW -> HomeProtectionMode.FLOW
            ProtectionMode.CURIOUS -> HomeProtectionMode.CURIOUS
        },
        accessibilityGranted = permissionState.accessibilityGranted,
        overlayEnabled = dashboardState.overlayEnabled,
        curiousCountEnabled = dashboardState.dailyReelLimit > 0,
        curiousTimeEnabled = dashboardState.dailyTimeLimitMinutes > 0,
        curiousReelsLimit = dashboardState.dailyReelLimit.coerceAtLeast(1),
        curiousTimeLimitMinutes = dashboardState.dailyTimeLimitMinutes.coerceAtLeast(5),
        curiousResetPeriod = when (dashboardState.limitResetPeriod) {
            LimitResetPeriod.HOUR -> CuriousResetPeriod.HOUR
            LimitResetPeriod.DAY -> CuriousResetPeriod.DAY
        },
        reelsClosedToday = dashboardState.reelsCount,
        timeBackTodayMinutes = dashboardState.timeSpentMinutes,
        currentStreakDays = dashboardState.currentStreakDays,
        curiousRemainingCount = dashboardState.curiousRemainingCount,
        curiousRemainingMinutes = dashboardState.curiousRemainingMinutes,
        selectedSupportedPackages = dashboardState.selectedApps
    )

    LaunchedEffect(Unit) {
        delay(600L)
        permissionsViewModel.checkAndShowSheetIfNeeded(context)
    }

    if (sheetState.isVisible && sheetState.type != null) {
        PermissionBottomSheet(
            type = sheetState.type!!,
            sheetState = permModalState,
            onDismiss = { permissionsViewModel.dismissSheet() },
            onAgree = { permissionsViewModel.onPermissionSheetAgree(context, sheetState.type!!) },
        )
    }

    if (activeHomeSheet == DashboardHomeSheet.MODE_INFO && selectedSheetMode != null) {
        val mode = selectedSheetMode!!

        ModeInfoBottomSheet(
            mode = mode,
            title = when (mode) {
                HomeProtectionMode.FLOW -> "Flow Mode"
                HomeProtectionMode.PAUSED -> "Pause Mode"
                HomeProtectionMode.CURIOUS -> "Curious Mode"
            },
            description = when (mode) {
                HomeProtectionMode.FLOW ->
                    "ReelBreak exits reels the moment they appear, so you can stay in momentum without making repeated decisions."

                HomeProtectionMode.PAUSED ->
                    "Protection stays configured, but ReelBreak stops interrupting until you turn protection back on."

                HomeProtectionMode.CURIOUS ->
                    "ReelBreak allows intentional viewing, but stops you once your reel or time limit is reached."
            },
            features = when (mode) {
                HomeProtectionMode.FLOW -> listOf(
                    "Closes reels instantly",
                    "Works without limit setup",
                    "Best for strict focus sessions"
                )

                HomeProtectionMode.PAUSED -> listOf(
                    "Stops interruptions temporarily",
                    "Keeps your limits and settings",
                    "Easy to resume anytime"
                )

                HomeProtectionMode.CURIOUS -> listOf(
                    "Supports reel count limits",
                    "Supports time-based limits",
                    "Resets automatically each period"
                )
            },
            buttonText = if (mode == HomeProtectionMode.CURIOUS) "Continue" else "Got It",
            onDismiss = {
                activeHomeSheet = null
                selectedSheetMode = null
            },
            onPrimaryClick = {
                when (mode) {
                    HomeProtectionMode.FLOW -> {
                        dashboardViewModel.markModeInfoSeen(HomeProtectionMode.FLOW)
                        activeHomeSheet = null
                        selectedSheetMode = null
                    }

                    HomeProtectionMode.PAUSED -> {
                        dashboardViewModel.markModeInfoSeen(HomeProtectionMode.PAUSED)
                        activeHomeSheet = null
                        selectedSheetMode = null
                    }

                    HomeProtectionMode.CURIOUS -> {
                        dashboardViewModel.markModeInfoSeen(HomeProtectionMode.CURIOUS)
                        activeHomeSheet = DashboardHomeSheet.CURIOUS_SETUP
                    }
                }
            }
        )
    }
    if (activeHomeSheet == DashboardHomeSheet.CURIOUS_SETUP) {
        CuriousSetupBottomSheet(
            reelsLimit = homeUiState.curiousReelsLimit,
            timeLimitMinutes = homeUiState.curiousTimeLimitMinutes,
            resetPeriod = homeUiState.curiousResetPeriod,
            onReelsLimitChange = { value ->
                if (value > 0) dashboardViewModel.setDailyReelLimit(value)
            },
            onTimeLimitChange = { value ->
                if (value > 0) dashboardViewModel.setDailyTimeLimit(value)
            },
            onResetPeriodChange = { period ->
                dashboardViewModel.setLimitResetPeriod(
                    when (period) {
                        CuriousResetPeriod.HOUR -> LimitResetPeriod.HOUR
                        CuriousResetPeriod.DAY -> LimitResetPeriod.DAY
                    }
                )
            },
            onDismiss = {
                activeHomeSheet = null
                selectedSheetMode = null
            },
            onSaveClick = {
                dashboardViewModel.onModeSelected(HomeProtectionMode.CURIOUS)
                activeHomeSheet = null
                selectedSheetMode = null
            }
        )
    }

    if (activeHomeSheet == DashboardHomeSheet.OVERLAY_PREVIEW) {
        OverlayPreviewBottomSheet(
            onDismiss = {
                activeHomeSheet = null
            }
        )
    }

    MainScaffold(selectedTab = selectedTab, onTabSelected = onTabSelected) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            DashboardHeader(
                isDarkMode = dashboardState.isDarkMode,
                onThemeToggle = { dashboardViewModel.toggleTheme() }
            )

            Spacer(modifier = Modifier.height(14.dp))

            val shouldShowPermissionPager = missingPermissionItems.isNotEmpty()

            if (shouldShowPermissionPager) {
                val pagerState = rememberPagerState(
                    initialPage = 0,
                    pageCount = { missingPermissionItems.size }
                )

                Spacer(Modifier.height(12.dp))

                Column(
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth()
                    ) { page ->
                        val item = missingPermissionItems[page]
                        val isGranted = when (item.type) {
                            PermissionSheetType.ACCESSIBILITY -> permissionState.accessibilityGranted
                        }

                        PermissionPagerCard(
                            item = item,
                            isGranted = isGranted,
                            onClick = {
                                permissionsViewModel.showSheet(item.type)
                            }
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    PermissionPagerIndicator(
                        currentPage = pagerState.currentPage,
                        pageCount = missingPermissionItems.size
                    )
                }

                Spacer(Modifier.height(12.dp))
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {
                item {
                    DashboardHomeSection(
                        state = homeUiState,
                        onProtectionToggle = {
                            if (!permissionState.accessibilityGranted) {
                                permissionsViewModel.showSheet(PermissionSheetType.ACCESSIBILITY)
                                return@DashboardHomeSection
                            }

                            when (dashboardState.activeMode) {
                                ActiveBlockMode.STRICT -> dashboardViewModel.onBlockModeCardClicked(BlockMode.BLOCK_NOW)
                                ActiveBlockMode.LIMIT -> dashboardViewModel.onBlockModeCardClicked(BlockMode.LIMIT_BASED)
                            }
                        },
                        onModeSelected = { mode ->
                            if (!permissionState.accessibilityGranted) {
                                permissionsViewModel.showSheet(PermissionSheetType.ACCESSIBILITY)
                                return@DashboardHomeSection
                            }

                            when (mode) {
                                HomeProtectionMode.FLOW -> {
                                    dashboardViewModel.onModeSelected(HomeProtectionMode.FLOW)
                                    if (!hasSeenFlowSheet) {
                                        selectedSheetMode = HomeProtectionMode.FLOW
                                        activeHomeSheet = DashboardHomeSheet.MODE_INFO
                                    }
                                }

                                HomeProtectionMode.PAUSED -> {
                                    dashboardViewModel.onModeSelected(HomeProtectionMode.PAUSED)
                                    if (!hasSeenPauseSheet) {
                                        selectedSheetMode = HomeProtectionMode.PAUSED
                                        activeHomeSheet = DashboardHomeSheet.MODE_INFO
                                    }
                                }

                                HomeProtectionMode.CURIOUS -> {
                                    val hasCuriousConfig =
                                        dashboardState.dailyReelLimit > 0 || dashboardState.dailyTimeLimitMinutes > 0

                                    selectedSheetMode = HomeProtectionMode.CURIOUS

                                    when {
                                        !hasSeenCuriousSheet -> {
                                            activeHomeSheet = DashboardHomeSheet.MODE_INFO
                                        }

                                        hasCuriousConfig -> {
                                            activeHomeSheet = DashboardHomeSheet.CURIOUS_SETUP
                                        }

                                        else -> {
                                            activeHomeSheet = DashboardHomeSheet.CURIOUS_SETUP
                                        }
                                    }
                                }
                            }
                        },
                        onProtectionInfoClick = {
                            if (!permissionState.accessibilityGranted) {
                                permissionsViewModel.showSheet(PermissionSheetType.ACCESSIBILITY)
                                return@DashboardHomeSection
                            }

                            selectedSheetMode = homeUiState.selectedMode
                            activeHomeSheet = DashboardHomeSheet.MODE_INFO
                        },
                        onOverlayToggle = { enabled ->
                            if (!permissionState.accessibilityGranted) {
                                permissionsViewModel.showSheet(PermissionSheetType.ACCESSIBILITY)
                                return@DashboardHomeSection
                            }
                            dashboardViewModel.onOverlayReminderToggle(enabled)
                        },
                        onPreviewOverlayClick = {
                            activeHomeSheet = DashboardHomeSheet.OVERLAY_PREVIEW
                        },
                        onSupportedAppToggle = { pkg ->
                           dashboardViewModel.toggleAppSelection(pkg)
                        }
                    )
                }
            }
        }
    }

//    LaunchedEffect(activeHomeSheet, selectedSheetMode) {
//        if (activeHomeSheet == DashboardHomeSheet.MODE_INFO && selectedSheetMode != null) {
//            DashboardModeSheetPreferences.markSeen(context, selectedSheetMode!!)
//        }
//    }
}