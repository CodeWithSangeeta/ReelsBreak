package com.practice.reelbreak.ui.focusedmode

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.practice.reelbreak.R
import com.practice.reelbreak.ui.component.MainScaffold
import com.practice.reelbreak.ui.focused_mode.FocusHeader
import com.practice.reelbreak.ui.focused_mode.StartFocusButton
import com.practice.reelbreak.ui.focused_mode.TimerSelectorRow
import com.practice.reelbreak.ui.theme.LocalAppColors

data class FocusAppChip(
    val name: String,
    val packageName: String,
    val iconRes: Int
)

private val focusApps = listOf(
    FocusAppChip("Instagram", "com.instagram.android", R.drawable.ic_instagram),
    FocusAppChip("YouTube", "com.google.android.youtube", R.drawable.ic_youtube),
    FocusAppChip("Facebook", "com.facebook.katana", R.drawable.ic_facebook),
    FocusAppChip("TikTok", "com.zhiliaoapp.musically", R.drawable.ic_tiktok),
    FocusAppChip("Snapchat", "com.snapchat.android", R.drawable.ic_snapchat),
    FocusAppChip("Twitter", "com.twitter.android", R.drawable.ic_twitter)
)

@Composable
fun FocusScreen(
    viewModel: FocusViewModel = hiltViewModel(),
    selectedTab: Int = 1,
    onTabSelected: (Int) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val colors = LocalAppColors.current

    // Total millis = selectedMinutes * 60_000 (used for progress arc)
    val totalMillis = state.selectedMinutes.toLong() * 60_000L
    // Progress from 1.0 (full) down to 0.0 (done)
    val progress = if (totalMillis > 0 && state.isFocusActive)
        (state.remainingMillis.toFloat() / totalMillis.toFloat()).coerceIn(0f, 1f)
    else 1f

    MainScaffold(
        selectedTab = selectedTab,
        onTabSelected = onTabSelected
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = colors.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(top = 16.dp, bottom = 120.dp)
            ) {

                // ── Header ──────────────────────────────────────────────
                FocusHeader(isFocusActive = state.isFocusActive)

                Spacer(modifier = Modifier.height(28.dp))

                // ── Countdown Circle (always visible; shows time when active) ──
                CountdownTimerCircle(
                    remainingMillis = state.remainingMillis,
                    isActive = state.isFocusActive,
                    progress = progress,
                    onEditClick = { /* allows changing duration when not active */ }
                )

                Spacer(modifier = Modifier.height(28.dp))

                // ── Session Duration (disabled while active) ─────────────
                SectionHeader(
                    icon = {
                        Icon(
                            Icons.Filled.Timer,
                            contentDescription = null,
                            tint = colors.purplePrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    title = "Session Duration"
                )
                Spacer(modifier = Modifier.height(12.dp))
                TimerSelectorRow(
                    selectedTime = state.selectedMinutes.toLong(),
                    onTimeSelected = { minutes: Int ->
                        if (!state.isFocusActive) viewModel.setSelectedMinutes(minutes)
                    }
                )

                Spacer(modifier = Modifier.height(28.dp))

                // ── Block These Apps ─────────────────────────────────────
                SectionHeader(
                    icon = {
                        Icon(
                            Icons.Filled.Shield,
                            contentDescription = null,
                            tint = colors.purplePrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    title = "Block These Apps"
                )
                Spacer(modifier = Modifier.height(12.dp))
                BlockAppsSection(
                    selectedPackages = state.selectedApps,
                  //  isEnabled = !state.isFocusActive,
                    onToggle = { pkg -> viewModel.toggleAppSelection(pkg) }
                )

                Spacer(modifier = Modifier.height(28.dp))

                // ── Quote ────────────────────────────────────────────────
                QuoteCard()

                Spacer(modifier = Modifier.height(28.dp))

                // ── Start / Stop button ──────────────────────────────────
                StartFocusButton(
                    isFocusActive = state.isFocusActive,
                    onToggle = {
                        if (state.isFocusActive) viewModel.stopFocusSession()
                        else viewModel.startFocusSession()
                    }
                )
            }
        }
    }
}

// ── Circular countdown timer ──────────────────────────────────────────────────
@Composable
private fun CountdownTimerCircle(
    remainingMillis: Long,
    isActive: Boolean,
    progress: Float,
    onEditClick: () -> Unit
) {
    // Animate the sweep angle smoothly
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 800),
        label = "timerProgress"
    )

    val totalSeconds = remainingMillis / 1000L
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    val purpleColor = Color(0xFF9333EA)
    val purpleTrack = Color(0xFF2D1B4E)

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            // Circular arc drawn with Canvas
            Canvas(modifier = Modifier.size(200.dp)) {
                val strokeWidth = 14.dp.toPx()
                val arcSize = size.width - strokeWidth

                // Background track circle
                drawArc(
                    color = purpleTrack,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                    size = Size(arcSize, arcSize),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Progress arc
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            Color(0xFF7C3AED),
                            Color(0xFF9333EA),
                            Color(0xFFA855F7),
                            Color(0xFF7C3AED)
                        )
                    ),
                    startAngle = -90f,
                    sweepAngle = 360f * animatedProgress,
                    useCenter = false,
                    topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                    size = Size(arcSize, arcSize),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }

            // Inner content
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (isActive) {
                    // Show HH MM SS countdown
                    if (hours > 0) {
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = String.format("%02d", hours),
                                color = Color.White,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "h ",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = String.format("%02d", minutes),
                                color = Color.White,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "m",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = String.format("%02d", minutes),
                                color = Color.White,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "m ",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = String.format("%02d", seconds),
                                color = Color.White,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "s",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    }

                    Text(
                        text = "Remaining",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 12.sp
                    )

                } else {
                    // Not active – show a ready state
                    Text(
                        text = "Ready",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "to focus",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    // Edit hint
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onEditClick() }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Edit",
                            tint = Color(0xFF9333EA),
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "Edit",
                            color = Color(0xFF9333EA),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

//

@Composable
private fun SectionHeader(
    icon: @Composable () -> Unit,
    title: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        icon()
        Text(
            text = title,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}


@Composable
fun BlockAppsSection(
    selectedPackages: Set<String>,
    isEnabled: Boolean = true,
    onToggle: (String) -> Unit
) {
    val colors = LocalAppColors.current

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        focusApps.chunked(3).forEach { rowApps ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowApps.forEach { app ->
                    val isSelected = selectedPackages.contains(app.packageName)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(90.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (isSelected)
                                    Brush.linearGradient(listOf(Color(0xFF4C1D95), Color(0xFF1E3A8A)))
                                else
                                    Brush.linearGradient(listOf(Color(0xFF1C1233), Color(0xFF140B26)))
                            )
                            .border(
                                1.5.dp,
                                if (isSelected) Color(0xFFB794F4) else Color(0xFF2D1B4E),
                                RoundedCornerShape(16.dp)
                            )
                            .then(
                                if (isEnabled) Modifier.clickable { onToggle(app.packageName) }
                                else Modifier
                            )
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.Black.copy(alpha = 0.25f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = app.iconRes),
                                        contentDescription = app.name,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .align(Alignment.TopEnd)
                                            .clip(CircleShape)
                                            .background(Color(0xFF22C55E)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "✓",
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                            Text(
                                text = app.name,
                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.55f),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                maxLines = 1
                            )
                        }
                    }
                }
                // Fill empty slots
                repeat(3 - rowApps.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}


@Composable
fun QuoteCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1C1233))
            .border(1.dp, Color(0xFF2D1B4E), RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "💡", fontSize = 22.sp)
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "Motivation",
                color = Color(0xFFB794F4),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "\"The secret of getting ahead is getting started.\"",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}