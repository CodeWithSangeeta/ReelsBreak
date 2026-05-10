package com.practice.reelbreak.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practice.reelbreak.ui.dashboard.DashboardScreen
import com.practice.reelbreak.ui.focusedmode.FocusScreen
import com.practice.reelbreak.ui.onboarding.OnboardingScreen
import com.practice.reelbreak.ui.settings.SettingsScreen
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.practice.reelbreak.ui.theme.LocalAppColors
import com.practice.reelbreak.viewmodel.DashboardViewModel
import com.practice.reelbreak.viewmodel.MainViewModel
import com.practice.reelbreak.viewmodel.PermissionsViewModel

@Composable
fun AppNavigation(
    startDestination: NavKey,
    mainViewModel: MainViewModel,
    dashboardViewModel: DashboardViewModel = viewModel(),
    permissionsViewModel: PermissionsViewModel = viewModel(),
) {
    val colors = LocalAppColors.current
    val backStack = rememberNavBackStack(startDestination)

    var selectedTab by remember {
        mutableIntStateOf(
            when (startDestination) {
                DashboardKey -> 0
                FocusKey -> 1
                SettingsKey -> 2
                else -> 0
            }
        )
    }

    val onTabSelected: (Int) -> Unit = { index ->
        selectedTab = index

        val targetKey: NavKey = when (index) {
            0 -> DashboardKey
            1 -> FocusKey
            2 -> SettingsKey
            else -> DashboardKey
        }

        if (backStack.lastOrNull() != targetKey) {
            if (
                backStack.lastOrNull() is DashboardKey ||
                backStack.lastOrNull() is FocusKey ||
                backStack.lastOrNull() is SettingsKey
            ) {
                backStack.removeAt(backStack.lastIndex)
            }
            backStack.add(targetKey)
        }

        while (backStack.size > 1 && backStack.last() !is OnboardingKey) {
            backStack.removeAt(backStack.lastIndex)
        }

        if (backStack.lastOrNull() is DashboardKey ||
            backStack.lastOrNull() is FocusKey ||
            backStack.lastOrNull() is SettingsKey
        ) {
            backStack.removeAt(backStack.lastIndex)
        }

        backStack.add(targetKey)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        NavDisplay(
            backStack = backStack,
            onBack = {
                if (backStack.size > 1) {
                    backStack.removeAt(backStack.lastIndex)
                }
            },
            entryProvider = entryProvider {
                entry<OnboardingKey> {
                    OnboardingScreen(
                        onComplete = {
                            mainViewModel.completeOnboarding()
                            backStack.clear()
                            backStack.add(DashboardKey)
                            selectedTab = 0
                        },
                        onSkip = {
                            mainViewModel.completeOnboarding()
                            backStack.clear()
                            backStack.add(DashboardKey)
                            selectedTab = 0
                        }
                    )
                }

                entry<DashboardKey> {
                    DashboardScreen(
                        dashboardViewModel = dashboardViewModel,
                        permissionsViewModel = permissionsViewModel,
                        selectedTab = selectedTab,
                        onTabSelected = onTabSelected
                    )
                }

                entry<FocusKey> {
                    FocusScreen(
                        selectedTab = selectedTab,
                        onTabSelected = onTabSelected
                    )
                }

                entry<SettingsKey> {
                    SettingsScreen(
                        selectedTab = selectedTab,
                        onTabSelected = onTabSelected
                    )
                }
            },
            transitionSpec = {
                fadeIn(animationSpec = tween(220)) togetherWith
                        fadeOut(animationSpec = tween(180))
            },
            popTransitionSpec = {
                fadeIn(animationSpec = tween(220)) togetherWith
                        fadeOut(animationSpec = tween(180))
            },
            predictivePopTransitionSpec = {
                fadeIn(animationSpec = tween(180)) togetherWith
                        fadeOut(animationSpec = tween(140))
            }
        )
    }
}
