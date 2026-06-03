package com.sangeeta.reelsbreak.ui.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.reelsbreak.ui.dashboard.HomeProtectionMode
import com.sangeeta.reelsbreak.ui.theme.LocalAppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModeInfoBottomSheet(
    mode: HomeProtectionMode,
    title: String,
    description: String,
    features: List<String>,
    buttonText: String,
    onDismiss: () -> Unit,
    onPrimaryClick: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    val colors = LocalAppColors.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .background(colors.sheetDragHandle, RoundedCornerShape(999.dp))
                    .fillMaxWidth(0.14f)
                    .padding(vertical = 2.dp)
            )
        },
        containerColor = androidx.compose.ui.graphics.Color.Transparent
    ) {
        SurfaceCard(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            innerPadding = PaddingValues(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                Text(
                    text = title,
                    color = colors.textPrimary,
                    fontSize = 24.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = description,
                    color = colors.textSecondary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    features.forEach { feature ->
                        FeaturePoint(text = feature)
                    }
                }

                PrimarySheetButton(
                    text = buttonText,
                    onClick = onPrimaryClick
                )
            }
        }
    }
}

@Composable
private fun FeaturePoint(text: String) {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (colors.isDark) androidx.compose.ui.graphics.Color.White.copy(alpha = 0.03f)
                else androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.02f),
                RoundedCornerShape(16.dp)
            )
            .border(
                1.dp,
                colors.borderSubtle.copy(alpha = if (colors.isDark) 0.36f else 0.12f),
                RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Text(
            text = text,
            color = colors.textPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}