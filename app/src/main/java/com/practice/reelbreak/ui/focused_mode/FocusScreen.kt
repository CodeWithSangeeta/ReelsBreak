package com.practice.reelbreak.ui.focused_mode

import android.content.ComponentName
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.practice.reelbreak.core.accessibility.ReelsAccessibilityService
import com.practice.reelbreak.ui.dashboard.component.AppScreenHeader
import com.practice.reelbreak.ui.dashboard.component.MainScaffold
import com.practice.reelbreak.ui.permission.PermissionBottomSheet
import com.practice.reelbreak.ui.permission.PermissionSheetType
import com.practice.reelbreak.ui.theme.LocalAppColors
import com.practice.reelbreak.viewmodel.FocusViewModel
import com.practice.reelbreak.viewmodel.PermissionsViewModel
import kotlinx.coroutines.delay

fun isAccessibilityServiceEnabled(context: android.content.Context): Boolean {
    val expected = ComponentName(context, ReelsAccessibilityService::class.java)
    val enabled  = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
    ) ?: return false
    return enabled.split(":").any { ComponentName.unflattenFromString(it) == expected }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusScreen(
    viewModel: FocusViewModel = hiltViewModel(),
    permissionsViewModel: PermissionsViewModel = hiltViewModel(),
    selectedTab: Int = 1,
    onTabSelected: (Int) -> Unit
) {

    val state  by viewModel.uiState.collectAsStateWithLifecycle()
    val colors  = LocalAppColors.current
    val context  = LocalContext.current
    val sheetState by permissionsViewModel.sheetState.collectAsStateWithLifecycle()
    val permModalState       = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val lifecycleOwner       = LocalLifecycleOwner.current
    val currentIsFocusActive by rememberUpdatedState(state.isFocusActive)
    val chipSelectedMinutes =
        if (currentIsFocusActive) state.sessionDurationMinutes else state.selectedMinutes

    // Re-check accessibility on every resume
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val nowGranted = isAccessibilityServiceEnabled(context)
                if (nowGranted) {
                    permissionsViewModel.updateAccessibilityGranted(true)
                    permissionsViewModel.dismissSheet()
                } else {
                    permissionsViewModel.updateAccessibilityGranted(false)

                    if (currentIsFocusActive) {
                        viewModel.stopFocusSession()
                    }
                    permissionsViewModel.checkAndShowSheetIfNeeded(context)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(Unit) {
        delay(600L)
        permissionsViewModel.checkAndShowSheetIfNeeded(context) }

    if (sheetState.isVisible && sheetState.type != null) {
        PermissionBottomSheet(
            type      = sheetState.type!!,
            sheetState = permModalState,
            onDismiss = { permissionsViewModel.dismissSheet() },
            onAgree   = { permissionsViewModel.onPermissionSheetAgree(context, sheetState.type!!) }
        )
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { msg ->
            android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_SHORT).show()
            viewModel.dismissError()
        }
    }

    val totalMillis =
        if (currentIsFocusActive) state.sessionDurationMinutes.toLong() * 60_000L
        else state.selectedMinutes.toLong() * 60_000L
    val progress    = if (totalMillis > 0 && currentIsFocusActive )
        (state.remainingMillis.toFloat() / totalMillis.toFloat()).coerceIn(0f, 1f)
    else 1f

    MainScaffold(selectedTab = selectedTab, onTabSelected = onTabSelected) { paddingValues ->
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                AppScreenHeader(
                    title    = "Focus Mode",
                    subtitle = "Block distractions and stay on track",
                    actions  = {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background( Color.White.copy(alpha = 0.10f))
                                .border(
                                    1.dp,
                                    Color.White.copy(alpha = 0.25f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Shield,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = colors.background)
        ) {

                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    TimerCard(
                        remainingMillis = state.remainingMillis,
//                        selectedMinutes = chipSelectedMinutes.toLong(),
                        isActive        = currentIsFocusActive ,
                        progress        = progress,
                        isFocusActive   = currentIsFocusActive,
                        onToggle        = {
                            val granted = isAccessibilityServiceEnabled(context)
                            when {
                                !granted            -> permissionsViewModel.showSheet(PermissionSheetType.ACCESSIBILITY)
                                currentIsFocusActive -> viewModel.stopFocusSession()
                                else                -> viewModel.startFocusSession()
                            }
                        }
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentPadding = PaddingValues(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        item {
                            DurationCard(
                                selectedMinutes = chipSelectedMinutes,
                                enabled = true,
                                onSelect = { viewModel.setSelectedMinutes(it) }
                            )
                        }

                        item {
                            SupportedAppsCard(
                                selectedPackages = state.selectedApps,
                                isEnabled = true,
                                onToggle = { pkg -> viewModel.toggleAppSelection(pkg) }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }

                }
           }
        }
    }
}