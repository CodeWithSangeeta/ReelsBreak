package com.practice.reelbreak.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practice.reelbreak.ui.component.AppScreenHeader
import com.practice.reelbreak.ui.component.MainScaffold
import com.practice.reelbreak.ui.theme.LocalAppColors
import com.practice.reelbreak.viewmodel.SettingsViewModel

data class SettingsState(
    val isNotificationsEnabled: Boolean = false,
    val isWeekendRelaxEnabled: Boolean = false,
    val isPrivacySectionExpanded: Boolean = false,
    val isHelpSectionExpanded: Boolean = false,
    val appVersion: String = "1.0.0"
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    selectedTab: Int = 2,
    onTabSelected: (Int) -> Unit,
    viewModel: SettingsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val colors = LocalAppColors.current

    MainScaffold(selectedTab = selectedTab, onTabSelected = onTabSelected) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize()) {
           AppScreenHeader(
               title = "Settings",
               subtitle = "Manage preferences, reminders, and privacy",
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
                           imageVector = Icons.Filled.Settings,
                           contentDescription = null,
                           tint = Color.White,
                           modifier = Modifier.size(20.dp)
                       )
                   }
               },
           )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(
                    start = 16.dp, end = 16.dp,
                    top = 16.dp, bottom = 120.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    SettingsCard {
                        Column {
                            SettingsCardTitle(text = "Preferences")
                            Spacer(Modifier.height(4.dp))

                            ToggleRow(
                                icon = Icons.Outlined.Notifications,
                                iconBg = colors.purplePrimary.copy(alpha = 0.12f),
                                iconTint = colors.purplePrimary,
                                title = "Reminders and alerts",
                                subtitle = "Receive reminders, session updates, and limit alerts",
                                isEnabled = uiState.isNotificationsEnabled,
                                onToggle = viewModel::toggleNotifications
                            )
                            Divider()
                            ToggleRow(
                                icon = Icons.Outlined.CalendarToday,
                                iconBg = colors.purplePrimary.copy(alpha = 0.12f),
                                iconTint = colors.purplePrimary,
                                title = "Weekend  pause",
                                subtitle = "Pause limits on Saturdays and Sundays",
                                isEnabled = uiState.isWeekendRelaxEnabled,
                                onToggle = viewModel::toggleWeekendRelax
                            )
                        }
                    }
                }

                item {
                    SettingsCard {
                        Column {
                            SettingsCardTitle(text = "Help & FAQs")
                            Spacer(Modifier.height(8.dp))
                            FaqItem(
                                question = "How does ReelBreak detect short-form videos?",
                                answer = "ReelBreak uses Android Accessibility access to recognize when supported short-video screens open in apps like Instagram, YouTube, TikTok, Snapchat, and Facebook so it can apply the protection mode you choose."                            )
                            Divider()
                            FaqItem(
                                question = "What permissions does ReelBreak need?",
                                answer = "ReelBreak may ask for Accessibility access to detect supported short-video screens, Usage Access to measure time spent, and Display Over Other Apps for the optional floating counter."                            )
                            Divider()
                            FaqItem(
                                question = "Can I customize which apps are blocked?",
                                answer = "Yes. In Focus Mode, you can select which supported apps should be blocked during your session."
                            )

                            Divider()
                            FaqItem(
                                question = "Why does ReelBreak need Accessibility access?",
                                        answer = "ReelBreak uses Accessibility access only to detect when supported short-video screens are shown so it can apply your chosen protection mode. It does not read or store your text, messages, or passwords."
                            )

                            Divider()
                            FaqItem(
                                question = "What happens if I turn permissions off?",
                                answer = "If you turn off Accessibility or Usage Access, ReelBreak may not be able to block reels or track time correctly. You can always reopen the app later to turn permissions back on."
                            )

                            Divider()
                            FaqItem(
                                question = "Does ReelBreak work offline?",
                                answer = "Yes. ReelBreak does not require an internet connection to block reels or track your usage time. Some support links, like feedback or rating, may open online apps."
                            )

                            Divider()
                            FaqItem(
                                question = "Will ReelBreak affect my battery or performance?",
                                answer = "ReelBreak is designed to be lightweight. It only runs checks when you open supported apps or when a focus session is active, and it does not do heavy work in the background."
                            )

                            Divider()
                            FaqItem(
                                question = "Can I pause protection temporarily?",
                                answer = "Yes. You can stop an active focus session at any time from the Focus Mode screen. You can also switch to a lighter protection mode, like limits instead of instant blocking."
                            )

                            Divider()
                            FaqItem(
                                question = "How can I contact support or suggest features?",
                                answer = "Use the Send feedback option in Settings to email us or share your thoughts. We read every message and use it to improve ReelBreak."
                            )

                            Divider()
                            FaqItem(
                                question = "How is my data handled?",
                                answer = "ReelBreak stores your usage and settings on your device. If we ever add analytics or remote services, we will update our privacy policy and let you review the changes inside the app."
                            )


                        }
                    }
                }

                item {
                    SettingsCard {
                        Column {
                            SettingsCardTitle(text = "Support and Legal")
                            Spacer(Modifier.height(4.dp))

                            ActionRow(
                                icon = Icons.Outlined.ChatBubbleOutline,
                                iconBg  =colors.purplePrimary.copy(alpha = if (colors.isDark) 0.22f else 0.12f),
                                iconTint = colors.purplePrimary,
                                title = "Send feedback",
                                onClick = viewModel::sendFeedback
                            )
                            Divider()
                            ActionRow(
                                icon = Icons.Outlined.StarOutline,
                                iconBg  = colors.warningOrange.copy(alpha = if (colors.isDark) 0.22f else 0.12f),

                                iconTint = colors.warningOrange,
                                title = "Rate the app",
                                onClick = viewModel::rateApp
                            )
                            Divider()
                            ActionRow(
                                icon = Icons.Outlined.Share,
                                iconBg  = colors.blueAccent.copy(alpha = if (colors.isDark) 0.22f else 0.12f),
                                iconTint = colors.blueAccent,
                                title = "Share ReelBreak",
                                onClick = viewModel::shareApp
                            )
                            Divider()
                            ActionRow(
                                icon = Icons.Outlined.Shield,
                                iconBg  = colors.successGreen.copy(alpha = if (colors.isDark) 0.22f else 0.12f),
                                iconTint = colors.successGreen,
                                title = "Privacy policy",
                                onClick = viewModel::openPrivacyPolicy
                            )
                        }
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "ReelBreak v${uiState.appVersion}",
                            color = colors.textMuted,
                            fontSize = 12.sp
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Built to support mindful scrolling",
                                color = colors.textMuted,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}