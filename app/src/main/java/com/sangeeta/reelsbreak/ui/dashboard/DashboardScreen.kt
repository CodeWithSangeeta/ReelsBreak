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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sangeeta.reelsbreak.domain.model.ActiveBlockMode
import com.sangeeta.reelsbreak.ui.dashboard.component.MainScaffold
import com.sangeeta.reelsbreak.ui.permission.PermissionBottomSheet
import com.sangeeta.reelsbreak.ui.permission.PermissionSheetType
import com.sangeeta.reelsbreak.ui.theme.LocalAppColors
import com.sangeeta.reelsbreak.viewmodel.DashboardViewModel
import com.sangeeta.reelsbreak.viewmodel.PermissionsViewModel
import com.sangeeta.reelsbreak.domain.model.LimitResetPeriod
import com.sangeeta.reelsbreak.domain.model.ProtectionMode
import com.sangeeta.reelsbreak.ui.dashboard.component.DashboardHeader
import com.sangeeta.reelsbreak.ui.dashboard.component.ReelBreakHomeSection
import kotlinx.coroutines.delay

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
        isProtectionEnabled = permissionState.accessibilityGranted,
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
        curiousRemainingCount = dashboardState.curiousRemainingCount,
        curiousRemainingMinutes = dashboardState.curiousRemainingMinutes
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
                    ReelBreakHomeSection(
                        state = homeUiState,
                        onProtectionToggle = {
                            if (!permissionState.accessibilityGranted) {
                                permissionsViewModel.showSheet(PermissionSheetType.ACCESSIBILITY)
                                return@ReelBreakHomeSection
                            }

                            when (dashboardState.activeMode) {
                                ActiveBlockMode.STRICT -> dashboardViewModel.onBlockModeCardClicked(
                                    BlockMode.BLOCK_NOW
                                )
                                ActiveBlockMode.LIMIT -> dashboardViewModel.onBlockModeCardClicked(
                                    BlockMode.LIMIT_BASED
                                )
                            }
                        },
                        onModeSelected = { mode ->
                            if (!permissionState.accessibilityGranted) {
                                permissionsViewModel.showSheet(PermissionSheetType.ACCESSIBILITY)
                                return@ReelBreakHomeSection
                            }
                            when (mode) {
                                HomeProtectionMode.FLOW -> dashboardViewModel.onModeSelected(
                                    HomeProtectionMode.FLOW
                                )
                                HomeProtectionMode.CURIOUS -> dashboardViewModel.onModeSelected(
                                    HomeProtectionMode.CURIOUS
                                )
                                HomeProtectionMode.PAUSED -> dashboardViewModel.onModeSelected(
                                    HomeProtectionMode.PAUSED
                                )
                            }
                        },
                        onOverlayToggle = { enabled ->
                            if (!permissionState.accessibilityGranted) {
                                permissionsViewModel.showSheet(PermissionSheetType.ACCESSIBILITY)
                                return@ReelBreakHomeSection
                            }
                            dashboardViewModel.onOverlayReminderToggle(enabled)
                        },
                        onCuriousCountToggle = { enabled ->
                            dashboardViewModel.setCuriousCountEnabled(enabled)
                        },
                        onCuriousTimeToggle = { enabled ->
                            dashboardViewModel.setCuriousTimeEnabled(enabled)
                        },
                        onCuriousReelsLimitChange = { value ->
                            if (value > 0) dashboardViewModel.setDailyReelLimit(value)
                        },
                        onCuriousTimeLimitChange = { value ->
                            if (value > 0) dashboardViewModel.setDailyTimeLimit(value)
                        },
                        onCuriousPeriodChange = { period ->
                            dashboardViewModel.setLimitResetPeriod(
                                when (period) {
                                    CuriousResetPeriod.HOUR -> LimitResetPeriod.HOUR
                                    CuriousResetPeriod.DAY -> LimitResetPeriod.DAY
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}