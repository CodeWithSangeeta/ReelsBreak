package com.practice.reelbreak.ui.focusedmode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.practice.reelbreak.ui.component.MainScaffold
import com.practice.reelbreak.ui.focusedmode.FocusViewModel

//@Composable
//fun FocusScreen(
//    navController: NavController,
//    focusViewModel: FocusViewModel = viewModel(),
//    selectedTab: Int = 2,
//    onTabSelected: (Int) -> Unit = {}
//) {
//    MainScaffold(
//        selectedTab = selectedTab,
//        onTabSelected = onTabSelected
//    ) { paddingValues ->
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                   // .padding(paddingValues)
//                    .background(
//                        brush = Brush.verticalGradient(
//                            0f to Color(0xFF1A0033),
//                            1f to Color(0xFF2D1B69)
//                        )
//                    ),
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 32.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(
//                            Icons.Outlined.ArrowBack,
//                            contentDescription = "Back",
//                            tint = Color.White
//                        )
//                    }
//                    Spacer(modifier = Modifier.weight(1f))
//                }
//
//                Icon(
//                    Icons.Outlined.Shield,
//                    contentDescription = "Focus Mode",
//                    modifier = Modifier.size(80.dp),
//                    tint = Color(0xFF8B5CF6)
//                )
//
//                Text(
//                    text = "Focus Mode",
//                    color = Color.White,
//                    fontSize = 36.sp,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.padding(vertical = 16.dp)
//                )
//
//                Text(
//                    text = "Block distractions and boost productivity",
//                    color = Color.White.copy(alpha = 0.8f),
//                    fontSize = 18.sp
//                )
//
//                Text(
//                    text = "Focus mode features coming soon...",
//                    color = Color.White.copy(alpha = 0.6f),
//                    fontSize = 16.sp,
//                    modifier = Modifier.padding(top = 48.dp)
//                )
//            }
//        }
//    }




import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practice.reelbreak.ui.component.MainScaffold
import com.practice.reelbreak.ui.focused_mode.ActiveFocusBanner
import com.practice.reelbreak.ui.focused_mode.AppBlockingGrid
import com.practice.reelbreak.ui.focused_mode.FocusHeader
import com.practice.reelbreak.ui.focused_mode.QuoteSection
import com.practice.reelbreak.ui.focused_mode.SectionLabel
import com.practice.reelbreak.ui.focused_mode.StartFocusButton
import com.practice.reelbreak.ui.focused_mode.TimerSelectorRow
import com.practice.reelbreak.ui.theme.LocalAppColors

@Composable
fun FocusScreen(
    navController: NavController,
    focusViewModel: FocusViewModel = viewModel(),
    selectedTab: Int = 1,
    onTabSelected: (Int) -> Unit
) {
    val blockedApps by focusViewModel.blockedApps.collectAsState()
    val isFocusActive by focusViewModel.isFocusActive.collectAsState()
    val selectedTime by focusViewModel.selectedTime.collectAsState()

    MainScaffold(
        selectedTab = selectedTab,
        onTabSelected = onTabSelected
    ) { paddingValues ->
        val colors = LocalAppColors.current
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = colors.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 100.dp)
            ) {
                // ── Header ───────────────────────────────────────────────
                FocusHeader(isFocusActive = isFocusActive)

                Spacer(modifier = Modifier.height(8.dp))

                // ── Active Focus Banner ───────────────────────────────────
                if (isFocusActive) {
                    ActiveFocusBanner(onStop = { focusViewModel.stopFocus() })
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // ── Timer Section ─────────────────────────────────────────
                SectionLabel(icon = Icons.Filled.Timer, title = "Session Duration")
                Spacer(modifier = Modifier.height(12.dp))
                TimerSelectorRow(
                    selectedTime = selectedTime,
                    onTimeSelected = { focusViewModel.selectTime(it) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ── App Blocking Section ──────────────────────────────────
                SectionLabel(icon = Icons.Filled.Shield, title = "Block These Apps")
                Spacer(modifier = Modifier.height(12.dp))
                AppBlockingGrid(
                    blockedApps = blockedApps,
                    onToggle = { focusViewModel.toggleApp(it) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ── Motivational Quote ────────────────────────────────────
                QuoteSection()

                Spacer(modifier = Modifier.height(24.dp))

                // ── Start Button ──────────────────────────────────────────
                StartFocusButton(
                    isFocusActive = isFocusActive,
                    onToggle = {
                        if (isFocusActive) focusViewModel.stopFocus()
                        else focusViewModel.startFocus()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
