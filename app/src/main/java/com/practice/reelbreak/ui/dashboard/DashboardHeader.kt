package com.practice.reelbreak.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.reelbreak.ui.component.GradientColor


//@Composable
//fun DashboardHeader(
//    userName: String,
//    onVisibilityToggle: () -> Unit,
//    onThemeToggle: () -> Unit
//) {
//    Row(
//        modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.Top
//    ) {
//        Column {
//            Text(
//                text = "ReelsBreak",
//                style = MaterialTheme.typography.headlineMedium,
//                fontWeight = FontWeight.Bold,
//                color = Color.White
//            )
//            Spacer(modifier=Modifier.height(2.dp))
//            Text(
//                text = "Stay mindful, $userName.",
//                color = MaterialTheme.colorScheme.primary
//            )
//        }
//
//        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
//            HeaderIconButton(
//                icon = Icons.Outlined.Visibility,
//                onClick = onVisibilityToggle
//            )
//            HeaderIconButton(
//                icon = Icons.Outlined.DarkMode,
//                onClick = onThemeToggle
//            )
//        }
//    }
//}




@Composable
 fun DashboardHeader(isServiceActive: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 52.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "ReelBreak",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Take back your focus",
                color = GradientColor.TextSecondary,
                fontSize = 13.sp
            )
        }

        // Status badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(
                    if (isServiceActive) Color(0x2A2ECC71) else Color(0x22FFFFFF)
                )
                .border(
                    width = 1.dp,
                    color = if (isServiceActive) Color(0x882ECC71) else GradientColor.borderSubtle,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(
                            if (isServiceActive) GradientColor.SuccessGreen
                            else GradientColor.TextMuted
                        )
                )
                Text(
                    text = if (isServiceActive) "Active" else "Inactive",
                    color = if (isServiceActive) GradientColor.SuccessGreen else GradientColor.TextMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
