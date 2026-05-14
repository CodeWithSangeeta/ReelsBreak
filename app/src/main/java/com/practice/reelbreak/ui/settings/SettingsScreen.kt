package com.practice.reelbreak.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practice.reelbreak.ui.component.AppScreenHeader
import com.practice.reelbreak.ui.component.MainScaffold
import com.practice.reelbreak.ui.theme.LocalAppColors

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
               title = "Setting",
               subtitle = "Customize your experience"
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
                // App Settings card
                item {
                    FigmaSettingsCard {
                        Column {
                            FigmaCardTitle(text = "App Settings")
                            Spacer(Modifier.height(4.dp))

                            FigmaToggleRow(
                                icon = Icons.Outlined.Notifications,
                                iconBg = colors.purplePrimary.copy(alpha = 0.12f),
                                iconTint = colors.purplePrimary,
                                title = "Notifications",
                                subtitle = "Get reminders and limit alerts",
                                isEnabled = uiState.isNotificationsEnabled,
                                onToggle = viewModel::toggleNotifications
                            )
                            FigmaDivider()
                            FigmaToggleRow(
                                icon = Icons.Outlined.CalendarToday,
                                iconBg = colors.purplePrimary.copy(alpha = 0.12f),
                                iconTint = colors.purplePrimary,
                                title = "Weekend Relax",
                                subtitle = "Disable limits on Saturdays and Sundays",
                                isEnabled = uiState.isWeekendRelaxEnabled,
                                onToggle = viewModel::toggleWeekendRelax
                            )
                        }
                    }
                }

                // Frequently Asked Questions card
                item {
                    FigmaSettingsCard {
                        Column {
                            FigmaCardTitle(text = "Frequently Asked Questions")
                            Spacer(Modifier.height(8.dp))
                            FigmaFaqItem(
                                question = "How does ReelBreak detect short-form videos?",
                                answer = "ReelBreak uses Android's Accessibility Service to detect when you open a reels or shorts screen inside Instagram, YouTube, TikTok, Snapchat, or Facebook."
                            )
                            FigmaDivider()
                            FigmaFaqItem(
                                question = "What permissions does ReelBreak need?",
                                answer = "Accessibility Service (required), Usage Access (required for time tracking), and Display Over Apps (optional for overlay bubble)."
                            )
                            FigmaDivider()
                            FigmaFaqItem(
                                question = "Can I customize which apps are blocked?",
                                answer = "Yes! In Focus Mode you can choose exactly which apps to block during a session."
                            )
                            FigmaDivider()
                            FigmaFaqItem(
                                question = "Is my data private?",
                                answer = "All data stays on your device. ReelBreak never sends anything to any server. It only reads which app is on screen — never content, messages, or personal data."
                            )
                        }
                    }
                }

                // Support & Info card
                item {
                    FigmaSettingsCard {
                        Column {
                            FigmaCardTitle(text = "Support & Info")
                            Spacer(Modifier.height(4.dp))

                            FigmaActionRow(
                                icon = Icons.Outlined.ChatBubbleOutline,
                                iconBg  =colors.purplePrimary.copy(alpha = if (colors.isDark) 0.22f else 0.12f),
                                iconTint = colors.purplePrimary,
                                title = "Send Feedback",
                                onClick = viewModel::sendFeedback
                            )
                            FigmaDivider()
                            FigmaActionRow(
                                icon = Icons.Outlined.StarOutline,
                                iconBg  = colors.warningOrange.copy(alpha = if (colors.isDark) 0.22f else 0.12f),

                                iconTint = colors.warningOrange,
                                title = "Rate ReelBreak",
                                onClick = viewModel::rateApp
                            )
                            FigmaDivider()
                            FigmaActionRow(
                                icon = Icons.Outlined.Share,
                                iconBg  = colors.blueAccent.copy(alpha = if (colors.isDark) 0.22f else 0.12f),
                                iconTint = colors.blueAccent,
                                title = "Share with Friends",
                                onClick = viewModel::shareApp
                            )
                            FigmaDivider()
                            FigmaActionRow(
                                icon = Icons.Outlined.Shield,
                                iconBg  = colors.successGreen.copy(alpha = if (colors.isDark) 0.22f else 0.12f),
                                iconTint = colors.successGreen,
                                title = "Privacy Policy",
                                onClick = viewModel::openPrivacyPolicy
                            )
                        }
                    }
                }

                // Version footer
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
                                text = "Built with",
                                color = colors.textMuted,
                                fontSize = 12.sp
                            )
                            Text(text = "❤️", fontSize = 12.sp)
                            Text(
                                text = "for mindful scrolling",
                                color = colors.textMuted ,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}



@Composable
private fun FigmaSettingsCard(content: @Composable () -> Unit) {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colors.cardSurface)
            .border(
                1.dp,
                 colors.borderSubtle,
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
private fun FigmaCardTitle(text: String) {
    val colors = LocalAppColors.current
    Text(
        text = text,
        color = colors.textPrimary,
        fontSize = 17.sp,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun FigmaDivider() {
    val colors = LocalAppColors.current
    Divider(
        modifier = Modifier.padding(vertical = 0.dp),
        color = colors.borderSubtle,
        thickness = 1.dp
    )
}

@Composable
private fun FigmaToggleRow(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    val colors = LocalAppColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (colors.isDark) Color(0xFF2A1F40) else iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = if (colors.isDark) colors.purpleSoft else iconTint, modifier = Modifier.size(20.dp))
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(text = title, color =colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(text = subtitle, color =colors.textSecondary, fontSize = 12.sp)
        }
        Switch(
            checked = isEnabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = colors.switchTrackOn,
                uncheckedThumbColor = colors.textMuted,
                uncheckedTrackColor = colors.switchTrackOff,
                uncheckedBorderColor = colors.borderSubtle
            )
        )
    }
}

@Composable
private fun FigmaFaqItem(question: String, answer: String) {
    val colors = LocalAppColors.current
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { expanded = !expanded }
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = question,
                color = colors.textPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f).padding(end = 8.dp),
                lineHeight = 20.sp
            )
            Icon(
                imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                tint = colors.textMuted,
                modifier = Modifier.size(20.dp)
            )
        }
        if (expanded) {
            Text(
                text = answer,
                color = colors.textSecondary,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}

@Composable
private fun FigmaActionRow(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    onClick: () -> Unit
) {
    val colors = LocalAppColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background( iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
        }
        Text(
            text = title,
            color = colors.textPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = colors.textMuted,
            modifier = Modifier.size(18.dp)
        )
    }
}


