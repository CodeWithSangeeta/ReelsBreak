package com.practice.reelbreak.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.reelbreak.ui.theme.LocalAppColors

@Composable
fun OverlayToggleRow(
    isEnabled: Boolean,
    hasPermission: Boolean,
    onToggle: () -> Unit
) {
    val colors = LocalAppColors.current

    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            androidx.compose.material3.Text(
                text = "Overlay bubble",
                color = androidx.compose.ui.graphics.Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            androidx.compose.material3.Text(
                text = if (hasPermission)
                    "Show reels & time over other apps"
                else
                    "Grant overlay permission to enable",
                color = colors.textSecondary,
                fontSize = 12.sp
            )
        }

        androidx.compose.material3.Switch(
            checked = isEnabled && hasPermission,
            onCheckedChange = { onToggle() }
        )
    }
}
