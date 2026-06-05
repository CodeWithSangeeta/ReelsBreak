package com.sangeeta.reelsbreak.ui.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.reelsbreak.ui.theme.LocalAppColors

@Composable
fun QuickControlsCard(
    overlayEnabled: Boolean,
    onOverlayToggle: (Boolean) -> Unit,
    onPreviewOverlayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current

    SurfaceCard(
        modifier = modifier.fillMaxWidth(),
        innerPadding = androidx.compose.foundation.layout.PaddingValues(18.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            SectionHeader(
                title = "Quick controls",
                subtitle = "Fast adjustments without opening settings"
            )

            SettingsActionRow(
                title = "Overlay reminder",
                subtitle = "Show an interruption overlay when reels open",
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.NotificationsActive,
                        contentDescription = null,
                        tint = colors.purplePrimary,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingContent = {
                    Switch(
                        checked = overlayEnabled,
                        onCheckedChange = onOverlayToggle,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = colors.switchTrackOn,
                            uncheckedThumbColor = colors.textMuted,
                            uncheckedTrackColor = colors.switchTrackOff,
                            uncheckedBorderColor = colors.borderSubtle
                        )
                    )
                }
            )

            HorizontalDivider(
                color = colors.borderSubtle.copy(alpha = if (colors.isDark) 0.34f else 0.10f),
                thickness = 1.dp
            )

            SettingsActionRow(
                title = "Preview overlay",
                subtitle = "See what the interruption experience looks like",
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.RemoveRedEye,
                        contentDescription = null,
                        tint = colors.blueAccent,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingContent = {
                    Icon(
                        imageVector = Icons.Outlined.ChevronRight,
                        contentDescription = null,
                        tint = colors.textMuted,
                        modifier = Modifier.size(18.dp)
                    )
                },
                onClick = onPreviewOverlayClick
            )
        }
    }
}

@Composable
fun SettingsActionRow(
    title: String,
    subtitle: String,
    icon: @Composable () -> Unit,
    trailingContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val colors = LocalAppColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .then(
                if (onClick != null) {
                    Modifier
                        .clip(RoundedCornerShape(18.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onClick
                        )
                        .background(
                            if (colors.isDark) Color.White.copy(alpha = 0.02f)
                            else Color.Black.copy(alpha = 0.02f)
                        )
                } else Modifier
            )
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(colors.purplePrimary.copy(alpha = 0.10f))
                .border(1.dp, colors.borderSubtle, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                color = colors.textPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                color = colors.textSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                maxLines = 2
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier.width(52.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            trailingContent()
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            color = colors.textPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        if (subtitle != null) {
            Text(
                text = subtitle,
                color = colors.textMuted,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )
        }
    }
}