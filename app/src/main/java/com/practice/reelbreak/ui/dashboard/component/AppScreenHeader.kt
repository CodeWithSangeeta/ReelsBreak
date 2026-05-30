package com.practice.reelbreak.ui.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import com.practice.reelbreak.ui.theme.LocalAppColors


@Composable
fun AppScreenHeader(
    title: String,
    subtitle: String? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(132.dp)
            .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
            .background(
                colors.appColor
            )
            .border(
                width = 1.dp,
                brush = if (colors.isDark)
                    Brush.verticalGradient(
                        listOf(colors.purpleSoft, colors.purpleDeep)
                    )
                else
                    Brush.verticalGradient(
                        listOf(colors.purpleSoft.copy(alpha = 0.7f), colors.purplePrimary.copy(alpha = 0.6f))
                    ),
                shape = RoundedCornerShape(30.dp)
            )

            .padding(horizontal = 20.dp)
            .padding(top = 42.dp, bottom = 20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text       = title,
                    fontSize   = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Color.White
                )
                if (actions != null) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        content = actions
                    )
                }
            }

            if (subtitle != null) {
                Text(
                    text       = subtitle,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White.copy(alpha = 0.80f),
                )
            }

        }
    }
}