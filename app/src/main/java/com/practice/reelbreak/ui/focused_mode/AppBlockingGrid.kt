package com.practice.reelbreak.ui.focused_mode

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
data class AppItem(
    val name: String,
    val emoji: String,
    val gradient: Brush
)

val appItems = listOf(
    AppItem("Instagram", "📸", Brush.linearGradient(listOf(Color(0xFFE1306C), Color(0xFFF58529)))),
    AppItem("YouTube", "▶️", Brush.linearGradient(listOf(Color(0xFFFF0000), Color(0xFFCC0000)))),
    AppItem("Facebook", "💬", Brush.linearGradient(listOf(Color(0xFF1877F2), Color(0xFF42A5F5)))),
    AppItem("TikTok", "🎵", Brush.linearGradient(listOf(Color(0xFF010101), Color(0xFF00F5D4)))),
    AppItem("Snapchat", "👻", Brush.linearGradient(listOf(Color(0xFFFFFC00), Color(0xFFFFC300)))),
    AppItem("Twitter", "🐦", Brush.linearGradient(listOf(Color(0xFF1DA1F2), Color(0xFF0D8BD9))))
)

@Composable
 fun AppBlockingGrid(
    blockedApps: Set<String>,
    onToggle: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        appItems.chunked(3).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowItems.forEach { app ->
                    val isBlocked = blockedApps.contains(app.name)
                    AppToggleChip(
                        app = app,
                        isBlocked = isBlocked,
                        onToggle = { onToggle(app.name) },
                        modifier = Modifier.weight(1f)
                    )
                }
                // fill remaining slots if row isn't full
                repeat(3 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
