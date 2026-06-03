//package com.sangeeta.reelsbreak.ui.onboarding
//
//import androidx.compose.animation.animateColorAsState
//import androidx.compose.animation.core.*
//import androidx.compose.foundation.*
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.pager.HorizontalPager
//import androidx.compose.foundation.pager.rememberPagerState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.*
//import androidx.compose.ui.draw.*
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.*
//import androidx.compose.ui.graphics.drawscope.DrawScope
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.*
//import androidx.hilt.navigation.compose.hiltViewModel
//import com.sangeeta.reelsbreak.ui.onboarding.BlendedImage
//import com.sangeeta.reelsbreak.viewmodel.MainViewModel
//import kotlinx.coroutines.launch
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
//    val config = LocalConfiguration.current
//
//    val screenHeight = config.screenHeightDp.dp
//    val isCompact = screenHeight < 700.dp
//
//    val pages = OnboardingPageType.values().toList()
//    val pageState = rememberPagerState(initialPage = 0) { pages.size }
//    val isLastPage = pageState.currentPage == pages.lastIndex
//
//    val infiniteTransition = rememberInfiniteTransition(label = "orb")
//    val orbOffset by infiniteTransition.animateFloat(
//        initialValue = 0f, targetValue = 1f,
//        animationSpec = infiniteRepeatable(tween(6000, easing = LinearEasing), RepeatMode.Reverse),
//        label = "orbOff"
//    )
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                Brush.verticalGradient(listOf(Color(0xFF0D0618), Color(0xFF130A22), Color(0xFF0D0618)))
//            )
//    ) {
//        Canvas(modifier = Modifier.fillMaxSize()) { drawOrbBackground(orbOffset) }
//
//        Column(modifier = Modifier.fillMaxSize()) {
//            // ── Top Bar ──────────────────────────────────
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .statusBarsPadding()
//                    .padding(horizontal = 24.dp, vertical = 12.dp)
//            ) {
//                Text(
//                    text = "${pageState.currentPage + 1} / ${pages.size}",
//                    color = Color.White.copy(alpha = 0.35f),
//                    fontSize = 13.sp,
//                    fontWeight = FontWeight.Medium,
//                    modifier = Modifier.align(Alignment.CenterStart)
//                )
//                if (!isLastPage) {
//                    Box(
//                        modifier = Modifier
//                            .align(Alignment.CenterEnd)
//                            .clip(RoundedCornerShape(999.dp))
//                            .background(Color.White.copy(alpha = 0.08f))
//                            .clickable(
//                                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
//                                indication = null,
//                                onClick = onSkip
//                            )
//                            .padding(horizontal = 14.dp, vertical = 6.dp)
//                    ) {
//                        Text(
//                            "Skip",
//                            color = Color.White.copy(alpha = 0.55f),
//                            fontSize = 13.sp,
//                            fontWeight = FontWeight.Medium
//                        )
//                    }
//                }
//            }
//
//            // ── Pager System (Stripped of Perms Screen) ───
//            HorizontalPager(
//                state = pageState,
//                modifier = Modifier.weight(1f),
//                userScrollEnabled = true
//            ) { index ->
//                when (pages[index]) {
//                    OnboardingPageType.WELCOME -> WelcomePage(isCompact = isCompact)
//                    OnboardingPageType.HOWITWORKS -> HowItWorksPage(isCompact = isCompact)
//                    OnboardingPageType.LIMITSSETUP -> LimitsSetupPage(isCompact = isCompact)
//                    OnboardingPageType.ALLSET -> AllSetPage(isCompact = isCompact)
//                }
//            }
//
//            // ── Bottom Section ───────────────────────────
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .navigationBarsPadding()
//                    .padding(horizontal = 24.dp)
//                    .padding(bottom = 28.dp),
//                verticalArrangement = Arrangement.spacedBy(20.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                PillIndicator(currentPage = pageState.currentPage, totalPages = pages.size)
//                OnboardingCTAButton(
//                    text = if (isLastPage) "Start Using ReelBreak" else "Continue",
//                    onClick = {
//                        if (isLastPage) {
//                            coroutineScope.launch {
//                                mainViewModel.completeOnboarding()
//                                onComplete()
//                            }
//                        } else {
//                            coroutineScope.launch {
//                                pageState.animateScrollToPage(pageState.currentPage + 1)
//                            }
//                        }
//                    }
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun WelcomePage(isCompact: Boolean) {
//    PageLayout(isCompact = isCompact) {
//        BlendedImage(
//            imageRes = com.sangeeta.reelsbreak.R.drawable.gemini_generated_image_quxe1oquxe1oquxe ,
//            contentDescription = "Welcome illustration",
//            isCompact = isCompact
//        )
//        Spacer(modifier = Modifier.height(if (isCompact) 20.dp else 32.dp))
//        Text(
//            text = "Take Back Scroll",
//            color = Color.White,
//            fontSize = if (isCompact) 28.sp else 34.sp,
//            fontWeight = FontWeight.Bold,
//            textAlign = TextAlign.Center,
//            lineHeight = if (isCompact) 34.sp else 42.sp
//        )
//        Spacer(modifier = Modifier.height(12.dp))
//        Text(
//            text = "ReelBreak blocks short-form videos before they steal hours of your day.",
//            color = Color.White.copy(alpha = 0.58f),
//            fontSize = if (isCompact) 13.sp else 15.sp,
//            textAlign = TextAlign.Center,
//            lineHeight = 22.sp,
//            modifier = Modifier.padding(horizontal = 8.dp)
//        )
//        Spacer(modifier = Modifier.height(if (isCompact) 20.dp else 28.dp))
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            FeaturePill(icon = Icons.Filled.Block, text = "Block")
//            Spacer(Modifier.width(8.dp))
//            FeaturePill(icon = Icons.Filled.Timer, text = "Limit")
//            Spacer(Modifier.width(8.dp))
//            FeaturePill(icon = Icons.Filled.Shield, text = "Focus")
//        }
//    }
//}
//
//@Composable
//private fun HowItWorksPage(isCompact: Boolean) {
//    PageLayout(isCompact = isCompact) {
//        BlendedImage(
//            imageRes =  com.sangeeta.reelsbreak.R.drawable.gemini_generated_image_uhy2ueuhy2ueuhy2__1_,
//            contentDescription = "How it works illustration",
//            isCompact = isCompact
//        )
//        Spacer(modifier = Modifier.height(if (isCompact) 20.dp else 28.dp))
//        PageHeading(
//            title = "Three Ways to Stay in Control",
//            subtitle = "Pick the mode that fits your goals",
//            isCompact = isCompact
//        )
//        Spacer(modifier = Modifier.height(if (isCompact) 16.dp else 22.dp))
//        ModeRow(
//            icon = Icons.Filled.Block, iconColor = Color(0xFFF87171),
//            title = "Block Instantly",
//            desc = "Every reel attempt is closed the moment you open it.",
//            isCompact = isCompact
//        )
//        Spacer(Modifier.height(10.dp))
//        ModeRow(
//            icon = Icons.Filled.HourglassEmpty, iconColor = Color(0xFF60A5FA),
//            title = "Set a Daily Limit",
//            desc = "Watch a set number of reels or minutes, then auto-block.",
//            isCompact = isCompact
//        )
//        Spacer(Modifier.height(10.dp))
//        ModeRow(
//            icon = Icons.Filled.Lock, iconColor = Color(0xFF34D399),
//            title = "Focus Session",
//            desc = "Lock yourself out for a timed session — from 15 min to 5 days.",
//            isCompact = isCompact
//        )
//    }
//}
//
//@Composable
//private fun LimitsSetupPage(isCompact: Boolean) {
//    PageLayout(isCompact = isCompact) {
//        BlendedImage(
//            imageRes =  com.sangeeta.reelsbreak.R.drawable.gemini_generated_image_bp6w86bp6w86bp6w,
//            contentDescription = "Limits illustration",
//            isCompact = isCompact
//        )
//        Spacer(modifier = Modifier.height(if (isCompact) 20.dp else 28.dp))
//        PageHeading(
//            title = "Your Rules, Your Schedule",
//            subtitle = "Customize daily limits that fit your life — change them anytime.",
//            isCompact = isCompact
//        )
//        Spacer(modifier = Modifier.height(if (isCompact) 16.dp else 22.dp))
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(10.dp)
//        ) {
//            InfoStatCard(
//                icon = Icons.Filled.Videocam, iconColor = Color(0xFF8B5CF6),
//                value = "Reel Count", subtext = "Limit total reels/day",
//                modifier = Modifier.weight(1f), isCompact = isCompact
//            )
//            InfoStatCard(
//                icon = Icons.Filled.AccessTime, iconColor = Color(0xFF60A5FA),
//                value = "Time Limit", subtext = "Limit minutes/day",
//                modifier = Modifier.weight(1f), isCompact = isCompact
//            )
//        }
//        Spacer(Modifier.height(12.dp))
//        TipBox(
//            text = "Set limits from the Dashboard once you're inside the app. You can change them any time.",
//            isCompact = isCompact
//        )
//    }
//}
//
//@Composable
//private fun AllSetPage(isCompact: Boolean) {
//    PageLayout(isCompact = isCompact) {
//        BlendedImage(
//            imageRes =  com.sangeeta.reelsbreak.R.drawable.gemini_generated_image_3flk23flk23flk23,
//            contentDescription = "All set illustration",
//            isCompact = isCompact
//        )
//        Spacer(modifier = Modifier.height(if (isCompact) 20.dp else 28.dp))
//        Text(
//            text = "You're All Set! 🎉",
//            color = Color.White,
//            fontSize = if (isCompact) 26.sp else 32.sp,
//            fontWeight = FontWeight.Bold,
//            textAlign = TextAlign.Center
//        )
//        Spacer(Modifier.height(10.dp))
//        Text(
//            text = "ReelBreak is ready to guard your focus. Scroll less. Live more.",
//            color = Color.White.copy(alpha = 0.58f),
//            fontSize = if (isCompact) 13.sp else 15.sp,
//            textAlign = TextAlign.Center,
//            lineHeight = 22.sp,
//            modifier = Modifier.padding(horizontal = 8.dp)
//        )
//        Spacer(modifier = Modifier.height(if (isCompact) 18.dp else 26.dp))
//        listOf(
//            Triple(Icons.Filled.Block,         Color(0xFFF87171), "Blocking engine ready"),
//            Triple(Icons.Filled.Shield,        Color(0xFF8B5CF6), "Focus Mode configured"),
//            Triple(Icons.Filled.Notifications, Color(0xFFFBBF24), "Stay mindful every day"),
//        ).forEach { (icon, tint, label) ->
//            ChecklistItem(icon = icon, iconColor = tint, text = label, isCompact = isCompact)
//            Spacer(Modifier.height(8.dp))
//        }
//    }
//}
//
//// ─── BOTTOM RENDER ATTACHMENTS ──────────────────────────────────────────────
//
//@Composable
//private fun PageLayout(isCompact: Boolean, content: @Composable ColumnScope.() -> Unit) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//            .padding(horizontal = 24.dp)
//            .padding(top = if (isCompact) 8.dp else 16.dp, bottom = 8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        content = content
//    )
//}
//
//@Composable
//private fun BlendedImage(imageRes: Int, contentDescription: String, isCompact: Boolean) {
//    val imageHeight = if (isCompact) 200.dp else 260.dp
//    Box(modifier = Modifier.fillMaxWidth().height(imageHeight), contentAlignment = Alignment.Center) {
//        Image(
//            painter = painterResource(id = imageRes),
//            contentDescription = contentDescription,
//            contentScale = ContentScale.Fit,
//            modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp)
//        )
//        Spacer(
//            modifier = Modifier
//                .fillMaxSize()
//                .drawWithContent { }
//                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
//                .drawWithCache {
//                    onDrawWithContent {
//                        drawContent()
//                        drawRect(
//                            brush = Brush.radialGradient(
//                                colors = listOf(Color.Transparent, Color.Black.copy(0.15f), Color.Black.copy(0.60f), Color.Black),
//                                center = Offset(size.width / 2f, size.height * 0.45f),
//                                radius = size.width * 0.62f
//                            ),
//                            blendMode = BlendMode.DstIn
//                        )
//                    }
//                }
//        )
//        Spacer(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(imageHeight * 0.45f)
//                .align(Alignment.BottomCenter)
//                .background(
//                    Brush.verticalGradient(
//                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.55f), Color.Black.copy(alpha = 0.90f), Color.Black)
//                    )
//                )
//        )
//    }
//}
//
//@Composable
//private fun PageHeading(title: String, subtitle: String, isCompact: Boolean) {
//    Text(text = title, color = Color.White, fontSize = if (isCompact) 24.sp else 30.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, lineHeight = if (isCompact) 30.sp else 38.sp)
//    Spacer(Modifier.height(8.dp))
//    Text(text = subtitle, color = Color.White.copy(alpha = 0.55f), fontSize = if (isCompact) 13.sp else 14.sp, textAlign = TextAlign.Center, lineHeight = 21.sp)
//}
//
//@Composable
//private fun FeaturePill(icon: ImageVector, text: String) {
//    Row(modifier = Modifier.clip(RoundedCornerShape(999.dp)).background(Color(0xFF1C1233)).border(1.dp, Color(0xFF8B5CF6).copy(alpha = 0.40f), RoundedCornerShape(999.dp)).padding(horizontal = 12.dp, vertical = 7.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
//        Icon(imageVector = icon, contentDescription = null, tint = Color(0xFFA78BFA), modifier = Modifier.size(13.dp))
//        Text(text = text, color = Color.White.copy(alpha = 0.80f), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
//    }
//}
//
//@Composable
//private fun ModeRow(icon: ImageVector, iconColor: Color, title: String, desc: String, isCompact: Boolean) {
//    Row(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Color(0xFF1C1233)).border(1.dp, Color(0xFF2D1B4E), RoundedCornerShape(14.dp)).padding(horizontal = 14.dp, vertical = if (isCompact) 10.dp else 14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
//        Box(modifier = Modifier.size(if (isCompact) 38.dp else 44.dp).clip(RoundedCornerShape(11.dp)).background(iconColor.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
//            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(if (isCompact) 18.dp else 22.dp))
//        }
//        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
//            Text(text = title, color = Color.White, fontSize = if (isCompact) 13.sp else 14.sp, fontWeight = FontWeight.SemiBold)
//            Text(text = desc, color = Color.White.copy(alpha = 0.52f), fontSize = if (isCompact) 11.sp else 12.sp, lineHeight = 17.sp)
//        }
//    }
//}
//
//@Composable
//private fun InfoStatCard(icon: ImageVector, iconColor: Color, value: String, subtext: String, modifier: Modifier, isCompact: Boolean) {
//    Column(modifier = modifier.clip(RoundedCornerShape(14.dp)).background(Color(0xFF1C1233)).border(1.dp, Color(0xFF2D1B4E), RoundedCornerShape(14.dp)).padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
//        Box(modifier = Modifier.size(if (isCompact) 36.dp else 42.dp).clip(RoundedCornerShape(10.dp)).background(iconColor.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
//            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(if (isCompact) 18.dp else 22.dp))
//        }
//        Text(text = value, color = Color.White, fontSize = if (isCompact) 12.sp else 13.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
//        Text(text = subtext, color = Color.White.copy(alpha = 0.45f), fontSize = if (isCompact) 10.sp else 11.sp, textAlign = TextAlign.Center, lineHeight = 15.sp)
//    }
//}
//
//@Composable
//private fun TipBox(text: String, isCompact: Boolean) {
//    Row(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0xFF1C1233)).border(1.dp, Color(0xFFFBBF24).copy(alpha = 0.25f), RoundedCornerShape(12.dp)).padding(horizontal = 14.dp, vertical = if (isCompact) 10.dp else 12.dp), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
//        Icon(imageVector = Icons.Filled.Lightbulb, contentDescription = null, tint = Color(0xFFFBBF24), modifier = Modifier.size(16.dp).padding(top = 1.dp))
//        Text(text = text, color = Color.White.copy(alpha = 0.58f), fontSize = if (isCompact) 11.sp else 12.sp, lineHeight = 17.sp)
//    }
//}
//
//@Composable
//private fun ChecklistItem(icon: ImageVector, iconColor: Color, text: String, isCompact: Boolean) {
//    Row(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0xFF1C1233)).border(1.dp, Color(0xFF2D1B4E), RoundedCornerShape(12.dp)).padding(horizontal = 14.dp, vertical = if (isCompact) 10.dp else 13.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
//        Box(modifier = Modifier.size(if (isCompact) 32.dp else 36.dp).clip(RoundedCornerShape(9.dp)).background(iconColor.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
//            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(if (isCompact) 16.dp else 18.dp))
//        }
//        Text(text = text, color = Color.White.copy(alpha = 0.82f), fontSize = if (isCompact) 13.sp else 14.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
//        Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = null, tint = Color(0xFF4ADE80), modifier = Modifier.size(18.dp)
//        )
//    }
//}
//
//@Composable
//private fun PillIndicator(currentPage: Int, totalPages: Int) {
//    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
//        repeat(totalPages) { index ->
//            val isActive = index == currentPage
//            val width by animateDpAsState(targetValue = if (isActive) 28.dp else 6.dp, animationSpec = tween(300), label = "indicatorWidth")
//            val color by animateColorAsState(targetValue = if (isActive) Color(0xFF8B5CF6) else Color.White.copy(alpha = 0.22f), animationSpec = tween(300), label = "indicatorColor")
//            Box(modifier = Modifier.height(6.dp).width(width).clip(CircleShape).background(color))
//        }
//    }
//}
//
//@Composable
//private fun OnboardingCTAButton(text: String, onClick: () -> Unit) {
//    Box(
//        modifier = Modifier.fillMaxWidth().height(54.dp).shadow(elevation = 20.dp, shape = RoundedCornerShape(18.dp), spotColor = Color(0xFF8B5CF6), ambientColor = Color(0xFF8B5CF6)).clip(RoundedCornerShape(18.dp)).background(Brush.linearGradient(listOf(Color(0xFF7C3AED), Color(0xFF4C1D95)))).clickable(
//            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }, indication = null, onClick = onClick
//        ), contentAlignment = Alignment.Center
//    ) {
//        Text(text = text, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
//    }
//}
//
//private fun DrawScope.drawOrbBackground(offset: Float) {
//    drawCircle(brush = Brush.radialGradient(colors = listOf(Color(0xFF7C3AED).copy(alpha = 0.18f), Color.Transparent), center = Offset(size.width * 0.1f, size.height * 0.08f + offset * 0.04f), radius = size.width * 0.55f), center = Offset(size.width * 0.1f, size.height * 0.08f + offset * 0.04f), radius = size.width * 0.55f)
//    drawCircle(brush = Brush.radialGradient(colors = listOf(Color(0xFF4C1D95).copy(alpha = 0.14f), Color.Transparent), center = Offset(size.width * 0.92f, size.height * 0.80f - offset * 0.05f), radius = size.width * 0.50f), center = Offset(size.width * 0.92f, size.height * 0.80f - offset * 0.05f), radius = size.width * 0.50f)
//}





package com.sangeeta.reelsbreak.ui.onboarding

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.hilt.navigation.compose.hiltViewModel
import com.sangeeta.reelsbreak.viewmodel.MainViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// ─── Onboarding Prefs ────────────────────────────────────────────────────────

private val Context.onboardingDataStore: DataStore<Preferences> by preferencesDataStore(name = "onboarding_prefs")

object OnboardingPreferences {
    private val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")

    fun isOnboardingCompleted(context: Context): Flow<Boolean> =
        context.onboardingDataStore.data.map { prefs -> prefs[ONBOARDING_COMPLETED] ?: false }

    suspend fun setOnboardingCompleted(context: Context) {
        context.onboardingDataStore.edit { prefs -> prefs[ONBOARDING_COMPLETED] = true }
    }
}

// ─── Page Types ──────────────────────────────────────────────────────────────

private enum class OnboardingPageType {
    HERO,
    PROTECTION_MODES,
    FOCUS_MODE,
    ACCESSIBILITY_INFO,
    PROGRESS_AND_OVERLAY
}

// ─── Color palette (local to onboarding, no LocalAppColors dependency) ───────

private val OB_BG_TOP        = Color(0xFF0A0415)
private val OB_BG_MID        = Color(0xFF0F0820)
private val OB_BG_BOT        = Color(0xFF0A0415)
private val OB_CARD          = Color(0xFF150D2A)
private val OB_CARD_BORDER   = Color(0xFF241640)
private val OB_PURPLE        = Color(0xFF8B5CF6)
private val OB_PURPLE_DEEP   = Color(0xFF4C1D95)
private val OB_PURPLE_SOFT   = Color(0xFFA78BFA)
private val OB_BLUE          = Color(0xFF60A5FA)
private val OB_GREEN         = Color(0xFF34D399)
private val OB_AMBER         = Color(0xFFFBBF24)
private val OB_RED           = Color(0xFFF87171)
private val OB_TEXT          = Color.White
private val OB_TEXT_SUB      = Color(0xFFB8B0CC)
private val OB_TEXT_MUTED    = Color(0xFF7A7090)

private val OB_GRADIENT_BG = Brush.verticalGradient(listOf(OB_BG_TOP, OB_BG_MID, OB_BG_BOT))
private val OB_GRADIENT_CTA = Brush.linearGradient(listOf(Color(0xFF7C3AED), Color(0xFF4C1D95)))

// ─── Main Onboarding Screen ──────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    onSkip: () -> Unit,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val config = LocalConfiguration.current
    val isCompact = config.screenHeightDp.dp < 700.dp

    val pages = OnboardingPageType.entries
    val pageState = rememberPagerState(initialPage = 0) { pages.size }
    val isLastPage = pageState.currentPage == pages.lastIndex

    // Ambient floating orb animation
    val infiniteTransition = rememberInfiniteTransition(label = "obAmbient")
    val orbDrift by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(7000, easing = LinearEasing), RepeatMode.Reverse),
        label = "orbDrift"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OB_GRADIENT_BG)
    ) {

        // Ambient orb canvas layer
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawAmbientOrbs(orbDrift)
        }

        Column(modifier = Modifier.fillMaxSize()) {

            // ── Top bar ──────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 14.dp)
            ) {
                // Page counter
                Text(
                    text = "${pageState.currentPage + 1} / ${pages.size}",
                    color = OB_TEXT_MUTED,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                // Skip button — hidden on last page
                androidx.compose.animation.AnimatedVisibility(
                    visible = !isLastPage,
                    modifier = Modifier.align(Alignment.CenterEnd),
                    enter = fadeIn(tween(200)),
                    exit = fadeOut(tween(200))
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(Color.White.copy(alpha = 0.07f))
                            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(999.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {
                                    coroutineScope.launch {
                                        mainViewModel.completeOnboarding()
                                        onSkip()
                                    }
                                }
                            )
                            .padding(horizontal = 16.dp, vertical = 7.dp)
                    ) {
                        Text(
                            "Skip",
                            color = OB_TEXT_SUB,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // ── HorizontalPager ───────────────────────────────────────────────
            HorizontalPager(
                state = pageState,
                modifier = Modifier.weight(1f),
                userScrollEnabled = true
            ) { index ->
                when (pages[index]) {
                    OnboardingPageType.HERO               -> HeroPage(isCompact, pageState, index)
                    OnboardingPageType.PROTECTION_MODES   -> ProtectionModesPage(isCompact, pageState, index)
                    OnboardingPageType.FOCUS_MODE         -> FocusModePage(isCompact, pageState, index)
                    OnboardingPageType.ACCESSIBILITY_INFO -> AccessibilityInfoPage(isCompact, pageState, index)
                    OnboardingPageType.PROGRESS_AND_OVERLAY -> ProgressAndOverlayPage(isCompact, pageState, index)
                }
            }

            // ── Bottom bar ───────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 28.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ObPillIndicator(currentPage = pageState.currentPage, totalPages = pages.size)
                ObCTAButton(
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

// ─────────────────────────────────────────────────────────────────────────────
// PAGE 1 — HERO
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun HeroPage(isCompact: Boolean, pageState: PagerState, pageIndex: Int) {
    val visible = pageState.currentPage == pageIndex
    val entered = remember { mutableStateOf(false) }
    LaunchedEffect(visible) { if (visible) entered.value = true }

    val infiniteTransition = rememberInfiniteTransition(label = "hero")

    // Floating shield pulse
    val shieldScale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.07f,
        animationSpec = infiniteRepeatable(tween(2200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "shieldScale"
    )
    // Ring rotation
    val ringAngle by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(12000, easing = LinearEasing)),
        label = "ringAngle"
    )
    // Glow pulse
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f, targetValue = 0.55f,
        animationSpec = infiniteRepeatable(tween(2800, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "glowAlpha"
    )

    ObPageLayout(isCompact) {
        Spacer(Modifier.height(if (isCompact) 8.dp else 20.dp))

        // Hero illustration — animated shield with orbit ring
        Box(
            modifier = Modifier
                .size(if (isCompact) 180.dp else 220.dp),
            contentAlignment = Alignment.Center
        ) {
            // Glow halo
            Box(
                modifier = Modifier
                    .size(if (isCompact) 180.dp else 220.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                OB_PURPLE.copy(alpha = glowAlpha),
                                Color.Transparent
                            )
                        ),
                        CircleShape
                    )
            )

            // Orbit ring
            Canvas(
                modifier = Modifier
                    .size(if (isCompact) 155.dp else 190.dp)
                    .rotate(ringAngle)
            ) {
                val stroke = Stroke(width = 1.5.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f)))
                drawCircle(
                    color = OB_PURPLE.copy(alpha = 0.35f),
                    radius = size.minDimension / 2f,
                    style = stroke
                )
                // 3 orbit dots
                val r = size.minDimension / 2f
                val dotAngles = listOf(0f, 120f, 240f)
                dotAngles.forEach { angle ->
                    val rad = Math.toRadians(angle.toDouble())
                    val cx = (size.width / 2f + r * Math.cos(rad)).toFloat()
                    val cy = (size.height / 2f + r * Math.sin(rad)).toFloat()
                    drawCircle(color = OB_PURPLE_SOFT, radius = 4.dp.toPx(), center = Offset(cx, cy))
                }
            }

            // Inner shield icon
            Box(
                modifier = Modifier
                    .size(if (isCompact) 100.dp else 120.dp)
                    .scale(shieldScale)
                    .background(
                        Brush.radialGradient(
                            listOf(OB_PURPLE.copy(0.25f), OB_PURPLE_DEEP.copy(0.45f))
                        ),
                        CircleShape
                    )
                    .border(1.5.dp, OB_PURPLE.copy(0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Shield,
                    contentDescription = null,
                    tint = OB_PURPLE_SOFT,
                    modifier = Modifier.size(if (isCompact) 46.dp else 56.dp)
                )
            }

            // Small floating chips — Block / Limit / Focus
            ObFloatingChip(
                text = "Block",
                color = OB_RED,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = 4.dp, y = 24.dp)
            )
            ObFloatingChip(
                text = "Limit",
                color = OB_BLUE,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-4).dp, y = 28.dp)
            )
            ObFloatingChip(
                text = "Focus",
                color = OB_GREEN,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (-8).dp)
            )
        }

        Spacer(Modifier.height(if (isCompact) 24.dp else 36.dp))

        // Headline
        ObAnimatedContent(visible = entered.value, delayMs = 0) {
            Text(
                text = "Break the scroll loop",
                color = OB_TEXT,
                fontSize = if (isCompact) 28.sp else 34.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = if (isCompact) 34.sp else 42.sp,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        Spacer(Modifier.height(12.dp))

        ObAnimatedContent(visible = entered.value, delayMs = 80) {
            Text(
                text = "ReelBreak detects and blocks short-form video feeds before they consume your time. Choose how — and take control back.",
                color = OB_TEXT_SUB,
                fontSize = if (isCompact) 13.sp else 15.sp,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        Spacer(Modifier.height(if (isCompact) 20.dp else 28.dp))

        // Supported apps row
        ObAnimatedContent(visible = entered.value, delayMs = 160) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "WORKS WITH",
                    color = OB_TEXT_MUTED,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.4.sp
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf("YouTube", "Instagram", "Snapchat", "Facebook", "+ more").forEach { app ->
                        ObAppBadge(text = app)
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// PAGE 2 — PROTECTION MODES
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ProtectionModesPage(isCompact: Boolean, pageState: PagerState, pageIndex: Int) {
    val visible = pageState.currentPage == pageIndex
    val entered = remember { mutableStateOf(false) }
    LaunchedEffect(visible) { if (visible) entered.value = true }

    val modes = listOf(
        Triple(Icons.Outlined.Block,         OB_RED,   Pair("Block Instantly",   "Every reel is closed the moment it opens. No exceptions.")),
        Triple(Icons.Outlined.HourglassEmpty,OB_BLUE,  Pair("Set Mindful Limits","Watch up to a set count or daily minutes, then auto-block.")),
        Triple(Icons.Outlined.PauseCircle,   OB_AMBER, Pair("Pause Protection",  "Temporarily relax blocking without losing your saved setup."))
    )

    ObPageLayout(isCompact) {
        Spacer(Modifier.height(if (isCompact) 4.dp else 12.dp))

        // Animated mode preview card (top illustration)
        ObModeIllustration(isCompact)

        Spacer(Modifier.height(if (isCompact) 18.dp else 26.dp))

        ObAnimatedContent(visible = entered.value, delayMs = 0) {
            ObPageHeading(
                title = "Three ways to protect",
                subtitle = "Switch modes anytime from your dashboard",
                isCompact = isCompact
            )
        }

        Spacer(Modifier.height(if (isCompact) 16.dp else 22.dp))

        modes.forEachIndexed { i, (icon, color, texts) ->
            ObAnimatedContent(visible = entered.value, delayMs = i * 80) {
                ObModeCard(
                    icon = icon,
                    iconColor = color,
                    title = texts.first,
                    description = texts.second,
                    isCompact = isCompact
                )
            }
            if (i < modes.lastIndex) Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
private fun ObModeIllustration(isCompact: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "modeIllu")
    val activeIndex by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 2.99f,
        animationSpec = infiniteRepeatable(tween(3600, easing = LinearEasing), RepeatMode.Restart),
        label = "modeActive"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f, targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "modeAlpha"
    )

    val activeIdx = activeIndex.toInt().coerceIn(0, 2)
    val modeConfigs = listOf(
        Triple(Icons.Outlined.Block,          OB_RED,   "Block"),
        Triple(Icons.Outlined.HourglassEmpty, OB_BLUE,  "Limit"),
        Triple(Icons.Outlined.PauseCircle,    OB_AMBER, "Pause")
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(OB_CARD)
            .border(1.dp, OB_CARD_BORDER, RoundedCornerShape(20.dp))
            .padding(horizontal = 20.dp, vertical = if (isCompact) 14.dp else 18.dp)
    ) {
        modeConfigs.forEachIndexed { i, (icon, color, label) ->
            val isActive = i == activeIdx
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isActive) color.copy(alpha = 0.15f) else Color.Transparent)
                    .border(
                        1.dp,
                        if (isActive) color.copy(alpha = pulseAlpha * 0.7f) else OB_CARD_BORDER,
                        RoundedCornerShape(14.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isActive) color else OB_TEXT_MUTED,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = label,
                    color = if (isActive) color else OB_TEXT_MUTED,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// PAGE 3 — FOCUS MODE
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun FocusModePage(isCompact: Boolean, pageState: PagerState, pageIndex: Int) {
    val visible = pageState.currentPage == pageIndex
    val entered = remember { mutableStateOf(false) }
    LaunchedEffect(visible) { if (visible) entered.value = true }

    val focusApps = listOf(
        Pair("YouTube",   OB_RED),
        Pair("Instagram", OB_PURPLE_SOFT),
        Pair("Facebook",  OB_BLUE),
        Pair("Snapchat",  OB_AMBER),
        Pair("TikTok",    Color(0xFF2DD4BF)),
        Pair("X",         OB_TEXT_SUB),
        Pair("ShareChat", Color(0xFF4ADE80)),
        Pair("Moj",       Color(0xFFF472B6))
    )

    ObPageLayout(isCompact) {
        Spacer(Modifier.height(if (isCompact) 4.dp else 12.dp))

        // Focus session illustration
        ObFocusTimerIllustration(isCompact)

        Spacer(Modifier.height(if (isCompact) 18.dp else 26.dp))

        ObAnimatedContent(visible = entered.value, delayMs = 0) {
            ObPageHeading(
                title = "Focus Mode",
                subtitle = "Start a timed session and block entire distracting apps — not just reels",
                isCompact = isCompact
            )
        }

        Spacer(Modifier.height(if (isCompact) 16.dp else 20.dp))

        // App grid
        ObAnimatedContent(visible = entered.value, delayMs = 80) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(OB_CARD)
                    .border(1.dp, OB_CARD_BORDER, RoundedCornerShape(16.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Supported apps",
                    color = OB_TEXT_MUTED,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.2.sp
                )
                FlowRowApps(apps = focusApps)
            }
        }

        Spacer(Modifier.height(10.dp))

        ObAnimatedContent(visible = entered.value, delayMs = 160) {
            ObInfoRow(
                icon = Icons.Outlined.Timer,
                iconColor = OB_GREEN,
                text = "Sessions can run from a few minutes to multiple days. Apps stay locked until the timer ends."
            )
        }
    }
}

@Composable
private fun ObFocusTimerIllustration(isCompact: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "focusTimer")
    val sweep by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(6000, easing = LinearEasing)),
        label = "timerSweep"
    )
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 0.7f,
        animationSpec = infiniteRepeatable(tween(1400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "timerGlow"
    )

    val size = if (isCompact) 120.dp else 150.dp

    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = Stroke(
                width = 6.dp.toPx(),
                cap = StrokeCap.Round
            )
            val trackStroke = Stroke(width = 6.dp.toPx())
            // Track
            drawCircle(
                color = OB_PURPLE.copy(alpha = 0.18f),
                radius = this.size.minDimension / 2f - 3.dp.toPx(),
                style = trackStroke
            )
            // Progress arc
            drawArc(
                color = OB_PURPLE.copy(alpha = 0.9f),
                startAngle = -90f,
                sweepAngle = sweep * 0.78f,
                useCenter = false,
                style = stroke
            )
        }

        // Glow
        Box(
            modifier = Modifier
                .size(size * 0.6f)
                .background(
                    Brush.radialGradient(listOf(OB_PURPLE.copy(glowPulse), Color.Transparent)),
                    CircleShape
                )
        )

        Icon(
            imageVector = Icons.Outlined.Timer,
            contentDescription = null,
            tint = OB_PURPLE_SOFT,
            modifier = Modifier.size(if (isCompact) 36.dp else 46.dp)
        )
    }
}

@Composable
private fun FlowRowApps(apps: List<Pair<String, Color>>) {
    val rows = apps.chunked(4)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        rows.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { (name, color) ->
                    ObAppChip(name = name, color = color)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// PAGE 4 — ACCESSIBILITY INFO
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun AccessibilityInfoPage(isCompact: Boolean, pageState: PagerState, pageIndex: Int) {
    val visible = pageState.currentPage == pageIndex
    val entered = remember { mutableStateOf(false) }
    LaunchedEffect(visible) { if (visible) entered.value = true }

    val infiniteTransition = rememberInfiniteTransition(label = "accessAnim")
    val scanY by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2600, easing = LinearEasing), RepeatMode.Reverse),
        label = "scanY"
    )

    val does = listOf(
        Pair(Icons.Outlined.Visibility,   "Detects when supported short-video screens appear"),
        Pair(Icons.Outlined.Shield,       "Applies your chosen protection mode automatically"),
        Pair(Icons.Outlined.PhoneAndroid, "Works across YouTube, Instagram, Snapchat & more")
    )
    val doesNot = listOf(
        Pair(Icons.Outlined.LockPerson,  "Does NOT read messages, passwords, or private text"),
        Pair(Icons.Outlined.CloudOff,    "Does NOT send data to any server"),
        Pair(Icons.Outlined.PersonOff,   "Does NOT collect personal information")
    )

    ObPageLayout(isCompact) {
        Spacer(Modifier.height(if (isCompact) 4.dp else 12.dp))

        // Phone scan illustration
        ObPhoneScanIllustration(scanY, isCompact)

        Spacer(Modifier.height(if (isCompact) 18.dp else 26.dp))

        ObAnimatedContent(visible = entered.value, delayMs = 0) {
            ObPageHeading(
                title = "Accessibility access",
                subtitle = "ReelBreak needs Android Accessibility access to detect short-video screens. Here is exactly what that means.",
                isCompact = isCompact
            )
        }

        Spacer(Modifier.height(if (isCompact) 14.dp else 20.dp))

        // What it DOES
        ObAnimatedContent(visible = entered.value, delayMs = 80) {
            ObListCard(
                label = "WHAT IT DOES",
                labelColor = OB_GREEN,
                items = does,
                cardBorderColor = OB_GREEN.copy(alpha = 0.20f),
                isCompact = isCompact
            )
        }

        Spacer(Modifier.height(10.dp))

        // What it DOES NOT
        ObAnimatedContent(visible = entered.value, delayMs = 160) {
            ObListCard(
                label = "WHAT IT DOES NOT DO",
                labelColor = OB_RED,
                items = doesNot,
                cardBorderColor = OB_RED.copy(alpha = 0.18f),
                isCompact = isCompact
            )
        }

        Spacer(Modifier.height(10.dp))

        ObAnimatedContent(visible = entered.value, delayMs = 240) {
            ObInfoRow(
                icon = Icons.Outlined.Info,
                iconColor = OB_AMBER,
                text = "You can enable or disable Accessibility access at any time from your Android settings."
            )
        }
    }
}

@Composable
private fun ObPhoneScanIllustration(scanY: Float, isCompact: Boolean) {
    val height = if (isCompact) 110.dp else 140.dp
    val width  = if (isCompact) 66.dp else 84.dp

    Box(
        modifier = Modifier.size(width = width, height = height),
        contentAlignment = Alignment.Center
    ) {
        // Phone frame
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cornerRadius = 18.dp.toPx()
            val borderWidth  = 2.dp.toPx()
            drawRoundRect(
                color = OB_PURPLE.copy(alpha = 0.5f),
                size = this.size,
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius),
                style = Stroke(width = borderWidth)
            )
            // Screen fill
            drawRoundRect(
                color = OB_CARD,
                size = this.size,
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius)
            )
            // Scan line
            val lineY = (scanY * this.size.height * 0.8f) + this.size.height * 0.1f
            drawLine(
                color = OB_GREEN.copy(alpha = 0.7f),
                start = Offset(8.dp.toPx(), lineY),
                end   = Offset(this.size.width - 8.dp.toPx(), lineY),
                strokeWidth = 1.5.dp.toPx()
            )
            // Tiny content lines
            val lineColor = OB_TEXT_MUTED.copy(alpha = 0.2f)
            listOf(0.25f, 0.42f, 0.59f, 0.76f).forEach { frac ->
                drawLine(
                    color = lineColor,
                    start = Offset(12.dp.toPx(), this.size.height * frac),
                    end   = Offset(this.size.width - 12.dp.toPx(), this.size.height * frac),
                    strokeWidth = 1.dp.toPx()
                )
            }
        }

        Icon(
            imageVector = Icons.Outlined.AccessibilityNew,
            contentDescription = null,
            tint = OB_PURPLE.copy(alpha = 0.45f),
            modifier = Modifier.size(if (isCompact) 24.dp else 30.dp)
        )
    }
}

@Composable
private fun ObListCard(
    label: String,
    labelColor: Color,
    items: List<Pair<ImageVector, String>>,
    cardBorderColor: Color,
    isCompact: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(OB_CARD)
            .border(1.dp, cardBorderColor, RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(if (isCompact) 10.dp else 12.dp)
    ) {
        Text(
            text = label,
            color = labelColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.4.sp
        )
        items.forEach { (icon, text) ->
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = labelColor,
                    modifier = Modifier
                        .size(15.dp)
                        .padding(top = 1.dp)
                )
                Text(
                    text = text,
                    color = OB_TEXT_SUB,
                    fontSize = if (isCompact) 12.sp else 13.sp,
                    lineHeight = 18.sp,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// PAGE 5 — PROGRESS & OVERLAY
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ProgressAndOverlayPage(isCompact: Boolean, pageState: PagerState, pageIndex: Int) {
    val visible = pageState.currentPage == pageIndex
    val entered = remember { mutableStateOf(false) }
    LaunchedEffect(visible) { if (visible) entered.value = true }

    val infiniteTransition = rememberInfiniteTransition(label = "finalPage")

    // Counter tick animation
    val counterVal by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 12f,
        animationSpec = infiniteRepeatable(
            tween(4000, easing = FastOutSlowInEasing), RepeatMode.Restart
        ),
        label = "counterTick"
    )
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 0.7f,
        animationSpec = infiniteRepeatable(tween(1800, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "finalGlow"
    )

    val readyItems = listOf(
        Triple(Icons.Outlined.Block,           OB_RED,          "Reel blocking engine active"),
        Triple(Icons.Outlined.LockClock,       OB_PURPLE_SOFT,  "Focus Mode ready to protect"),
        Triple(Icons.Outlined.Notifications,   OB_AMBER,        "Overlay reminder available"),
        Triple(Icons.Outlined.BarChart,        OB_BLUE,         "Daily stats tracked for you"),
        Triple(Icons.Outlined.PrivacyTip,      OB_GREEN,        "100% private — nothing leaves your device")
    )

    ObPageLayout(isCompact) {
        Spacer(Modifier.height(if (isCompact) 8.dp else 16.dp))

        // Celebration illustration — animated stat card + glow
        ObFinalIllustration(counterVal, glowPulse, isCompact)

        Spacer(Modifier.height(if (isCompact) 20.dp else 30.dp))

        ObAnimatedContent(visible = entered.value, delayMs = 0) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "You're all set!",
                    color = OB_TEXT,
                    fontSize = if (isCompact) 28.sp else 34.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "ReelBreak is ready. Start protecting your time — you can adjust everything from inside the app.",
                    color = OB_TEXT_SUB,
                    fontSize = if (isCompact) 13.sp else 15.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }

        Spacer(Modifier.height(if (isCompact) 16.dp else 22.dp))

        readyItems.forEachIndexed { i, (icon, color, label) ->
            ObAnimatedContent(visible = entered.value, delayMs = i * 70) {
                ObReadyItem(icon = icon, iconColor = color, text = label, isCompact = isCompact)
            }
            if (i < readyItems.lastIndex) Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun ObFinalIllustration(counterVal: Float, glowPulse: Float, isCompact: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isCompact) 100.dp else 120.dp),
        contentAlignment = Alignment.Center
    ) {
        // Glow behind
        Box(
            modifier = Modifier
                .size(140.dp)
                .background(
                    Brush.radialGradient(listOf(OB_GREEN.copy(glowPulse * 0.35f), Color.Transparent)),
                    CircleShape
                )
        )

        // Stat card
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .background(OB_CARD)
                .border(1.dp, OB_GREEN.copy(alpha = 0.30f), RoundedCornerShape(18.dp))
                .padding(horizontal = 24.dp, vertical = if (isCompact) 14.dp else 18.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ObStatItem(value = "${counterVal.toInt()}", label = "Reels blocked", color = OB_RED)
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(36.dp)
                    .background(OB_CARD_BORDER)
            )
            ObStatItem(value = "${(counterVal * 4.5f).toInt()}m", label = "Time saved", color = OB_GREEN)
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(36.dp)
                    .background(OB_CARD_BORDER)
            )
            ObStatItem(value = "ON", label = "Protection", color = OB_PURPLE_SOFT)
        }
    }
}

@Composable
private fun ObStatItem(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(
            text = value,
            color = color,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = OB_TEXT_MUTED,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ObReadyItem(icon: ImageVector, iconColor: Color, text: String, isCompact: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(13.dp))
            .background(OB_CARD)
            .border(1.dp, OB_CARD_BORDER, RoundedCornerShape(13.dp))
            .padding(horizontal = 14.dp, vertical = if (isCompact) 10.dp else 12.dp),
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
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(if (isCompact) 16.dp else 18.dp)
            )
        }
        Text(
            text = text,
            color = OB_TEXT_SUB,
            fontSize = if (isCompact) 12.sp else 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Outlined.CheckCircle,
            contentDescription = null,
            tint = OB_GREEN.copy(alpha = 0.80f),
            modifier = Modifier.size(16.dp)
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// SHARED COMPOSABLES
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ObPageLayout(isCompact: Boolean, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = if (isCompact) 4.dp else 8.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content
    )
}

@Composable
private fun ObPageHeading(title: String, subtitle: String, isCompact: Boolean) {
    Text(
        text = title,
        color = OB_TEXT,
        fontSize = if (isCompact) 24.sp else 30.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        lineHeight = if (isCompact) 30.sp else 38.sp
    )
    Spacer(Modifier.height(8.dp))
    Text(
        text = subtitle,
        color = OB_TEXT_SUB,
        fontSize = if (isCompact) 13.sp else 14.sp,
        textAlign = TextAlign.Center,
        lineHeight = 21.sp,
        modifier = Modifier.padding(horizontal = 4.dp)
    )
}

@Composable
private fun ObModeCard(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    description: String,
    isCompact: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(OB_CARD)
            .border(1.dp, OB_CARD_BORDER, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = if (isCompact) 12.dp else 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(if (isCompact) 40.dp else 46.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(if (isCompact) 20.dp else 24.dp)
            )
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                text = title,
                color = OB_TEXT,
                fontSize = if (isCompact) 13.sp else 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = description,
                color = OB_TEXT_MUTED,
                fontSize = if (isCompact) 11.sp else 12.sp,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
private fun ObInfoRow(icon: ImageVector, iconColor: Color, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(OB_CARD)
            .border(1.dp, iconColor.copy(alpha = 0.22f), RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 11.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier
                .size(15.dp)
                .padding(top = 1.dp)
        )
        Text(
            text = text,
            color = OB_TEXT_MUTED,
            fontSize = 12.sp,
            lineHeight = 17.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ObAppBadge(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(OB_CARD)
            .border(1.dp, OB_CARD_BORDER, RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = text,
            color = OB_TEXT_SUB,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ObAppChip(name: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.10f))
            .border(1.dp, color.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = name,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun ObFloatingChip(text: String, color: Color, modifier: Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(color.copy(alpha = 0.15f))
            .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(999.dp))
            .padding(horizontal = 9.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ObPillIndicator(currentPage: Int, totalPages: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        repeat(totalPages) { index ->
            val isActive = index == currentPage
            val width by animateDpAsState(
                targetValue = if (isActive) 28.dp else 6.dp,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                label = "pillWidth$index"
            )
            val color by animateColorAsState(
                targetValue = if (isActive) OB_PURPLE else Color.White.copy(alpha = 0.20f),
                animationSpec = tween(300),
                label = "pillColor$index"
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

@Composable
private fun ObCTAButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = OB_PURPLE,
                ambientColor = OB_PURPLE
            )
            .clip(RoundedCornerShape(16.dp))
            .background(OB_GRADIENT_CTA)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ObAnimatedContent(
    visible: Boolean,
    delayMs: Int,
    content: @Composable () -> Unit
) {
    val animSpec = remember(delayMs) {
        spring<Float>(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 380, delayMillis = delayMs),
        label = "obAlpha$delayMs"
    )
    val offsetY by animateFloatAsState(
        targetValue = if (visible) 0f else 28f,
        animationSpec = tween(durationMillis = 420, delayMillis = delayMs, easing = FastOutSlowInEasing),
        label = "obOffsetY$delayMs"
    )
    Box(
        modifier = Modifier
            .graphicsLayer {
                this.alpha = alpha
                translationY = offsetY
            }
    ) {
        content()
    }
}

// ─── Canvas Orb Background ───────────────────────────────────────────────────

private fun DrawScope.drawAmbientOrbs(drift: Float) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0xFF7C3AED).copy(alpha = 0.14f), Color.Transparent),
            center = Offset(size.width * 0.12f, size.height * 0.10f + drift * 30f),
            radius = size.width * 0.52f
        ),
        center = Offset(size.width * 0.12f, size.height * 0.10f + drift * 30f),
        radius = size.width * 0.52f
    )
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0xFF4C1D95).copy(alpha = 0.11f), Color.Transparent),
            center = Offset(size.width * 0.90f, size.height * 0.78f - drift * 25f),
            radius = size.width * 0.48f
        ),
        center = Offset(size.width * 0.90f, size.height * 0.78f - drift * 25f),
        radius = size.width * 0.48f
    )
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0xFF1D4ED8).copy(alpha = 0.07f), Color.Transparent),
            center = Offset(size.width * 0.55f, size.height * 0.45f),
            radius = size.width * 0.38f
        ),
        center = Offset(size.width * 0.55f, size.height * 0.45f),
        radius = size.width * 0.38f
    )
}