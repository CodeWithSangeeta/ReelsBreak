//package com.practice.reelbreak.ui.navigation
//
//import androidx.compose.animation.core.FastOutSlowInEasing
//import androidx.compose.animation.core.tween
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.animation.slideInHorizontally
//import androidx.compose.animation.slideOutHorizontally
//import androidx.compose.animation.togetherWith
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableIntStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation3.runtime.NavKey
//import androidx.navigation3.runtime.entryProvider
//import androidx.navigation3.runtime.rememberNavBackStack
//import androidx.navigation3.ui.NavDisplay
//import com.practice.reelbreak.ui.dashboard.DashboardScreen
//import com.practice.reelbreak.ui.focused_mode.FocusScreen
//import com.practice.reelbreak.ui.onboarding.OnboardingScreen
//import com.practice.reelbreak.ui.settings.SettingsScreen
//import com.practice.reelbreak.ui.theme.LocalAppColors
//import com.practice.reelbreak.viewmodel.DashboardViewModel
//import com.practice.reelbreak.viewmodel.MainViewModel
//import com.practice.reelbreak.viewmodel.PermissionsViewModel
//
//@Composable
//fun AppNavigation(
//    startDestination: NavKey,
//    mainViewModel: MainViewModel,
//    dashboardViewModel: DashboardViewModel = viewModel(),
//    permissionsViewModel: PermissionsViewModel = viewModel(),
//) {
//    val colors = LocalAppColors.current
//    val backStack = rememberNavBackStack(startDestination)
//
//    var selectedTab by remember {
//        mutableIntStateOf(
//            when (startDestination) {
//                is DashboardKey -> 0
//                is FocusKey -> 1
//                is SettingsKey -> 2
//                else -> 0
//            }
//        )
//    }
//
//    val onTabSelected: (Int) -> Unit = { index ->
//        if (selectedTab != index) {
//            selectedTab = index
//
//            val targetKey: NavKey = when (index) {
//                0 -> DashboardKey
//                1 -> FocusKey
//                2 -> SettingsKey
//                else -> DashboardKey
//            }
//
//            val isMainTab = { key: NavKey? ->
//                key is DashboardKey || key is FocusKey || key is SettingsKey
//            }
//
//            while (backStack.size > 1 && !isMainTab(backStack.lastOrNull())) {
//                backStack.removeAt(backStack.lastIndex)
//            }
//
//            if (isMainTab(backStack.lastOrNull())) {
//                backStack.removeAt(backStack.lastIndex)
//            }
//
//            backStack.add(targetKey)
//        }
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(colors.background)
//    ) {
//        NavDisplay(
//            backStack = backStack,
//            onBack = {
//                if (backStack.size > 1) {
//                    backStack.removeAt(backStack.lastIndex)
//
//                    selectedTab = when (backStack.lastOrNull()) {
//                        is DashboardKey -> 0
//                        is FocusKey -> 1
//                        is SettingsKey -> 2
//                        else -> selectedTab
//                    }
//                }
//            },
//            entryProvider = entryProvider {
//
//                entry<OnboardingKey> {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .background(colors.background)
//                    ) {
//                        OnboardingScreen(
//                            onComplete = {
//                                mainViewModel.completeOnboarding()
//                                backStack.clear()
//                                backStack.add(DashboardKey)
//                                selectedTab = 0
//                            },
//                            onSkip = {
//                                mainViewModel.completeOnboarding()
//                                backStack.clear()
//                                backStack.add(DashboardKey)
//                                selectedTab = 0
//                            }
//                        )
//                    }
//                }
//
//                entry<DashboardKey> {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .background(colors.background)
//                    ) {
//                        DashboardScreen(
//                            dashboardViewModel = dashboardViewModel,
//                            permissionsViewModel = permissionsViewModel,
//                            selectedTab = selectedTab,
//                            onTabSelected = onTabSelected
//                        )
//                    }
//                }
//
//                entry<FocusKey> {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .background(colors.background)
//                    ) {
//                        FocusScreen(
//                            selectedTab = selectedTab,
//                            onTabSelected = onTabSelected
//                        )
//                    }
//                }
//
//                entry<SettingsKey> {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .background(colors.background)
//                    ) {
//                        SettingsScreen(
//                            selectedTab = selectedTab,
//                            onTabSelected = onTabSelected
//                        )
//                    }
//                }
//            },
//            transitionSpec = {
//                (
//                        slideInHorizontally(
//                            initialOffsetX = { it / 8 },
//                            animationSpec = tween(240, easing = FastOutSlowInEasing)
//                        ) + fadeIn(
//                            animationSpec = tween(220, easing = FastOutSlowInEasing),
//                            initialAlpha = 0.96f
//                        )
//                        ) togetherWith
//                        (
//                                slideOutHorizontally(
//                                    targetOffsetX = { -it / 10 },
//                                    animationSpec = tween(220, easing = FastOutSlowInEasing)
//                                ) + fadeOut(
//                                    animationSpec = tween(180, easing = FastOutSlowInEasing),
//                                    targetAlpha = 0.98f
//                                )
//                                )
//            },
//            popTransitionSpec = {
//                (
//                        slideInHorizontally(
//                            initialOffsetX = { -it / 10 },
//                            animationSpec = tween(220, easing = FastOutSlowInEasing)
//                        ) + fadeIn(
//                            animationSpec = tween(200, easing = FastOutSlowInEasing),
//                            initialAlpha = 0.96f
//                        )
//                        ) togetherWith
//                        (
//                                slideOutHorizontally(
//                                    targetOffsetX = { it / 8 },
//                                    animationSpec = tween(220, easing = FastOutSlowInEasing)
//                                ) + fadeOut(
//                                    animationSpec = tween(180, easing = FastOutSlowInEasing),
//                                    targetAlpha = 0.98f
//                                )
//                                )
//            },
//            predictivePopTransitionSpec = {
//                (
//                        slideInHorizontally(
//                            initialOffsetX = { -it / 12 },
//                            animationSpec = tween(200, easing = FastOutSlowInEasing)
//                        ) + fadeIn(
//                            animationSpec = tween(180, easing = FastOutSlowInEasing),
//                            initialAlpha = 0.97f
//                        )
//                        ) togetherWith
//                        (
//                                slideOutHorizontally(
//                                    targetOffsetX = { it / 8 },
//                                    animationSpec = tween(180, easing = FastOutSlowInEasing)
//                                ) + fadeOut(
//                                    animationSpec = tween(150, easing = FastOutSlowInEasing),
//                                    targetAlpha = 0.98f
//                                )
//                                )
//            }
//        )
//    }
//}


package com.practice.reelbreak.ui.navigation

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.practice.reelbreak.ui.dashboard.DashboardScreen
import com.practice.reelbreak.ui.focused_mode.FocusScreen
import com.practice.reelbreak.ui.onboarding.OnboardingScreen
import com.practice.reelbreak.ui.settings.SettingsScreen
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
    initialTab: Int = 0
) {
    val colors = LocalAppColors.current

    val actualStartDestination = remember(startDestination, initialTab) {
        when (initialTab) {
            1 -> FocusKey
            2 -> SettingsKey
            else -> startDestination
        }
    }

    val backStack = rememberNavBackStack(actualStartDestination)

    var selectedTab by remember(actualStartDestination) {
        mutableIntStateOf(
            when (actualStartDestination) {
                is DashboardKey -> 0
                is FocusKey -> 1
                is SettingsKey -> 2
                else -> 0
            }
        )
    }

    val isMainTab: (NavKey?) -> Boolean = { key ->
        key is DashboardKey || key is FocusKey || key is SettingsKey
    }

    val onTabSelected: (Int) -> Unit = { index ->
        if (selectedTab != index) {
            selectedTab = index

            val targetKey: NavKey = when (index) {
                0 -> DashboardKey
                1 -> FocusKey
                2 -> SettingsKey
                else -> DashboardKey
            }

            while (backStack.size > 1 && !isMainTab(backStack.lastOrNull())) {
                backStack.removeAt(backStack.lastIndex)
            }

            if (backStack.lastOrNull() != targetKey) {
                if (isMainTab(backStack.lastOrNull())) {
                    backStack.removeAt(backStack.lastIndex)
                }
                backStack.add(targetKey)
            }
        }
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

                    selectedTab = when (backStack.lastOrNull()) {
                        is DashboardKey -> 0
                        is FocusKey -> 1
                        is SettingsKey -> 2
                        else -> selectedTab
                    }
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
                fadeIn(
                    animationSpec = tween(160, easing = FastOutSlowInEasing),
                    initialAlpha = 0.99f
                ) togetherWith fadeOut(
                    animationSpec = tween(120, easing = FastOutSlowInEasing),
                    targetAlpha = 0.99f
                )
            },
            popTransitionSpec = {
                fadeIn(
                    animationSpec = tween(160, easing = FastOutSlowInEasing),
                    initialAlpha = 0.99f
                ) togetherWith fadeOut(
                    animationSpec = tween(120, easing = FastOutSlowInEasing),
                    targetAlpha = 0.99f
                )
            },
            predictivePopTransitionSpec = {
                fadeIn(
                    animationSpec = tween(160, easing = FastOutSlowInEasing),
                    initialAlpha = 0.99f
                ) togetherWith fadeOut(
                    animationSpec = tween(120, easing = FastOutSlowInEasing),
                    targetAlpha = 0.99f
                )
            }
        )
    }
}