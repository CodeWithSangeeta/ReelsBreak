//package com.practice.reelbreak.ui.onboarding
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.pager.HorizontalPager
//import androidx.compose.foundation.pager.rememberPagerState
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import com.practice.reelbreak.ui.navigation.Routes
//import com.practice.reelbreak.ui.onboarding.component.ButtonGradient
//import com.practice.reelbreak.ui.onboarding.component.FloatingImage
//import com.practice.reelbreak.ui.onboarding.component.IndicatorRow
//import com.practice.reelbreak.ui.onboarding.component.OnboardDescription
//import com.practice.reelbreak.ui.onboarding.component.OnboardHeading
//import com.practice.reelbreak.viewmodel.MainViewModel
//import kotlinx.coroutines.launch
//import com.practice.reelbreak.R
//import com.practice.reelbreak.core.accessibility.ReelsAccessibilityService
//import com.practice.reelbreak.ui.theme.LocalAppColors
//
////@Composable
////fun OnboardingScreen(
////    onComplete: () -> Unit,
////    onSkip: () -> Unit
////) {
////    val coroutineScope = rememberCoroutineScope()
////    val pages = remember {
////        listOf(
////            OnboardPage.Welcome,
////            OnboardPage.Counter,
////            OnboardPage.Break,
////            OnboardPage.Permission
////        )
////    }
////
////    val pageState = rememberPagerState(initialPage = 0, pageCount = { pages.size })
////    val colors = LocalAppColors.current
////
////
////    Column(modifier = Modifier.fillMaxSize()
////        .background(colors.background)
////        .padding(16.dp)
////    ) {
////
////        if(pageState.currentPage !=pages.lastIndex) {
////            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
////                TextButton(onClick = onSkip){
////                    Text(
////                        text = "Skip",
////                        fontSize = 18.sp,
////                        fontWeight = FontWeight.Bold,
////                        color = Color(0xFFC2B5B5),
////                        )
////                }
////            }
////        }
////
////            HorizontalPager(
////                state = pageState,
////                modifier = Modifier.weight(1f)
////            ) { index ->
////                OnboardPageContent(pages[index])
////            }
////
////
////        Spacer(modifier = Modifier.height(40.dp))
////        IndicatorRow(
////            currentPage = pageState.currentPage,
////            totalPages = pages.size
////        )
////        Spacer(modifier = Modifier.height(32.dp))
////
////        ButtonGradient(
////            text = if (pageState.currentPage == pages.lastIndex) "Get Started" else "Continue",
////            onClick = {
////                if(pageState.currentPage < pages.size-1){
////                    coroutineScope.launch {
////                        pageState.animateScrollToPage(pageState.currentPage+1)
////                    }
////
////                }
////                else{
////                  onComplete()
////                }
////            }
////        )
////        Spacer(modifier = Modifier.height(40.dp))
////
////
////    }
////
////}
////
////
////
////@Composable
////fun OnboardPageContent(page: OnboardPage) {
////    Column(
////        modifier = Modifier
////            .fillMaxSize()
////            .padding(18.dp),
////    ) {
////        FloatingImage(
////            imageResId = page.imageRes,
////            floatingDistance = 80f,
////            size = 300.dp
////        )
////
////        Spacer(modifier = Modifier.height(32.dp))
////
////        OnboardHeading(page.title)
////
////        Spacer(modifier = Modifier.height(16.dp))
////
////        OnboardDescription(page.description)
////    }
////}
////
//
//
//
//
//import android.content.ComponentName
//import android.content.Intent
//import android.net.Uri
//import android.provider.Settings
//import androidx.compose.animation.animateColorAsState
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.style.TextAlign
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.compose.LocalLifecycleOwner
//import androidx.lifecycle.LifecycleEventObserver
//import androidx.lifecycle.Lifecycle
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun OnboardingScreen(
//    onComplete: () -> Unit,
//    onSkip: () -> Unit,
//    mainViewModel: MainViewModel = hiltViewModel()
//) {
//    val context = LocalContext.current
//    val coroutineScope = rememberCoroutineScope()
//    val colors = LocalAppColors.current
//
//    val pages = OnboardingPageType.values().toList()
//    val pageState = rememberPagerState(initialPage = 0) { pages.size }
//    val isLastPage = pageState.currentPage == pages.lastIndex
//
//    // ── Permission states (live-checked on resume) ────────────────────
//    var accessibilityGranted by remember {
//        mutableStateOf(isAccessibilityEnabled(context))
//    }
//    var notificationGranted by remember {
//        mutableStateOf(isNotificationEnabled(context))
//    }
//    var overlayGranted by remember {
//        mutableStateOf(Settings.canDrawOverlays(context))
//    }
//
//    // Recheck on every resume (user may return from Settings)
//    val lifecycleOwner = LocalLifecycleOwner.current
//    DisposableEffect(lifecycleOwner) {
//        val observer = LifecycleEventObserver { _, event ->
//            if (event == Lifecycle.Event.ON_RESUME) {
//                accessibilityGranted = isAccessibilityEnabled(context)
//                notificationGranted = isNotificationEnabled(context)
//                overlayGranted = Settings.canDrawOverlays(context)
//            }
//        }
//        lifecycleOwner.lifecycle.addObserver(observer)
//        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
//    }
//
//    // ── Limits state (set on page 3) ──────────────────────────────────
//    var reelLimit by remember { mutableIntStateOf(10) }
//    var timeLimit by remember { mutableIntStateOf(30) }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(brush = colors.background)
//    ) {
//        Column(modifier = Modifier.fillMaxSize()) {
//
//            // ── Skip button (hide on last page) ───────────────────────
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 48.dp, end = 20.dp),
//                contentAlignment = Alignment.CenterEnd
//            ) {
//                if (!isLastPage) {
//                    Text(
//                        text = "Skip",
//                        color = Color.White.copy(alpha = 0.45f),
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Medium,
//                        modifier = Modifier
//                            .clickable(
//                                interactionSource = remember { MutableInteractionSource() },
//                                indication = null,
//                                onClick = onSkip
//                            )
//                            .padding(8.dp)
//                    )
//                }
//            }
//
//            // ── Pager ─────────────────────────────────────────────────
//            HorizontalPager(
//                state = pageState,
//                modifier = Modifier.weight(1f),
//                userScrollEnabled = true
//            ) { index ->
//                when (pages[index]) {
//                    OnboardingPageType.WELCOME -> WelcomePage()
//                    OnboardingPageType.HOW_IT_WORKS -> HowItWorksPage()
//                    OnboardingPageType.LIMITS_SETUP -> LimitsSetupPage(
//                        reelLimit = reelLimit,
//                        timeLimit = timeLimit,
//                        onReelLimitChange = { reelLimit = it },
//                        onTimeLimitChange = { timeLimit = it }
//                    )
//                    OnboardingPageType.OVERLAY_FEATURE -> OverlayFeaturePage()
//                    OnboardingPageType.PERMISSIONS -> PermissionsPage(
//                        accessibilityGranted = accessibilityGranted,
//                        notificationGranted = notificationGranted,
//                        overlayGranted = overlayGranted,
//                        onGrantAccessibility = {
//                            context.startActivity(
//                                Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
//                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                            )
//                        },
//                        onGrantNotification = {
//                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                                context.startActivity(
//                                    Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
//                                        .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
//                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                )
//                            }
//                        },
//                        onGrantOverlay = {
//                            context.startActivity(
//                                Intent(
//                                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                                    Uri.parse("package:${context.packageName}")
//                                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                            )
//                        }
//                    )
//                    OnboardingPageType.ALL_SET -> AllSetPage()
//                }
//            }
//
//            // ── Indicator dots ────────────────────────────────────────
//            OnboardingIndicator(
//                currentPage = pageState.currentPage,
//                totalPages = pages.size
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // ── CTA Button ────────────────────────────────────────────
//            OnboardingButton(
//                text = if (isLastPage) "Start Using ReelsBreak 🚀" else "Continue",
//                onClick = {
//                    if (isLastPage) {
//                        coroutineScope.launch {
//                            mainViewModel.completeOnboarding()
//                            onComplete()
//                        }
//                    } else {
//                        coroutineScope.launch {
//                            pageState.animateScrollToPage(pageState.currentPage + 1)
//                        }
//                    }
//                }
//            )
//
//            Spacer(modifier = Modifier.height(36.dp))
//        }
//    }
//}
//
//// ─────────────────────────────────────────────────────────────────────────────
//// PAGE 1: Welcome
//// ─────────────────────────────────────────────────────────────────────────────
//@Composable
//private fun WelcomePage() {
//    PageContainer {
//        Box(
//            modifier = Modifier
//                .size(120.dp)
//                .shadow(
//                    elevation = 32.dp,
//                    shape = CircleShape,
//                    spotColor = Color(0xFF9333EA),
//                    ambientColor = Color(0xFF9333EA)
//                )
//                .clip(CircleShape)
//                .background(
//                    Brush.radialGradient(
//                        listOf(Color(0xFF7C3AED), Color(0xFF4F46E5))
//                    )
//                ),
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(
//                imageVector = Icons.Filled.Block,
//                contentDescription = null,
//                tint = Color.White,
//                modifier = Modifier.size(56.dp)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        Text(
//            text = "Welcome to\nReelsBreak",
//            color = Color.White,
//            fontSize = 32.sp,
//            fontWeight = FontWeight.Bold,
//            textAlign = TextAlign.Center,
//            lineHeight = 40.sp
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text(
//            text = "Take back control of your scroll.\nBlock short-form videos before they steal your focus.",
//            color = Color.White.copy(alpha = 0.65f),
//            fontSize = 15.sp,
//            textAlign = TextAlign.Center,
//            lineHeight = 22.sp
//        )
//
//        Spacer(modifier = Modifier.height(36.dp))
//
//        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
//            StatPill(icon = Icons.Filled.Block,        text = "Block Reels")
//            StatPill(icon = Icons.Filled.Timer,        text = "Set Limits")
//            StatPill(icon = Icons.Filled.TrackChanges, text = "Stay Focused")
//        }
//    }
//}
//
//// ─────────────────────────────────────────────────────────────────────────────
//// PAGE 2: How It Works
//@Composable
//private fun HowItWorksPage() {
//    PageContainer {
//        PageTitle(
//            icon = Icons.Filled.Bolt,
//            iconTint = Color(0xFFFBBF24),
//            title = "How It Works"
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        PageSubtitle("Three powerful modes to suit your lifestyle")
//        Spacer(modifier = Modifier.height(28.dp))
//
//        FeatureCard(
//            icon = Icons.Filled.Block,
//            title = "Block Instantly",
//            description = "Opens any short-form feed? Blocked immediately. No exceptions.",
//            gradient = Brush.linearGradient(listOf(Color(0xFF4C1D95), Color(0xFF7C3AED)))
//        )
//        Spacer(modifier = Modifier.height(12.dp))
//        FeatureCard(
//            icon = Icons.Filled.HourglassEmpty,
//            title = "Set a Daily Limit",
//            description = "Watch a fixed number of reels or minutes, then auto-block for the day.",
//            gradient = Brush.linearGradient(listOf(Color(0xFF1E3A8A), Color(0xFF3B82F6)))
//        )
//        Spacer(modifier = Modifier.height(12.dp))
//        FeatureCard(
//            icon = Icons.Filled.Lock,
//            title = "Focus Mode",
//            description = "Lock yourself out for a set session — 15 mins to 5 days.",
//            gradient = Brush.linearGradient(listOf(Color(0xFF065F46), Color(0xFF10B981)))
//        )
//    }
//}
//
//// ─────────────────────────────────────────────────────────────────────────────
//// PA
//@Composable
//private fun LimitsSetupPage(
//    reelLimit: Int,
//    timeLimit: Int,
//    onReelLimitChange: (Int) -> Unit,
//    onTimeLimitChange: (Int) -> Unit
//) {
//    PageContainer {
//        PageTitle(
//            icon = Icons.Filled.Tune,
//            iconTint = Color(0xFF9333EA),
//            title = "Set Your Limits"
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        PageSubtitle("You can always adjust these later in Settings")
//        Spacer(modifier = Modifier.height(28.dp))
//
//        LimitSliderCard(
//            label = "Daily Reel Limit",
//            icon = Icons.Filled.Videocam,
//            iconTint = Color(0xFF9333EA),
//            value = reelLimit.toFloat(),
//            valueRange = 1f..50f,
//            displayValue = "$reelLimit reels/day",
//            steps = 48,
//            accentColor = Color(0xFF9333EA),
//            onValueChange = { onReelLimitChange(it.toInt()) }
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        LimitSliderCard(
//            label = "Daily Time Limit",
//            icon = Icons.Filled.Timer,
//            iconTint = Color(0xFF3B82F6),
//            value = timeLimit.toFloat(),
//            valueRange = 5f..120f,
//            displayValue = if (timeLimit >= 60)
//                "${timeLimit / 60}h ${timeLimit % 60}m/day"
//            else "$timeLimit min/day",
//            steps = 22,
//            accentColor = Color(0xFF3B82F6),
//            onValueChange = { onTimeLimitChange(it.toInt()) }
//        )
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clip(RoundedCornerShape(12.dp))
//                .background(Color(0xFF1C1233))
//                .padding(12.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(10.dp)
//        ) {
//            Icon(
//                imageVector = Icons.Filled.Lightbulb,
//                contentDescription = null,
//                tint = Color(0xFFFBBF24),
//                modifier = Modifier.size(18.dp)
//            )
//            Text(
//                text = "These apply when Limit Mode is active on the home screen.",
//                color = Color.White.copy(alpha = 0.6f),
//                fontSize = 12.sp,
//                lineHeight = 17.sp
//            )
//        }
//    }
//}
//
//// ─────────────────────────────────────────────────────────────────────────────
//// PAGE 4: Overlay Feature
//// ─────────────────────────────────────────────────────────────────────────────
//@Composable
//private fun OverlayFeaturePage() {
//    PageContainer {
//        PageTitle(
//            icon = Icons.Filled.Visibility,
//            iconTint = Color(0xFF3B82F6),
//            title = "Live Overlay Tracker"
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        PageSubtitle("A floating bubble shows your usage in real-time — even while inside other apps")
//        Spacer(modifier = Modifier.height(28.dp))
//
//        // Overlay bubble preview (no emojis inside either)
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clip(RoundedCornerShape(20.dp))
//                .background(Color(0xFF1C1233))
//                .border(1.dp, Color(0xFF2D1B4E), RoundedCornerShape(20.dp))
//                .padding(20.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                Box(
//                    modifier = Modifier
//                        .size(width = 180.dp, height = 120.dp)
//                        .clip(RoundedCornerShape(16.dp))
//                        .background(Color(0xFF0F0A1E))
//                        .border(1.dp, Color(0xFF2D1B4E), RoundedCornerShape(16.dp))
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .align(Alignment.TopEnd)
//                            .padding(8.dp)
//                            .clip(RoundedCornerShape(12.dp))
//                            .background(
//                                Brush.linearGradient(
//                                    listOf(Color(0xFF7C3AED), Color(0xFF4F46E5))
//                                )
//                            )
//                            .padding(horizontal = 10.dp, vertical = 6.dp)
//                    ) {
//                        Column {
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically,
//                                horizontalArrangement = Arrangement.spacedBy(4.dp)
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Filled.Videocam,
//                                    contentDescription = null,
//                                    tint = Color.White,
//                                    modifier = Modifier.size(10.dp)
//                                )
//                                Text(
//                                    text = "7 reels",
//                                    color = Color.White,
//                                    fontSize = 10.sp,
//                                    fontWeight = FontWeight.SemiBold
//                                )
//                            }
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically,
//                                horizontalArrangement = Arrangement.spacedBy(4.dp)
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Filled.Timer,
//                                    contentDescription = null,
//                                    tint = Color.White.copy(alpha = 0.75f),
//                                    modifier = Modifier.size(10.dp)
//                                )
//                                Text(
//                                    text = "12 min",
//                                    color = Color.White.copy(alpha = 0.75f),
//                                    fontSize = 9.sp
//                                )
//                            }
//                        }
//                    }
//                    Column(
//                        modifier = Modifier
//                            .align(Alignment.BottomStart)
//                            .padding(10.dp),
//                        verticalArrangement = Arrangement.spacedBy(4.dp)
//                    ) {
//                        repeat(3) {
//                            Box(
//                                modifier = Modifier
//                                    .height(6.dp)
//                                    .fillMaxWidth(if (it == 2) 0.5f else 0.8f)
//                                    .clip(CircleShape)
//                                    .background(Color.White.copy(alpha = 0.15f))
//                            )
//                        }
//                    }
//                }
//                Text(
//                    text = "Overlay preview",
//                    color = Color.White.copy(alpha = 0.4f),
//                    fontSize = 11.sp
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        // Feature bullets with icons
//        data class OverlayBullet(val icon: ImageVector, val tint: Color, val text: String)
//        listOf(
//            OverlayBullet(Icons.Filled.BarChart,       Color(0xFF9333EA), "Live reel count while you scroll"),
//            OverlayBullet(Icons.Filled.AccessTime,     Color(0xFF3B82F6), "Real-time minutes spent tracker"),
//            OverlayBullet(Icons.Filled.Apps,           Color(0xFF10B981), "Works across Instagram, YouTube, TikTok & more"),
//            OverlayBullet(Icons.Filled.SettingsInputComponent, Color(0xFFFBBF24), "Enable/disable anytime from Settings")
//        ).forEach { bullet ->
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 5.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                Box(
//                    modifier = Modifier
//                        .size(32.dp)
//                        .clip(RoundedCornerShape(8.dp))
//                        .background(bullet.tint.copy(alpha = 0.15f)),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Icon(
//                        imageVector = bullet.icon,
//                        contentDescription = null,
//                        tint = bullet.tint,
//                        modifier = Modifier.size(16.dp)
//                    )
//                }
//                Text(
//                    text = bullet.text,
//                    color = Color.White.copy(alpha = 0.75f),
//                    fontSize = 13.sp
//                )
//            }
//        }
//    }
//}
//
//// ─────────────────────────────────────────────────────────────────────────────
//// PAGE 5: Permissions
//// ─────────────────────────────────────────────────────────────────────────────
//@Composable
//private fun PermissionsPage(
//    accessibilityGranted: Boolean,
//    notificationGranted: Boolean,
//    overlayGranted: Boolean,
//    onGrantAccessibility: () -> Unit,
//    onGrantNotification: () -> Unit,
//    onGrantOverlay: () -> Unit
//) {
//    PageContainer {
//        PageTitle(
//            icon = Icons.Filled.Security,
//            iconTint = Color(0xFF9333EA),
//            title = "Grant Permissions"
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        PageSubtitle("These are required for ReelsBreak to work properly")
//        Spacer(modifier = Modifier.height(24.dp))
//
//        OnboardPermissionCard(
//            icon = Icons.Filled.Accessibility,
//            title = "Accessibility Service",
//            description = "Detects when you open a reels feed and triggers the block.",
//            isGranted = accessibilityGranted,
//            isRequired = true,
//            onGrant = onGrantAccessibility
//        )
//        Spacer(modifier = Modifier.height(12.dp))
//        OnboardPermissionCard(
//            icon = Icons.Filled.Layers,
//            title = "Display Over Other Apps",
//            description = "Allows the live usage bubble to float over any app you're using.",
//            isGranted = overlayGranted,
//            isRequired = false,
//            onGrant = onGrantOverlay
//        )
//        Spacer(modifier = Modifier.height(12.dp))
//        OnboardPermissionCard(
//            icon = Icons.Filled.Notifications,
//            title = "Notifications",
//            description = "Get alerts when your daily limit is reached or a focus session ends.",
//            isGranted = notificationGranted,
//            isRequired = false,
//            onGrant = onGrantNotification
//        )
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clip(RoundedCornerShape(12.dp))
//                .background(Color(0xFF0D1B0D))
//                .border(1.dp, Color(0xFF22C55E).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
//                .padding(12.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(10.dp)
//        ) {
//            Icon(
//                imageVector = Icons.Filled.Shield,
//                contentDescription = null,
//                tint = Color(0xFF4ADE80),
//                modifier = Modifier.size(18.dp)
//            )
//            Text(
//                text = "ReelsBreak never reads your messages or personal data. Accessibility is used only to detect reels screens.",
//                color = Color(0xFF4ADE80).copy(alpha = 0.8f),
//                fontSize = 11.sp,
//                lineHeight = 16.sp
//            )
//        }
//    }
//}
//// ─────────────────────────────────────────────────────────────────────────────
//// PAGE 6: All Set
//// ─────────────────────────────────────────────────────────────────────────────
//@Composable
//private fun AllSetPage() {
//    PageContainer {
//        Box(
//            modifier = Modifier
//                .size(130.dp)
//                .shadow(
//                    elevation = 40.dp,
//                    shape = CircleShape,
//                    spotColor = Color(0xFF22C55E),
//                    ambientColor = Color(0xFF22C55E)
//                )
//                .clip(CircleShape)
//                .background(
//                    Brush.radialGradient(
//                        listOf(Color(0xFF065F46), Color(0xFF10B981))
//                    )
//                ),
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(
//                imageVector = Icons.Filled.CheckCircle,
//                contentDescription = null,
//                tint = Color.White,
//                modifier = Modifier.size(64.dp)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        Text(
//            text = "You're All Set!",
//            color = Color.White,
//            fontSize = 32.sp,
//            fontWeight = FontWeight.Bold,
//            textAlign = TextAlign.Center
//        )
//        Spacer(modifier = Modifier.height(12.dp))
//        Text(
//            text = "ReelsBreak is ready to guard your focus.\nScroll less. Live more.",
//            color = Color.White.copy(alpha = 0.65f),
//            fontSize = 15.sp,
//            textAlign = TextAlign.Center,
//            lineHeight = 22.sp
//        )
//
//        Spacer(modifier = Modifier.height(36.dp))
//
//        data class SummaryItem(val icon: ImageVector, val tint: Color, val label: String)
//        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
//            listOf(
//                SummaryItem(Icons.Filled.Block,         Color(0xFFEF4444), "Blocking engine ready"),
//                SummaryItem(Icons.Filled.TrackChanges,  Color(0xFF9333EA), "Focus Mode configured"),
//                SummaryItem(Icons.Filled.Visibility,    Color(0xFF3B82F6), "Overlay tracker available"),
//                SummaryItem(Icons.Filled.Notifications, Color(0xFFFBBF24), "Notifications set up")
//            ).forEach { item ->
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clip(RoundedCornerShape(12.dp))
//                        .background(Color(0xFF1C1233))
//                        .padding(horizontal = 16.dp, vertical = 12.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//                    Icon(
//                        imageVector = item.icon,
//                        contentDescription = null,
//                        tint = item.tint,
//                        modifier = Modifier.size(18.dp)
//                    )
//                    Text(
//                        text = item.label,
//                        color = Color.White.copy(alpha = 0.85f),
//                        fontSize = 13.sp,
//                        fontWeight = FontWeight.Medium
//                    )
//                    Spacer(modifier = Modifier.weight(1f))
//                    Icon(
//                        imageVector = Icons.Filled.Check,
//                        contentDescription = null,
//                        tint = Color(0xFF4ADE80),
//                        modifier = Modifier.size(16.dp)
//                    )
//                }
//            }
//        }
//    }
//}
//// ─────────────────────────────────────────────────────────────────────────────
//// SHARED SUB-COMPOSABLES
//// ─────────────────────────────────────────────────────────────────────────────
//
//@Composable
//private fun PageContainer(content: @Composable ColumnScope.() -> Unit) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//            .padding(horizontal = 24.dp)
//            .padding(top = 12.dp, bottom = 24.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        content = content
//    )
//}
//
//@Composable
//private fun PageTitle(icon: ImageVector, iconTint: Color = Color(0xFF9333EA), title: String) {
//    Box(
//        modifier = Modifier
//            .size(64.dp)
//            .clip(RoundedCornerShape(20.dp))
//            .background(iconTint.copy(alpha = 0.15f))
//            .border(1.dp, iconTint.copy(alpha = 0.4f), RoundedCornerShape(20.dp)),
//        contentAlignment = Alignment.Center
//    ) {
//        Icon(
//            imageVector = icon,
//            contentDescription = null,
//            tint = iconTint,
//            modifier = Modifier.size(32.dp)
//        )
//    }
//    Spacer(modifier = Modifier.height(16.dp))
//    Text(
//        text = title,
//        color = Color.White,
//        fontSize = 28.sp,
//        fontWeight = FontWeight.Bold,
//        textAlign = TextAlign.Center
//    )
//}
//
//@Composable
//private fun PageSubtitle(text: String) {
//    Text(
//        text = text,
//        color = Color.White.copy(alpha = 0.55f),
//        fontSize = 14.sp,
//        textAlign = TextAlign.Center,
//        lineHeight = 21.sp
//    )
//}
//
//@Composable
//private fun StatPill(icon: ImageVector, text: String) {
//    Row(
//        modifier = Modifier
//            .clip(RoundedCornerShape(999.dp))
//            .background(Color(0xFF2D1B4E))
//            .border(1.dp, Color(0xFF7C3AED).copy(alpha = 0.5f), RoundedCornerShape(999.dp))
//            .padding(horizontal = 12.dp, vertical = 8.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.spacedBy(6.dp)
//    ) {
//        Icon(
//            imageVector = icon,
//            contentDescription = null,
//            tint = Color(0xFFB794F4),
//            modifier = Modifier.size(14.dp)
//        )
//        Text(
//            text = text,
//            color = Color.White.copy(alpha = 0.85f),
//            fontSize = 12.sp,
//            fontWeight = FontWeight.Medium
//        )
//    }
//}
//
//@Composable
//private fun FeatureCard(
//    icon: ImageVector,
//    title: String,
//    description: String,
//    gradient: Brush
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(16.dp))
//            .background(gradient)
//            .padding(16.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.spacedBy(14.dp)
//    ) {
//        Box(
//            modifier = Modifier
//                .size(44.dp)
//                .clip(RoundedCornerShape(12.dp))
//                .background(Color.Black.copy(alpha = 0.25f)),
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(
//                imageVector = icon,
//                contentDescription = null,
//                tint = Color.White,
//                modifier = Modifier.size(24.dp)
//            )
//        }
//        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
//            Text(
//                text = title,
//                color = Color.White,
//                fontSize = 14.sp,
//                fontWeight = FontWeight.SemiBold
//            )
//            Text(
//                text = description,
//                color = Color.White.copy(alpha = 0.7f),
//                fontSize = 12.sp,
//                lineHeight = 17.sp
//            )
//        }
//    }
//}
//
//@Composable
//private fun LimitSliderCard(
//    label: String,
//    icon: ImageVector,
//    iconTint: Color,
//    value: Float,
//    valueRange: ClosedFloatingPointRange<Float>,
//    displayValue: String,
//    steps: Int,
//    accentColor: Color,
//    onValueChange: (Float) -> Unit
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(16.dp))
//            .background(Color(0xFF1C1233))
//            .border(1.dp, Color(0xFF2D1B4E), RoundedCornerShape(16.dp))
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(10.dp)
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                Icon(
//                    imageVector = icon,
//                    contentDescription = null,
//                    tint = iconTint,
//                    modifier = Modifier.size(18.dp)
//                )
//                Text(
//                    text = label,
//                    color = Color.White.copy(alpha = 0.85f),
//                    fontSize = 13.sp,
//                    fontWeight = FontWeight.SemiBold
//                )
//            }
//            Box(
//                modifier = Modifier
//                    .clip(RoundedCornerShape(8.dp))
//                    .background(accentColor.copy(alpha = 0.2f))
//                    .border(1.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
//                    .padding(horizontal = 10.dp, vertical = 4.dp)
//            ) {
//                Text(
//                    text = displayValue,
//                    color = accentColor,
//                    fontSize = 12.sp,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//        }
//        Slider(
//            value = value,
//            onValueChange = onValueChange,
//            valueRange = valueRange,
//            steps = steps,
//            modifier = Modifier.fillMaxWidth(),
//            colors = SliderDefaults.colors(
//                thumbColor = accentColor,
//                activeTrackColor = accentColor,
//                inactiveTrackColor = accentColor.copy(alpha = 0.2f)
//            )
//        )
//    }
//}
//
//@Composable
//private fun OnboardPermissionCard(
//    icon: ImageVector,
//    title: String,
//    description: String,
//    isGranted: Boolean,
//    isRequired: Boolean,
//    onGrant: () -> Unit
//) {
//    val borderColor by animateColorAsState(
//        targetValue = if (isGranted) Color(0xFF22C55E).copy(alpha = 0.5f)
//        else Color(0xFF2D1B4E),
//        animationSpec = tween(300),
//        label = "permBorder"
//    )
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(16.dp))
//            .background(
//                if (isGranted) Color(0xFF0D1B0D)
//                else Color(0xFF1C1233)
//            )
//            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
//            .padding(16.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.spacedBy(12.dp)
//    ) {
//        // Icon box
//        Box(
//            modifier = Modifier
//                .size(44.dp)
//                .clip(RoundedCornerShape(12.dp))
//                .background(
//                    if (isGranted) Color(0xFF22C55E).copy(alpha = 0.2f)
//                    else Color(0xFF2D1B4E)
//                ),
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(
//                imageVector = icon,
//                contentDescription = null,
//                tint = if (isGranted) Color(0xFF4ADE80) else Color(0xFF9333EA),
//                modifier = Modifier.size(22.dp)
//            )
//        }
//
//        // Text
//        Column(
//            modifier = Modifier.weight(1f),
//            verticalArrangement = Arrangement.spacedBy(2.dp)
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(6.dp)
//            ) {
//                Text(
//                    text = title,
//                    color = Color.White,
//                    fontSize = 13.sp,
//                    fontWeight = FontWeight.SemiBold
//                )
//                if (isRequired) {
//                    Box(
//                        modifier = Modifier
//                            .clip(RoundedCornerShape(4.dp))
//                            .background(Color(0xFF7C3AED).copy(alpha = 0.3f))
//                            .padding(horizontal = 5.dp, vertical = 1.dp)
//                    ) {
//                        Text(
//                            text = "Required",
//                            color = Color(0xFFB794F4),
//                            fontSize = 9.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//                }
//            }
//            Text(
//                text = description,
//                color = Color.White.copy(alpha = 0.5f),
//                fontSize = 11.sp,
//                lineHeight = 16.sp
//            )
//        }
//
//        // Grant / Granted button
//        if (isGranted) {
//            Box(
//                modifier = Modifier
//                    .clip(CircleShape)
//                    .background(Color(0xFF22C55E).copy(alpha = 0.2f))
//                    .padding(8.dp)
//            ) {
//                Icon(
//                    imageVector = Icons.Filled.Check,
//                    contentDescription = "Granted",
//                    tint = Color(0xFF4ADE80),
//                    modifier = Modifier.size(16.dp)
//                )
//            }
//        } else {
//            Box(
//                modifier = Modifier
//                    .clip(RoundedCornerShape(10.dp))
//                    .background(Color(0xFF7C3AED).copy(alpha = 0.3f))
//                    .border(1.dp, Color(0xFF9333EA).copy(alpha = 0.5f), RoundedCornerShape(10.dp))
//                    .clickable { onGrant() }
//                    .padding(horizontal = 12.dp, vertical = 8.dp)
//            ) {
//                Text(
//                    text = "Enable",
//                    color = Color(0xFFB794F4),
//                    fontSize = 12.sp,
//                    fontWeight = FontWeight.SemiBold
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun OnboardingIndicator(currentPage: Int, totalPages: Int) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        repeat(totalPages) { index ->
//            val isActive = index == currentPage
//            Box(
//                modifier = Modifier
//                    .padding(horizontal = 4.dp)
//                    .height(6.dp)
//                    .width(if (isActive) 24.dp else 6.dp)
//                    .clip(CircleShape)
//                    .background(
//                        if (isActive) Color(0xFF9333EA)
//                        else Color.White.copy(alpha = 0.25f)
//                    )
//            )
//        }
//    }
//}
//
//@Composable
//private fun OnboardingButton(text: String, onClick: () -> Unit) {
//    val colors = LocalAppColors.current
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 24.dp)
//            .height(56.dp)
//            .shadow(
//                elevation = 16.dp,
//                shape = RoundedCornerShape(18.dp),
//                spotColor = Color(0xFF9333EA),
//                ambientColor = Color(0xFF9333EA)
//            )
//            .clip(RoundedCornerShape(18.dp))
//            .background(colors.button)
//            .clickable(
//                interactionSource = remember { MutableInteractionSource() },
//                indication = null,
//                onClick = onClick
//            ),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = text,
//            color = Color.White,
//            fontSize = 16.sp,
//            fontWeight = FontWeight.Bold
//        )
//    }
//}
//
//// ─────────────────────────────────────────────────────────────────────────────
//// HELPERS
//// ─────────────────────────────────────────────────────────────────────────────
//
//private fun isAccessibilityEnabled(context: android.content.Context): Boolean {
//    val expectedComponent = ComponentName(
//        context,
//        ReelsAccessibilityService::class.java
//    )
//    val enabledServices = Settings.Secure.getString(
//        context.contentResolver,
//        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
//    ) ?: return false
//    return enabledServices.split(":").any { entry ->
//        ComponentName.unflattenFromString(entry) == expectedComponent
//    }
//}
//
//private fun isNotificationEnabled(context: android.content.Context): Boolean {
//    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//        val manager = context.getSystemService(android.app.NotificationManager::class.java)
//        manager?.areNotificationsEnabled() == true
//    } else true
//}


package com.practice.reelbreak.ui.onboarding

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.practice.reelbreak.R
import com.practice.reelbreak.core.accessibility.ReelsAccessibilityService
import com.practice.reelbreak.ui.theme.LocalAppColors
import com.practice.reelbreak.viewmodel.MainViewModel
import kotlinx.coroutines.launch

// ─── PAGE TYPES ────────────────────────────────────────────────────────────
enum class OnboardingPageType {
    WELCOME, HOWITWORKS, LIMITSSETUP, PERMISSIONS, ALLSET
}

// ─── MAIN SCREEN ────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    onSkip: () -> Unit,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val colors = LocalAppColors.current
    val config = LocalConfiguration.current

    val screenHeight = config.screenHeightDp.dp
    val isCompact = screenHeight < 700.dp

    val pages = OnboardingPageType.values().toList()
    val pageState = rememberPagerState(initialPage = 0) { pages.size }
    val isLastPage = pageState.currentPage == pages.lastIndex

    // Permission states
    var accessibilityGranted by remember { mutableStateOf(isAccessibilityEnabled(context)) }
    var overlayGranted by remember { mutableStateOf(Settings.canDrawOverlays(context)) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                accessibilityGranted = isAccessibilityEnabled(context)
                overlayGranted = Settings.canDrawOverlays(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    // Animated background orbs
    val infiniteTransition = rememberInfiniteTransition(label = "orb")
    val orbOffset by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(6000, easing = LinearEasing), RepeatMode.Reverse),
        label = "orbOff"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Color(0xFF0D0618), Color(0xFF130A22), Color(0xFF0D0618)))
            )
    ) {
        // Decorative blurred orbs
        Canvas(modifier = Modifier.fillMaxSize()) { drawOrbBackground(orbOffset) }

        Column(modifier = Modifier.fillMaxSize()) {

            // ── Top bar ──────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "${pageState.currentPage + 1} / ${pages.size}",
                    color = Color.White.copy(alpha = 0.35f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                if (!isLastPage) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clip(RoundedCornerShape(999.dp))
                            .background(Color.White.copy(alpha = 0.08f))
                            .clickable(
                                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                                indication = null,
                                onClick = onSkip
                            )
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text("Skip", color = Color.White.copy(alpha = 0.55f), fontSize = 13.sp,
                            fontWeight = FontWeight.Medium)
                    }
                }
            }

            // ── Pager ────────────────────────────────────
            HorizontalPager(
                state = pageState,
                modifier = Modifier.weight(1f),
                userScrollEnabled = true
            ) { index ->
                when (pages[index]) {
                    OnboardingPageType.WELCOME ->
                        WelcomePage(isCompact = isCompact)
                    OnboardingPageType.HOWITWORKS ->
                        HowItWorksPage(isCompact = isCompact)
                    OnboardingPageType.LIMITSSETUP ->
                        LimitsSetupPage(isCompact = isCompact)
                    OnboardingPageType.PERMISSIONS ->
                        PermissionsPage(
                            isCompact = isCompact,
                            accessibilityGranted = accessibilityGranted,
                            overlayGranted = overlayGranted,
                            onGrantAccessibility = {
                                context.startActivity(
                                    Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                )
                            },
                            onGrantOverlay = {
                                context.startActivity(
                                    Intent(
                                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                        Uri.parse("package:${context.packageName}")
                                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                )
                            }
                        )
                    OnboardingPageType.ALLSET ->
                        AllSetPage(isCompact = isCompact)
                }
            }

            // ── Bottom bar ───────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 28.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PillIndicator(currentPage = pageState.currentPage, totalPages = pages.size)
                OnboardingCTAButton(
                    text = if (isLastPage) "Start Using ReelBreak" else "Continue",
                    onClick = {
                        if (isLastPage) {
                            coroutineScope.launch {
                                mainViewModel.completeOnboarding()
                                onComplete()
                            }
                        } else {
                            coroutineScope.launch {
                                pageState.animateScrollToPage(pageState.currentPage + 1)
                            }
                        }
                    }
                )
            }
        }
    }
}

// ─── PAGE 1: WELCOME ────────────────────────────────────────────────────────
@Composable
private fun WelcomePage(isCompact: Boolean) {
    PageLayout(isCompact = isCompact) {
        BlendedImage(
            imageRes = R.drawable.gemini_generated_image_quxe1oquxe1oquxe,
            contentDescription = "Welcome illustration",
            isCompact = isCompact
        )
        Spacer(modifier = Modifier.height(if (isCompact) 20.dp else 32.dp))
        Text(
            text = "Take Back Scroll",
            color = Color.White,
            fontSize = if (isCompact) 28.sp else 34.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = if (isCompact) 34.sp else 42.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "ReelBreak blocks short-form videos before they steal hours of your day.",
            color = Color.White.copy(alpha = 0.58f),
            fontSize = if (isCompact) 13.sp else 15.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(if (isCompact) 20.dp else 28.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FeaturePill(icon = Icons.Filled.Block, text = "Block")
            Spacer(Modifier.width(8.dp))
            FeaturePill(icon = Icons.Filled.Timer, text = "Limit")
            Spacer(Modifier.width(8.dp))
            FeaturePill(icon = Icons.Filled.Shield, text = "Focus")
        }
    }
}

// ─── PAGE 2: HOW IT WORKS ───────────────────────────────────────────────────
@Composable
private fun HowItWorksPage(isCompact: Boolean) {
    PageLayout(isCompact = isCompact) {
        BlendedImage(
            imageRes = R.drawable.gemini_generated_image_uhy2ueuhy2ueuhy2__1_,
            contentDescription = "How it works illustration",
            isCompact = isCompact
        )
        Spacer(modifier = Modifier.height(if (isCompact) 20.dp else 28.dp))
        PageHeading(
            title = "Three Ways to Stay in Control",
            subtitle = "Pick the mode that fits your goals",
            isCompact = isCompact
        )
        Spacer(modifier = Modifier.height(if (isCompact) 16.dp else 22.dp))
        ModeRow(
            icon = Icons.Filled.Block, iconColor = Color(0xFFF87171),
            title = "Block Instantly",
            desc = "Every reel attempt is closed the moment you open it.",
            isCompact = isCompact
        )
        Spacer(Modifier.height(10.dp))
        ModeRow(
            icon = Icons.Filled.HourglassEmpty, iconColor = Color(0xFF60A5FA),
            title = "Set a Daily Limit",
            desc = "Watch a set number of reels or minutes, then auto-block.",
            isCompact = isCompact
        )
        Spacer(Modifier.height(10.dp))
        ModeRow(
            icon = Icons.Filled.Lock, iconColor = Color(0xFF34D399),
            title = "Focus Session",
            desc = "Lock yourself out for a timed session — from 15 min to 5 days.",
            isCompact = isCompact
        )
    }
}

// ─── PAGE 3: LIMITS SETUP ───────────────────────────────────────────────────
@Composable
private fun LimitsSetupPage(isCompact: Boolean) {
    PageLayout(isCompact = isCompact) {
        BlendedImage(
            imageRes = R.drawable.gemini_generated_image_bp6w86bp6w86bp6w,
            contentDescription = "Limits illustration",
            isCompact = isCompact
        )
        Spacer(modifier = Modifier.height(if (isCompact) 20.dp else 28.dp))
        PageHeading(
            title = "Your Rules, Your Schedule",
            subtitle = "Customize daily limits that fit your life — change them anytime.",
            isCompact = isCompact
        )
        Spacer(modifier = Modifier.height(if (isCompact) 16.dp else 22.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            InfoStatCard(
                icon = Icons.Filled.Videocam, iconColor = Color(0xFF8B5CF6),
                value = "Reel Count", subtext = "Limit total reels/day",
                modifier = Modifier.weight(1f), isCompact = isCompact
            )
            InfoStatCard(
                icon = Icons.Filled.AccessTime, iconColor = Color(0xFF60A5FA),
                value = "Time Limit", subtext = "Limit minutes/day",
                modifier = Modifier.weight(1f), isCompact = isCompact
            )
        }
        Spacer(Modifier.height(12.dp))
        TipBox(
            text = "Set limits from the Dashboard once you're inside the app. You can change them any time.",
            isCompact = isCompact
        )
    }
}

// ─── PAGE 4: PERMISSIONS ────────────────────────────────────────────────────
@Composable
private fun PermissionsPage(
    isCompact: Boolean,
    accessibilityGranted: Boolean,
    overlayGranted: Boolean,
    onGrantAccessibility: () -> Unit,
    onGrantOverlay: () -> Unit
) {
    PageLayout(isCompact = isCompact) {
        Box(
            modifier = Modifier
                .size(if (isCompact) 72.dp else 88.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFF8B5CF6).copy(alpha = 0.30f), Color.Transparent)
                    )
                )
                .border(1.dp, Color(0xFF8B5CF6).copy(alpha = 0.40f), RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Security,
                contentDescription = null,
                tint = Color(0xFF8B5CF6),
                modifier = Modifier.size(if (isCompact) 34.dp else 42.dp)
            )
        }
        Spacer(modifier = Modifier.height(if (isCompact) 16.dp else 22.dp))
        PageHeading(
            title = "Quick Permissions",
            subtitle = "ReelBreak needs these to work properly",
            isCompact = isCompact
        )
        Spacer(modifier = Modifier.height(if (isCompact) 16.dp else 22.dp))
        PermissionRow(
            icon = Icons.Filled.Accessibility, iconColor = Color(0xFF8B5CF6),
            title = "Accessibility Service",
            description = "Detects when you open a reels feed & triggers the block.",
            isGranted = accessibilityGranted, badge = "Required",
            badgeColor = Color(0xFF8B5CF6), onGrant = onGrantAccessibility,
            isCompact = isCompact
        )
        Spacer(Modifier.height(10.dp))
        PermissionRow(
            icon = Icons.Filled.Layers, iconColor = Color(0xFF34D399),
            title = "Display Over Apps",
            description = "Shows a live usage counter floating above any app.",
            isGranted = overlayGranted, badge = "Optional",
            badgeColor = Color(0xFF34D399), onGrant = onGrantOverlay,
            isCompact = isCompact
        )
        Spacer(Modifier.height(14.dp))
        PrivacyNote(isCompact = isCompact)
    }
}

// ─── PAGE 5: ALL SET ────────────────────────────────────────────────────────
@Composable
private fun AllSetPage(isCompact: Boolean) {
    PageLayout(isCompact = isCompact) {
        BlendedImage(
            imageRes = R.drawable.gemini_generated_image_3flk23flk23flk23,
            contentDescription = "All set illustration",
            isCompact = isCompact
        )
        Spacer(modifier = Modifier.height(if (isCompact) 20.dp else 28.dp))
        Text(
            text = "You're All Set! 🎉",
            color = Color.White,
            fontSize = if (isCompact) 26.sp else 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = "ReelBreak is ready to guard your focus. Scroll less. Live more.",
            color = Color.White.copy(alpha = 0.58f),
            fontSize = if (isCompact) 13.sp else 15.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(if (isCompact) 18.dp else 26.dp))
        listOf(
            Triple(Icons.Filled.Block,        Color(0xFFF87171), "Blocking engine ready"),
            Triple(Icons.Filled.Shield,       Color(0xFF8B5CF6), "Focus Mode configured"),
            Triple(Icons.Filled.Visibility,   Color(0xFF60A5FA), "Live overlay available"),
            Triple(Icons.Filled.Notifications,Color(0xFFFBBF24), "Stay mindful every day"),
        ).forEach { (icon, tint, label) ->
            ChecklistItem(icon = icon, iconColor = tint, text = label, isCompact = isCompact)
            Spacer(Modifier.height(8.dp))
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
//  SHARED COMPOSABLES
// ═══════════════════════════════════════════════════════════════════════════

// ── Responsive page container ────────────────────────────────────────────
@Composable
private fun PageLayout(isCompact: Boolean, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = if (isCompact) 8.dp else 16.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content
    )
}

// ── KEY CHANGE: BlendedImage replaces ImagePlaceholder ───────────────────
// No card, no box border. Image is drawn directly and fades into the
// dark background using a gradient scrim overlay + BlendMode.DstIn.
@Composable
private fun BlendedImage(
    imageRes: Int,
    contentDescription: String,
    isCompact: Boolean
) {
    val imageHeight = if (isCompact) 200.dp else 260.dp

    // Outer Box just provides the height slot — transparent, no background
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(imageHeight),
        contentAlignment = Alignment.Center
    ) {
        // 1️⃣  The actual image — fills the box, no clip, no card
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = contentDescription,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                // Slight scale-down so gradient edges never get cut
                .padding(horizontal = 12.dp)
        )

        // 2️⃣  Gradient scrim drawn ON TOP using BlendMode.DstIn
        //     This makes the image fade out at the edges and bottom,
        //     blending seamlessly into the dark background
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    // draw nothing — this is just the blend layer
                }
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        // Radial fade: image is bright in center, transparent at edges
                        drawRect(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color.Transparent,       // center = image shows fully
                                    Color.Black.copy(0.15f), // soft mid ring
                                    Color.Black.copy(0.60f), // edge fade
                                    Color.Black              // corners fully hidden
                                ),
                                center = Offset(size.width / 2f, size.height * 0.45f),
                                radius = size.width * 0.62f
                            ),
                            blendMode = BlendMode.DstIn
                        )
                    }
                }
        )

        // 3️⃣  Bottom-edge linear fade so it dissolves into the page below
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight * 0.45f)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xFF0D0618).copy(alpha = 0.55f),
                            Color(0xFF0D0618).copy(alpha = 0.90f),
                            Color(0xFF0D0618)
                        )
                    )
                )
        )
    }
}

// ── Page heading ─────────────────────────────────────────────────────────
@Composable
private fun PageHeading(title: String, subtitle: String, isCompact: Boolean) {
    Text(
        text = title,
        color = Color.White,
        fontSize = if (isCompact) 24.sp else 30.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        lineHeight = if (isCompact) 30.sp else 38.sp
    )
    Spacer(Modifier.height(8.dp))
    Text(
        text = subtitle,
        color = Color.White.copy(alpha = 0.55f),
        fontSize = if (isCompact) 13.sp else 14.sp,
        textAlign = TextAlign.Center,
        lineHeight = 21.sp
    )
}

// ── Feature pill ─────────────────────────────────────────────────────────
@Composable
private fun FeaturePill(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(Color(0xFF1C1233))
            .border(1.dp, Color(0xFF8B5CF6).copy(alpha = 0.40f), RoundedCornerShape(999.dp))
            .padding(horizontal = 12.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null,
            tint = Color(0xFFA78BFA), modifier = Modifier.size(13.dp))
        Text(text = text, color = Color.White.copy(alpha = 0.80f),
            fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

// ── Mode row ──────────────────────────────────────────────────────────────
@Composable
private fun ModeRow(icon: ImageVector, iconColor: Color, title: String, desc: String, isCompact: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF1C1233))
            .border(1.dp, Color(0xFF2D1B4E), RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = if (isCompact) 10.dp else 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(if (isCompact) 38.dp else 44.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(iconColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null,
                tint = iconColor, modifier = Modifier.size(if (isCompact) 18.dp else 22.dp))
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(text = title, color = Color.White,
                fontSize = if (isCompact) 13.sp else 14.sp, fontWeight = FontWeight.SemiBold)
            Text(text = desc, color = Color.White.copy(alpha = 0.52f),
                fontSize = if (isCompact) 11.sp else 12.sp, lineHeight = 17.sp)
        }
    }
}

// ── Info stat card ────────────────────────────────────────────────────────
@Composable
private fun InfoStatCard(
    icon: ImageVector, iconColor: Color, value: String,
    subtext: String, modifier: Modifier, isCompact: Boolean
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF1C1233))
            .border(1.dp, Color(0xFF2D1B4E), RoundedCornerShape(14.dp))
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(if (isCompact) 36.dp else 42.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null,
                tint = iconColor, modifier = Modifier.size(if (isCompact) 18.dp else 22.dp))
        }
        Text(text = value, color = Color.White,
            fontSize = if (isCompact) 12.sp else 13.sp,
            fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
        Text(text = subtext, color = Color.White.copy(alpha = 0.45f),
            fontSize = if (isCompact) 10.sp else 11.sp,
            textAlign = TextAlign.Center, lineHeight = 15.sp)
    }
}

// ── Tip box ───────────────────────────────────────────────────────────────
@Composable
private fun TipBox(text: String, isCompact: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1C1233))
            .border(1.dp, Color(0xFFFBBF24).copy(alpha = 0.25f), RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = if (isCompact) 10.dp else 12.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(imageVector = Icons.Filled.Lightbulb, contentDescription = null,
            tint = Color(0xFFFBBF24), modifier = Modifier.size(16.dp).padding(top = 1.dp))
        Text(text = text, color = Color.White.copy(alpha = 0.58f),
            fontSize = if (isCompact) 11.sp else 12.sp, lineHeight = 17.sp)
    }
}

// ── Permission row ────────────────────────────────────────────────────────
@Composable
private fun PermissionRow(
    icon: ImageVector, iconColor: Color, title: String, description: String,
    isGranted: Boolean, badge: String, badgeColor: Color,
    onGrant: () -> Unit, isCompact: Boolean
) {
    val borderColor by animateColorAsState(
        targetValue = if (isGranted) Color(0xFF34D399).copy(alpha = 0.45f) else Color(0xFF2D1B4E),
        animationSpec = tween(300), label = "permBorder"
    )
    val bgColor = if (isGranted) Color(0xFF0A1A10) else Color(0xFF1C1233)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = if (isCompact) 12.dp else 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(if (isCompact) 38.dp else 44.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(
                    if (isGranted) Color(0xFF34D399).copy(alpha = 0.15f)
                    else iconColor.copy(alpha = 0.15f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null,
                tint = if (isGranted) Color(0xFF4ADE80) else iconColor,
                modifier = Modifier.size(if (isCompact) 18.dp else 22.dp))
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(text = title, color = Color.White,
                    fontSize = if (isCompact) 12.sp else 13.sp, fontWeight = FontWeight.SemiBold)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(badgeColor.copy(alpha = 0.15f))
                        .padding(horizontal = 5.dp, vertical = 1.dp)
                ) {
                    Text(text = badge, color = badgeColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
            Text(text = description, color = Color.White.copy(alpha = 0.48f),
                fontSize = if (isCompact) 10.sp else 11.sp, lineHeight = 15.sp)
        }
        if (isGranted) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4ADE80).copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Filled.Check, contentDescription = "Granted",
                    tint = Color(0xFF4ADE80), modifier = Modifier.size(16.dp))
            }
        } else {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconColor.copy(alpha = 0.18f))
                    .border(1.dp, iconColor.copy(alpha = 0.45f), RoundedCornerShape(10.dp))
                    .clickable(
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                        indication = null, onClick = onGrant
                    )
                    .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
                Text(text = "Enable", color = iconColor,
                    fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

// ── Privacy note ──────────────────────────────────────────────────────────
@Composable
private fun PrivacyNote(isCompact: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0A1A10))
            .border(1.dp, Color(0xFF4ADE80).copy(alpha = 0.22f), RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = if (isCompact) 10.dp else 12.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(imageVector = Icons.Filled.Shield, contentDescription = null,
            tint = Color(0xFF4ADE80), modifier = Modifier.size(15.dp).padding(top = 1.dp))
        Text(
            text = "ReelBreak never reads your messages or personal content. Accessibility is used only to detect reels screens.",
            color = Color(0xFF4ADE80).copy(alpha = 0.78f),
            fontSize = if (isCompact) 10.sp else 11.sp,
            lineHeight = 16.sp
        )
    }
}

// ── Checklist item ────────────────────────────────────────────────────────
@Composable
private fun ChecklistItem(icon: ImageVector, iconColor: Color, text: String, isCompact: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1C1233))
            .border(1.dp, Color(0xFF2D1B4E), RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = if (isCompact) 10.dp else 13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(if (isCompact) 32.dp else 36.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(iconColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null,
                tint = iconColor, modifier = Modifier.size(if (isCompact) 16.dp else 18.dp))
        }
        Text(text = text, color = Color.White.copy(alpha = 0.82f),
            fontSize = if (isCompact) 13.sp else 14.sp,
            fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = null,
            tint = Color(0xFF4ADE80), modifier = Modifier.size(18.dp))
    }
}

// ── Pill page indicator ───────────────────────────────────────────────────
@Composable
private fun PillIndicator(currentPage: Int, totalPages: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        repeat(totalPages) { index ->
            val isActive = index == currentPage
            val width by animateDpAsState(
                targetValue = if (isActive) 28.dp else 6.dp,
                animationSpec = tween(300), label = "indicatorWidth"
            )
            val color by animateColorAsState(
                targetValue = if (isActive) Color(0xFF8B5CF6) else Color.White.copy(alpha = 0.22f),
                animationSpec = tween(300), label = "indicatorColor"
            )
            Box(
                modifier = Modifier
                    .height(6.dp)
                    .width(width)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

// ── CTA button ────────────────────────────────────────────────────────────
@Composable
private fun OnboardingCTAButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .shadow(
                elevation = 20.dp, shape = RoundedCornerShape(18.dp),
                spotColor = Color(0xFF8B5CF6), ambientColor = Color(0xFF8B5CF6)
            )
            .clip(RoundedCornerShape(18.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF7C3AED), Color(0xFF4C1D95))))
            .clickable(
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                indication = null, onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
    }
}

// ── Background canvas orbs ────────────────────────────────────────────────
private fun DrawScope.drawOrbBackground(offset: Float) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0xFF7C3AED).copy(alpha = 0.18f), Color.Transparent),
            center = Offset(size.width * 0.1f, size.height * 0.08f + offset * 0.04f),
            radius = size.width * 0.55f
        ),
        center = Offset(size.width * 0.1f, size.height * 0.08f + offset * 0.04f),
        radius = size.width * 0.55f
    )
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0xFF4C1D95).copy(alpha = 0.14f), Color.Transparent),
            center = Offset(size.width * 0.92f, size.height * 0.80f - offset * 0.05f),
            radius = size.width * 0.50f
        ),
        center = Offset(size.width * 0.92f, size.height * 0.80f - offset * 0.05f),
        radius = size.width * 0.50f
    )
}

// ── Helpers ───────────────────────────────────────────────────────────────
private fun isAccessibilityEnabled(context: android.content.Context): Boolean {
    val expectedComponent = ComponentName(context, ReelsAccessibilityService::class.java)
    val enabledServices = Settings.Secure.getString(
        context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
    ) ?: return false
    return enabledServices.split(":").any { entry ->
        ComponentName.unflattenFromString(entry) == expectedComponent
    }
}
