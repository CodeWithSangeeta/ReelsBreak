package com.practice.reelbreak.ui.focused_mode

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.reelbreak.core.registry.SupportedAppsCatalog
import com.practice.reelbreak.ui.theme.LocalAppColors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CurrentlyBlockedSection(blockedPackages: Set<String>) {
    val colors = LocalAppColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        padding  = androidx.compose.foundation.layout.PaddingValues(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "🔒", fontSize = 16.sp)
                Text(
                    text       = "Currently Blocked",
                    color      = colors.purpleSoft,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement   = Arrangement.spacedBy(12.dp)
            ) {
                blockedPackages.forEach { pkg ->
                    val iconRes = SupportedAppsCatalog.iconOf(pkg)
                    val name    = SupportedAppsCatalog.displayNameOf(pkg)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        if (colors.isDark) Color(0xFF2D1B4E)
                                        else colors.purpleSoft.copy(alpha = 0.10f)
                                    )
                                    .border(
                                        1.5.dp,
                                        colors.purplePrimary.copy(alpha = 0.55f),
                                        RoundedCornerShape(14.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (iconRes != null) {
                                    Image(
                                        painter           = painterResource(id = iconRes),
                                        contentDescription = name,
                                        modifier          = Modifier.size(30.dp)
                                    )
                                } else {
                                    Text(
                                        text       = name.first().toString(),
                                        color      = colors.textPrimary,
                                        fontSize   = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            // Block badge
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .align(Alignment.TopEnd)
                                    .clip(CircleShape)
                                    .background(colors.cardSurface)
                                    .border(1.dp, colors.borderActive, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🚫", fontSize = 9.sp)
                            }
                        }
                        Text(
                            text       = name,
                            color      = colors.purpleSoft,
                            fontSize   = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
