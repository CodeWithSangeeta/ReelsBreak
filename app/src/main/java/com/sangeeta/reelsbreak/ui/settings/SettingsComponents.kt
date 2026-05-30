package com.sangeeta.reelsbreak.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.reelsbreak.ui.theme.LocalAppColors


@Composable
 fun SettingsCard(content: @Composable () -> Unit) {
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
 fun SettingsCardTitle(text: String) {
    val colors = LocalAppColors.current
    Text(
        text = text,
        color = colors.textPrimary,
        fontSize = 17.sp,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
 fun Divider() {
    val colors = LocalAppColors.current
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 0.dp),
        thickness = 1.dp,
        color = colors.borderSubtle
    )
}

//@Composable
//fun ToggleRow(
//    icon: ImageVector,
//    iconBg: Color,
//    iconTint: Color,
//    title: String,
//    subtitle: String,
//    isEnabled: Boolean,
//    onToggle: (Boolean) -> Unit
//) {
//    val colors = LocalAppColors.current
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 12.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.spacedBy(12.dp)
//    ) {
//        Box(
//            modifier = Modifier
//                .size(40.dp)
//                .clip(CircleShape)
//                .background(if (colors.isDark) colors.switchTrackOff else iconBg),
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(imageVector = icon, contentDescription = null, tint = if (colors.isDark) colors.purpleSoft else iconTint, modifier = Modifier.size(20.dp))
//        }
//        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
//            Text(text = title, color =colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
//            Text(text = subtitle, color =colors.textSecondary, fontSize = 12.sp)
//        }
//        Switch(
//            checked = isEnabled,
//            onCheckedChange = onToggle,
//            colors = SwitchDefaults.colors(
//                checkedThumbColor = Color.White,
//                checkedTrackColor = colors.switchTrackOn,
//                uncheckedThumbColor = colors.textMuted,
//                uncheckedTrackColor = colors.switchTrackOff,
//                uncheckedBorderColor = colors.borderSubtle
//            )
//        )
//    }
//}



@Composable
fun ToggleRow(
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
                .background(if (colors.isDark) colors.switchTrackOff else iconBg.copy(alpha = 0.25f)), // Safe adaptive light bounce
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (colors.isDark) colors.purpleSoft else iconTint,
                modifier = Modifier.size(20.dp)
            )
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(text = title, color = colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(text = subtitle, color = colors.textSecondary, fontSize = 12.sp, lineHeight = 16.sp)
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
 fun FaqItem(question: String, answer: String) {
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
 fun ActionRow(
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
