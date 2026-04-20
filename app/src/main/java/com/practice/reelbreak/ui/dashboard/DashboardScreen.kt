package com.practice.reelbreak.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.navigation.NavController
import com.practice.reelbreak.ui.component.MainScaffold
import com.practice.reelbreak.ui.permission.PermissionBottomSheet
import com.practice.reelbreak.ui.theme.LocalAppColors
import com.practice.reelbreak.viewmodel.DashboardViewModel
import com.practice.reelbreak.viewmodel.PermissionsViewModel

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

    // Check permissions every time Dashboard opens
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
            var selectedMode by remember { mutableStateOf(BlockMode.BLOCK_NOW) }
            DashboardHeader(
                userName = dashboardState.userName,
                onVisibilityToggle = { dashboardViewModel.toggleCounterVisibility() },
                onThemeToggle = { dashboardViewModel.toggleTheme() }
            )
            Spacer(modifier = Modifier.height(8.dp))

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


                    // ── Block Mode Cards ──────────────────────────────────────────────
                    blockModeOptions.forEach { option ->
                       item {
                           BlockModeCard(
                               option = option,
                               isSelected = selectedMode == option.mode,
                               onSelect = { selectedMode = option.mode }
                           )

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                        }

                  //  item {Spacer(modifier = Modifier.height(8.dp))}

            }
        }
    }
}
