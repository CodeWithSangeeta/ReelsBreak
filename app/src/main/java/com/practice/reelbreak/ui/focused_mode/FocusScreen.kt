package com.practice.reelbreak.ui.focused_mode

import android.content.ComponentName
import android.provider.Settings
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.practice.reelbreak.R
import com.practice.reelbreak.core.accessibility.ReelsAccessibilityService
import com.practice.reelbreak.data.FocusStateHolder.isFocusActive
import com.practice.reelbreak.ui.component.AppScreenHeader
import com.practice.reelbreak.ui.component.MainScaffold
import com.practice.reelbreak.ui.focusedmode.FocusViewModel
import com.practice.reelbreak.ui.permission.PermissionBottomSheet
import com.practice.reelbreak.ui.permission.PermissionSheetType
import com.practice.reelbreak.ui.theme.LocalAppColors
import com.practice.reelbreak.viewmodel.PermissionsViewModel

data class FocusAppChip(
    val name: String,
    val packageName: String,
    val iconRes: Int
)

private val focusApps = listOf(
    FocusAppChip("Instagram",  "com.instagram.android",      R.drawable.ic_instagram),
    FocusAppChip("YouTube",    "com.google.android.youtube", R.drawable.ic_youtube),
    FocusAppChip("TikTok",     "com.zhiliaoapp.musically",   R.drawable.ic_tiktok),
    FocusAppChip("Facebook",   "com.facebook.katana",        R.drawable.ic_facebook),
    FocusAppChip("Snapchat",   "com.snapchat.android",       R.drawable.ic_snapchat),
    FocusAppChip("Twitter",    "com.twitter.android",        R.drawable.ic_twitter)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusScreen(
    viewModel: FocusViewModel = hiltViewModel(),
    permissionsViewModel: PermissionsViewModel = hiltViewModel(),
    selectedTab: Int = 1,
    onTabSelected: (Int) -> Unit
) {
    val state        by viewModel.uiState.collectAsState()
    val colors       = LocalAppColors.current
    val context      = LocalContext.current
    val sheetState           by permissionsViewModel.sheetState.collectAsState()
    val permModalState       = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val lifecycleOwner       = LocalLifecycleOwner.current

    // Re-check accessibility on every resume
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val nowGranted = isAccessibilityServiceEnabled(context)
                if (nowGranted) {
                    permissionsViewModel.updateAccessibilityGranted(true)
                    permissionsViewModel.dismissSheet()
                } else {
                    permissionsViewModel.updateAccessibilityGranted(false)
                    // ← Stop the session if it's running without permission
                    if (isFocusActive) {
                        viewModel.stopFocusSession()
                    }
                    permissionsViewModel.checkAndShowSheetIfNeeded(context)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(Unit) { permissionsViewModel.checkAndShowSheetIfNeeded(context) }

    if (sheetState.isVisible && sheetState.type != null) {
        PermissionBottomSheet(
            type      = sheetState.type!!,
            sheetState = permModalState,
            onDismiss = { permissionsViewModel.dismissSheet() },
            onAgree   = { permissionsViewModel.onPermissionSheetAgree(context, sheetState.type!!) }
        )
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { msg ->
            android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_SHORT).show()
            viewModel.dismissError()
        }
    }

    val totalMillis = state.selectedMinutes.toLong() * 60_000L
    val progress    = if (totalMillis > 0 && state.isFocusActive )
        (state.remainingMillis.toFloat() / totalMillis.toFloat()).coerceIn(0f, 1f)
    else 1f

    MainScaffold(selectedTab = selectedTab, onTabSelected = onTabSelected) { paddingValues ->
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                AppScreenHeader(
                    title    = "Focus Mode",
                    subtitle = "Stay Focus",
                    actions  = {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background( Color.White.copy(alpha = 0.10f))
                                .border(
                                    1.dp,
                                    Color.White.copy(alpha = 0.25f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Shield,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = colors.background)
        ) {

                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    TimerCard(
                        remainingMillis = state.remainingMillis,
                        isActive        = state.isFocusActive ,
                        progress        = progress,
                        isFocusActive   = state.isFocusActive,
                        onToggle        = {
                            val granted = isAccessibilityServiceEnabled(context)
                            when {
                                !granted            -> permissionsViewModel.showSheet(PermissionSheetType.ACCESSIBILITY)
                                state.isFocusActive -> viewModel.stopFocusSession()
                                else                -> viewModel.startFocusSession()
                            }
                        }
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentPadding = PaddingValues(),

                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        item {
                            DurationCard(
                                selectedMinutes = state.selectedMinutes,
                                enabled = !state.isFocusActive,
                                onSelect = { viewModel.setSelectedMinutes(it) }
                            )
                        }

                        item {
                            BlockAppsCard(
                                selectedPackages = state.selectedApps,
                                onToggle = { pkg -> viewModel.toggleAppSelection(pkg) }
                            )
                        }

                        item {if (state.isFocusActive && state.selectedApps.isNotEmpty()) {
                            CurrentlyBlockedSection(blockedPackages = state.selectedApps)
                        }
                            }
                    }

                }
           }
        }
    }
}


fun isAccessibilityServiceEnabled(context: android.content.Context): Boolean {
    val expected = ComponentName(context, ReelsAccessibilityService::class.java)
    val enabled  = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
    ) ?: return false
    return enabled.split(":").any { ComponentName.unflattenFromString(it) == expected }
}


@Composable
private fun TimerCard(
    remainingMillis: Long,
    isActive: Boolean,
    progress: Float,
    isFocusActive: Boolean,
    onToggle: () -> Unit
) {
    val colors = LocalAppColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        padding  = PaddingValues(20.dp)
    ) {
        Column(
            modifier            = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            TimerCircle(
                remainingMillis = remainingMillis,
                isActive        = isActive,
                progress        = progress
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .shadow(
                        elevation    = 8.dp,
                        shape        = RoundedCornerShape(999.dp),
                        ambientColor = if (isFocusActive) colors.successGreen.copy(alpha = 0.20f) else colors.glowPurple,
                        spotColor    = if (isFocusActive) colors.successGreen.copy(alpha = 0.20f) else colors.glowPurple
                    )
                    .clip(RoundedCornerShape(999.dp))
                    .background(if(isFocusActive) colors.borderColor else colors.appColor)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication        = null,
                        onClick           = onToggle
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector        = if (isFocusActive) Icons.Filled.Stop else Icons.Filled.PlayArrow,
                        contentDescription = null,
                        tint               = Color.White,
                        modifier           = Modifier.size(18.dp)
                    )
                    Text(
                        text       = if (isFocusActive) "Stop Focus Session" else "Start Focus Session",
                        color      = Color.White,
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}



@Composable
private fun TimerCircle(
    remainingMillis: Long,
    isActive: Boolean,
    progress: Float
) {
    val colors = LocalAppColors.current
    val animatedProgress by animateFloatAsState(
        targetValue   = progress,
        animationSpec = tween(800),
        label         = "timerProgress"
    )

    val totalSeconds = remainingMillis / 1000L
    val hours        = totalSeconds / 3600
    val minutes      = (totalSeconds % 3600) / 60
    val seconds      = totalSeconds % 60

    val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    Box(
        modifier         = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier         = Modifier.size(160.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(160.dp)) {
                val strokeW = 11.dp.toPx()
                val arcSz   = size.width - strokeW

                drawArc(
                    brush = Brush.verticalGradient(
                        colors = if (colors.isDark)
                            listOf(Color(0xFF7C3AED).copy(alpha = 0.25f), Color(0xFF4C1D95).copy(alpha = 0.25f))
                        else
                            listOf(Color(0xFF6B3FA0).copy(alpha = 0.18f), Color(0xFF4A2070).copy(alpha = 0.18f))
                    ),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter  = false,
                    topLeft    = Offset(strokeW / 2, strokeW / 2),
                    size       = Size(arcSz, arcSz),
                    style      = Stroke(width = strokeW, cap = StrokeCap.Round)
                )

                drawArc(
                    brush = Brush.verticalGradient(
                        colors = if (colors.isDark)
                            listOf(Color(0xFF7C3AED), Color(0xFF4C1D95))   // dark header colors
                        else
                            listOf(Color(0xFF6B3FA0), Color(0xFF4A2070))   // light header colors
                    ),
                    startAngle = -90f,
                    sweepAngle = 360f * animatedProgress,
                    useCenter  = false,
                    topLeft    = Offset(strokeW / 2, strokeW / 2),
                    size       = Size(arcSz, arcSz),
                    style      = Stroke(width = strokeW, cap = StrokeCap.Round)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text          = if (isActive) timeString else "00:00:00",
                    color         = if (isActive) colors.textPrimary else colors.textPrimary.copy(alpha = 0.35f),
                    fontSize      = 24.sp,
                    fontWeight    = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Text(
                    text       = if (isActive) "Remaining" else "Ready to focus",
                    color      = colors.textSecondary,
                    fontSize   = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}



private data class DurationChip(val minutes: Int, val label: String)

private val durationChips = listOf(
    DurationChip(15,   "15m"),   DurationChip(30,   "30m"),
    DurationChip(60,   "1h"),    DurationChip(120,  "2h"),
    DurationChip(240,  "4h"),    DurationChip(480,  "8h"),
    DurationChip(1440, "1d")
)

@Composable
private fun DurationCard(
    selectedMinutes: Int,
    enabled: Boolean,
    onSelect: (Int) -> Unit
) {
    val colors = LocalAppColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        padding  = PaddingValues(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            CardSectionTitle("Duration")

          LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(durationChips) { chip ->
                    val isSelected = chip.minutes == selectedMinutes
                    val bg = if (isSelected) colors.appColor
                    else colors.cardSurface

                      Box(
                        modifier = Modifier
                            .height(40.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(bg)
                            .border(
                                width = if (isSelected) 0.dp else 1.dp,
                                color = colors.borderSubtle,
                                shape = RoundedCornerShape(999.dp)
                            )
                            .then(
                                if (enabled) Modifier.clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication        = null
                                ) { onSelect(chip.minutes) } else Modifier
                            )
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text       = chip.label,
                            color      = if (isSelected) Color.White else colors.textSecondary,
                            fontSize   = 14.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun BlockAppsCard(
    selectedPackages: Set<String>,
    isEnabled: Boolean = true,
    onToggle: (String) -> Unit
) {
    val colors = LocalAppColors.current

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            padding  = PaddingValues(horizontal = 20.dp, vertical = 14.dp)
        ) {
            CardSectionTitle("Supported Apps")

         Spacer(Modifier.height(8.dp))

        focusApps.chunked(3).forEach { rowApps ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                rowApps.forEach { app ->
                    val isSelected = selectedPackages.contains(app.packageName)

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .then(
                                if (isEnabled) Modifier.clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication        = null
                                ) { onToggle(app.packageName) } else Modifier
                            )
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(contentAlignment = Alignment.TopEnd) {
                            // App icon — subtle selected ring
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .then(
                                        if (isSelected)
                                            Modifier.border(
                                                width = 2.dp,
                                                brush = Brush.linearGradient(
                                                    listOf(colors.purplePrimary, colors.blueAccent)
                                                ),
                                                shape = RoundedCornerShape(16.dp)
                                            )
                                        else
                                            Modifier.border(
                                                width = 1.dp,
                                                brush = Brush.linearGradient(
                                                    listOf(
                                                        colors.borderSubtle.copy(alpha = 0.40f),
                                                        colors.borderSubtle.copy(alpha = 0.20f)
                                                    )
                                                ),
                                                shape = RoundedCornerShape(16.dp)
                                            )
                                    )
                                    .background(
                                        if (isSelected)
                                            colors.purplePrimary.copy(alpha = if (colors.isDark) 0.12f else 0.07f)
                                        else
                                            Color.Transparent
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter            = painterResource(id = app.iconRes),
                                    contentDescription = app.name,
                                    modifier           = Modifier.size(34.dp)
                                )
                            }

                            // Checkmark badge — only when selected
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.linearGradient(
                                                listOf(colors.purplePrimary, colors.blueAccent)
                                            )
                                        )
                                        .border(1.5.dp, colors.background, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text       = "✓",
                                        color      = Color.White,
                                        fontSize   = 8.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Text(
                            text       = app.name,
                            color      = if (isSelected) colors.textPrimary else colors.textSecondary,
                            fontSize   = 11.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            maxLines   = 1
                        )
                    }
                }
                // Fill empty slots in last row
                repeat(3 - rowApps.size) { Spacer(modifier = Modifier.weight(1f)) }
            }
        }
            }
    }
}

// ── Currently blocked section (from second version) ───────────────────────────
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
                    val iconRes = packageToIconRes(pkg)
                    val name    = packageToName(pkg)

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



// ── Shared section title inside cards ────────────────────────────────────────
@Composable
private fun CardSectionTitle(title: String) {
    val colors = LocalAppColors.current
    Text(
        text       = title,
        color      = colors.textPrimary,
        fontSize   = 17.sp,
        fontWeight = FontWeight.SemiBold
    )
}


@Composable
fun Card(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(18.dp),
    content: @Composable BoxScope.() -> Unit
) {
    val colors = LocalAppColors.current

    Box(
        modifier = modifier
            .shadow(
                elevation = if (colors.isDark) 12.dp else 4.dp,
                shape =  RoundedCornerShape(20.dp),
                ambientColor = colors.glowPurple.copy(alpha = if (colors.isDark) 0.16f else 0.05f),
                spotColor = colors.glowPurple.copy(alpha = if (colors.isDark) 0.16f else 0.05f)
            )
            .clip(shape = RoundedCornerShape(20.dp))
            .background(colors.cardSurface)
            .border(1.dp, colors.borderColor, shape = RoundedCornerShape(20.dp))
            .padding(padding),
        content = content
    )
}

// ── Package → icon / name helpers ─────────────────────────────────────────────
private fun packageToIconRes(pkg: String): Int? = when (pkg) {
    "com.instagram.android"      -> R.drawable.ic_instagram
    "com.google.android.youtube" -> R.drawable.ic_youtube
    "com.facebook.katana"        -> R.drawable.ic_facebook
    "com.zhiliaoapp.musically"   -> R.drawable.ic_tiktok
    "com.snapchat.android"       -> R.drawable.ic_snapchat
    "com.twitter.android"        -> R.drawable.ic_twitter
    else                         -> null
}

private fun packageToName(pkg: String): String = when (pkg) {
    "com.instagram.android"      -> "Instagram"
    "com.google.android.youtube" -> "YouTube"
    "com.facebook.katana"        -> "Facebook"
    "com.zhiliaoapp.musically"   -> "TikTok"
    "com.snapchat.android"       -> "Snapchat"
    "com.twitter.android"        -> "Twitter"
    "com.whatsapp"               -> "WhatsApp"
    else -> pkg.substringAfterLast(".").replaceFirstChar { it.uppercase() }
}