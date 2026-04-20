package com.practice.reelbreak.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Weekend
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.practice.reelbreak.ui.permission.PermissionSheetType
import com.practice.reelbreak.ui.theme.LocalAppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
 fun SettingsContent(
    uiState: SettingsState,
    paddingValues: PaddingValues,
    onOpenPermissionSheet: (PermissionSheetType) -> Unit,
    onOpenPermissionSettings: (PermissionSheetType) -> Unit,
    onToggleNotifications: (Boolean) -> Unit,
    onToggleWeekendRelax: (Boolean) -> Unit,
    onTogglePrivacySection: () -> Unit,
    onToggleHelpSection: () -> Unit,
    onShareApp: () -> Unit,
    onRateApp: () -> Unit,
    onSendFeedback: () -> Unit,
    onContactUs: () -> Unit,
    onPrivacyPolicy: () -> Unit,
    onCloseSheet: () -> Unit
) {
    val colors = LocalAppColors.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            // ── Header ────────────────────────────────────────────────────
            item { SettingsHeader() }

            // ── Section: Permissions ──────────────────────────────────────
            item { SectionLabel(icon = Icons.Outlined.Shield, title = "Permissions") }
            item {
                PermissionsSection(
                    uiState = uiState,
                    onInfoClick = onOpenPermissionSheet,
                    onEnableClick = onOpenPermissionSettings
                )
            }
            item { TrustNote() }
            item { Spacer(Modifier.height(20.dp)) }

            // ── Section: Behavior ─────────────────────────────────────────
            item { SectionLabel(icon = Icons.Filled.Settings, title = "Behavior") }
            item {
                SettingsToggleRow(
                    icon = Icons.Filled.Notifications,
                    iconTint = colors.blueAccent,
                    title = "Notifications",
                    subtitle = "Alert when your limit is reached",
                    isEnabled = uiState.isNotificationsEnabled,
                    onToggle = onToggleNotifications
                )
            }
            item { RowDivider() }
            item {
                SettingsToggleRow(
                    icon = Icons.Filled.Weekend,
                    iconTint = colors.purpleSoft,
                    title = "Relax on Weekends",
                    subtitle = "Allow more screen time on Sat & Sun",
                    isEnabled = uiState.isWeekendRelaxEnabled,
                    onToggle = onToggleWeekendRelax
                )
            }
            item { Spacer(Modifier.height(20.dp)) }

            // ── Section: Privacy & Data ───────────────────────────────────
            item { SectionLabel(icon = Icons.Filled.PrivacyTip, title = "Privacy & Data") }
            item {
                CollapsibleSection(
                    isExpanded = uiState.isPrivacySectionExpanded,
                    onToggle = onTogglePrivacySection
                ) {
                    PrivacyDataContent()
                }
            }
            item { Spacer(Modifier.height(20.dp)) }

            // ── Section: Help & Troubleshooting ───────────────────────────
            item { SectionLabel(icon = Icons.Outlined.HelpOutline, title = "Help & Troubleshooting") }
            item {
                CollapsibleSection(
                    isExpanded = uiState.isHelpSectionExpanded,
                    onToggle = onToggleHelpSection
                ) {
                    HelpContent()
                }
            }
            item { Spacer(Modifier.height(20.dp)) }

            // ── Section: About & Support ──────────────────────────────────
            item { SectionLabel(icon = Icons.Filled.Info, title = "About & Support") }
            item {
                AboutSection(
                    appVersion = uiState.appVersion,
                    onShare = onShareApp,
                    onRate = onRateApp,
                    onFeedback = onSendFeedback,
                    onContact = onContactUs,
                    onPrivacyPolicy = onPrivacyPolicy
                )
            }
        }

        val activeSheet = uiState.activePermissionSheet  // ← capture first
        if (activeSheet != null) {
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ModalBottomSheet(
                onDismissRequest = onCloseSheet,
                sheetState = sheetState,
                containerColor = Color(0xFF1A1228),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                PermissionDetailSheet(
                    type = activeSheet as PermissionSheetType,   // ← now Kotlin knows it's PermissionSheetType
                    isGranted = when (activeSheet) {
                        PermissionSheetType.ACCESSIBILITY -> uiState.isAccessibilityGranted
                        PermissionSheetType.USAGE_ACCESS  -> uiState.isUsageAccessGranted
                        PermissionSheetType.OVERLAY       -> uiState.isOverlayGranted
                    },
                    onOpenSettings = {
                        onOpenPermissionSettings(activeSheet)
                        onCloseSheet()
                    },
                    onDismiss = onCloseSheet
                )
            }
        }

    }
}
