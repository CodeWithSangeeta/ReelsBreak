//package com.sangeeta.reelsbreak.ui.dashboard.component
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.material3.Switch
//import androidx.compose.material3.SwitchDefaults
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.sangeeta.reelsbreak.ui.theme.LocalAppColors
//
//
//@Composable
//fun OverlayToggleCard(
//    enabled: Boolean,
//    onToggle: (Boolean) -> Unit
//) {
//    val colors = LocalAppColors.current
//
//    SurfaceCard(
//        modifier = Modifier.fillMaxWidth(),
//        innerPadding = PaddingValues(18.dp)
//    ) {
//        Row(
//            verticalAlignment = Alignment.Top,
//            horizontalArrangement = Arrangement.spacedBy(14.dp),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Column(
//                modifier = Modifier.weight(1f),
//                verticalArrangement = Arrangement.spacedBy(5.dp)
//            ) {
//                Text(
//                    text = "Overlay reminder",
//                    color = colors.textPrimary,
//                    fontSize = 17.sp,
//                    fontWeight = FontWeight.Bold
//                )
//                Text(
//                    text = "Shows a small nudge while watching. You can dismiss it anytime.",
//                    color = colors.textSecondary,
//                    fontSize = 13.sp,
//                    lineHeight = 19.sp
//                )
//            }
//
//            Switch(
//                checked = enabled,
//                onCheckedChange = onToggle,
//                colors = SwitchDefaults.colors(
//                    checkedThumbColor = colors.successGreen,
//                    checkedTrackColor = colors.successGreen.copy(alpha = 0.38f),
//                    uncheckedThumbColor = if (colors.isDark) Color.White.copy(alpha = 0.56f) else Color.White,
//                    uncheckedTrackColor = colors.switchTrackOff
//                )
//            )
//        }
//    }
//}



package com.sangeeta.reelsbreak.ui.dashboard.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.reelsbreak.ui.theme.LocalAppColors

@Composable
fun OverlayToggleCard(
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    val colors = LocalAppColors.current

    // Using transparent border formatting to eliminate the generic boxed feel
    SurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        innerPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp),
        useTransparentBorder = true
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = "Overlay HUD Reminder",
                    color = colors.textPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Shows a small real-time heads-up metric nudge while you are watching short videos.",
                    color = colors.textSecondary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }

            Switch(
                checked = enabled,
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
}
