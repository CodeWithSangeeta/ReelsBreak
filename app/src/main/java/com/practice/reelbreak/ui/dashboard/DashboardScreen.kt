package com.practice.reelbreak.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.practice.reelbreak.ui.component.GradientColor
import com.practice.reelbreak.ui.component.MainScaffold
import com.practice.reelbreak.ui.navigation.Routes
import com.practice.reelbreak.ui.permission.PermissionNudgeCard
import com.practice.reelbreak.viewmodel.DashboardViewModel
import com.practice.reelbreak.viewmodel.PermissionsViewModel


//@Composable
//fun DashboardScreen(
//    navController: NavController,
//    dashboardViewModel: DashboardViewModel = viewModel(),
//    permissionsViewModel: PermissionsViewModel = viewModel(),
//    selectedTab: Int = 0,
//    onTabSelected: (Int) -> Unit = {}
//) {
//    val dashboardState by dashboardViewModel.uiState.collectAsState()
//    val permissionUiState by permissionsViewModel.uiState.collectAsState()
//
//    MainScaffold(
//        selectedTab = selectedTab,
//        onTabSelected = onTabSelected
//    ) { paddingValues ->
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .background(GradientColor.background)
//            .padding(horizontal = 24.dp),
//        ) {
//            DashboardHeader(
//                userName = dashboardState.userName,
//                onVisibilityToggle = { dashboardViewModel.toggleCounterVisibility() },
//                onThemeToggle = { dashboardViewModel.toggleTheme() }
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            LazyColumn(
//                modifier = Modifier
//                    .fillMaxSize(),
//                verticalArrangement = Arrangement.spacedBy(12.dp),
//                contentPadding = PaddingValues(bottom = 120.dp)
//            ) {
//
//                if (!permissionUiState.isContinueEnabled) {
//                    item {
//                        PermissionNudgeCard(
//                            onClick = { navController.navigate(Routes.PERMISSION) }
//                        )
//                    }
//                }
//
//                item { StatisticsCard(dashboardState) }
//                item { ActionGrid(navController=navController) }
//            }
//
//        }
//    }
//}


@Composable
fun DashboardScreen(
    navController: NavHostController,
    dashboardViewModel: DashboardViewModel,
    permissionsViewModel: PermissionsViewModel,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    isServiceActive: Boolean = false,
    onToggleService: () -> Unit = {}
) {
    var selectedMode by remember { mutableStateOf(BlockMode.LIMIT_BASED) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = GradientColor.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp) // space for floating nav
        ) {
            // ── Header ───────────────────────────────────────────────────────
            DashboardHeader(isServiceActive = isServiceActive)

            Spacer(modifier = Modifier.height(8.dp))

            // ── Status Card ───────────────────────────────────────────────────
            ServiceStatusCard(
                isActive = isServiceActive,
                onToggle = onToggleService
            )

            Spacer(modifier = Modifier.height(28.dp))

            // ── Section Title ─────────────────────────────────────────────────
            SectionTitle(
                title = "Choose Your Guard Mode",
                subtitle = "Select how ReelBreak protects your focus"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Block Mode Cards ──────────────────────────────────────────────
            blockModeOptions.forEach { option ->
                BlockModeCard(
                    option = option,
                    isSelected = selectedMode == option.mode,
                    onSelect = { selectedMode = option.mode }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── Quick Stats Row ───────────────────────────────────────────────
            SectionTitle(
                title = "Today's Summary",
                subtitle = "Your focus activity"
            )

            Spacer(modifier = Modifier.height(16.dp))

            QuickStatsRow()

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Floating nav at bottom
    MainScaffold(
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            content = {}
        )
    }
}



