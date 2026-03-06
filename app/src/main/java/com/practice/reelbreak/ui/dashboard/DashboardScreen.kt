package com.practice.reelbreak.ui.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.practice.reelbreak.ui.component.GradientColor
import com.practice.reelbreak.ui.component.MainScaffold
import com.practice.reelbreak.ui.navigation.Routes
import com.practice.reelbreak.ui.permission.PermissionNudgeCard
import com.practice.reelbreak.viewmodel.DashboardViewModel
import com.practice.reelbreak.viewmodel.PermissionsViewModel
import kotlinx.coroutines.delay


@Composable
fun DashboardScreen(
    navController: NavController,
    dashboardViewModel: DashboardViewModel = viewModel(),
    permissionsViewModel: PermissionsViewModel = viewModel(),
    selectedTab: Int = 0,
    onTabSelected: (Int) -> Unit = {}
) {
    val dashboardState by dashboardViewModel.uiState.collectAsState()
    val permissionUiState by permissionsViewModel.uiState.collectAsState()

    MainScaffold(
        selectedTab = selectedTab,
        onTabSelected = onTabSelected
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(GradientColor.background)
            .padding(horizontal = 24.dp),
        ) {
            DashboardHeader(
                userName = dashboardState.userName,
                onVisibilityToggle = { dashboardViewModel.toggleCounterVisibility() },
                onThemeToggle = { dashboardViewModel.toggleTheme() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {

//              AnimatedVisibility(
//                  visible = showPermissionNudge && !permissionUiState.isContinueEnabled,
//                  enter = fadeIn(),
//                  exit = fadeOut(),
//                  modifier = Modifier
//                      .align(Alignment.BottomCenter)
//                      .padding(bottom = 100.dp)
//              ) {
//                  PermissionNudgeCard(
//                      onClick = { navController.navigate(Routes.PERMISSION) }
//                  )
//              }


                if (!permissionUiState.isContinueEnabled) {
                    item {
                        PermissionNudgeCard(
                            onClick = { navController.navigate(Routes.PERMISSION) }
                        )
                    }
                }

                item { StatisticsCard(dashboardState) }
                item { ActionGrid() }
            }

        }
    }
}




