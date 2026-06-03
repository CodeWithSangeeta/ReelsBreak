package com.sangeeta.reelsbreak.ui.dashboard.component

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.reelsbreak.ui.dashboard.ReelBreakOverlayCard
import com.sangeeta.reelsbreak.ui.theme.LocalAppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverlayPreviewBottomSheet(
    onDismiss: () -> Unit,
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
                    text = "Overlay Preview",
                    color = colors.textPrimary,
                    fontSize = 24.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "This is how ReelBreak can gently interrupt a reel session.",
                    color = colors.textSecondary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (colors.isDark) {
                                androidx.compose.ui.graphics.Color.White.copy(alpha = 0.03f)
                            } else {
                                androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.025f)
                            },
                            RoundedCornerShape(24.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 22.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ReelBreakOverlayCard(
                        reelsWatched = 10,
                        reelLimit = 20,
                        timeDisplay = "12:30",
                        showReels = true,
                        showTimer = true
                    )
                }
            }
        }
    }
}