package com.practice.reelbreak.ui.onboarding

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
import com.practice.reelbreak.R
import com.practice.reelbreak.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    onSkip: () -> Unit,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val config = LocalConfiguration.current

    val screenHeight = config.screenHeightDp.dp
    val isCompact = screenHeight < 700.dp

    val pages = OnboardingPageType.values().toList()
    val pageState = rememberPagerState(initialPage = 0) { pages.size }
    val isLastPage = pageState.currentPage == pages.lastIndex

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
        Canvas(modifier = Modifier.fillMaxSize()) { drawOrbBackground(orbOffset) }

        Column(modifier = Modifier.fillMaxSize()) {
            // ── Top Bar ──────────────────────────────────
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
                        Text(
                            "Skip",
                            color = Color.White.copy(alpha = 0.55f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // ── Pager System (Stripped of Perms Screen) ───
            HorizontalPager(
                state = pageState,
                modifier = Modifier.weight(1f),
                userScrollEnabled = true
            ) { index ->
                when (pages[index]) {
                    OnboardingPageType.WELCOME -> WelcomePage(isCompact = isCompact)
                    OnboardingPageType.HOWITWORKS -> HowItWorksPage(isCompact = isCompact)
                    OnboardingPageType.LIMITSSETUP -> LimitsSetupPage(isCompact = isCompact)
                    OnboardingPageType.ALLSET -> AllSetPage(isCompact = isCompact)
                }
            }

            // ── Bottom Section ───────────────────────────
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
            Triple(Icons.Filled.Block,         Color(0xFFF87171), "Blocking engine ready"),
            Triple(Icons.Filled.Shield,        Color(0xFF8B5CF6), "Focus Mode configured"),
            Triple(Icons.Filled.Notifications, Color(0xFFFBBF24), "Stay mindful every day"),
        ).forEach { (icon, tint, label) ->
            ChecklistItem(icon = icon, iconColor = tint, text = label, isCompact = isCompact)
            Spacer(Modifier.height(8.dp))
        }
    }
}

// ─── BOTTOM RENDER ATTACHMENTS ──────────────────────────────────────────────

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

@Composable
private fun BlendedImage(imageRes: Int, contentDescription: String, isCompact: Boolean) {
    val imageHeight = if (isCompact) 200.dp else 260.dp
    Box(modifier = Modifier.fillMaxWidth().height(imageHeight), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = contentDescription,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp)
        )
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent { }
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(
                            brush = Brush.radialGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(0.15f), Color.Black.copy(0.60f), Color.Black),
                                center = Offset(size.width / 2f, size.height * 0.45f),
                                radius = size.width * 0.62f
                            ),
                            blendMode = BlendMode.DstIn
                        )
                    }
                }
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight * 0.45f)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.55f), Color.Black.copy(alpha = 0.90f), Color.Black)
                    )
                )
        )
    }
}

@Composable
private fun PageHeading(title: String, subtitle: String, isCompact: Boolean) {
    Text(text = title, color = Color.White, fontSize = if (isCompact) 24.sp else 30.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, lineHeight = if (isCompact) 30.sp else 38.sp)
    Spacer(Modifier.height(8.dp))
    Text(text = subtitle, color = Color.White.copy(alpha = 0.55f), fontSize = if (isCompact) 13.sp else 14.sp, textAlign = TextAlign.Center, lineHeight = 21.sp)
}

@Composable
private fun FeaturePill(icon: ImageVector, text: String) {
    Row(modifier = Modifier.clip(RoundedCornerShape(999.dp)).background(Color(0xFF1C1233)).border(1.dp, Color(0xFF8B5CF6).copy(alpha = 0.40f), RoundedCornerShape(999.dp)).padding(horizontal = 12.dp, vertical = 7.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Icon(imageVector = icon, contentDescription = null, tint = Color(0xFFA78BFA), modifier = Modifier.size(13.dp))
        Text(text = text, color = Color.White.copy(alpha = 0.80f), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun ModeRow(icon: ImageVector, iconColor: Color, title: String, desc: String, isCompact: Boolean) {
    Row(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Color(0xFF1C1233)).border(1.dp, Color(0xFF2D1B4E), RoundedCornerShape(14.dp)).padding(horizontal = 14.dp, vertical = if (isCompact) 10.dp else 14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(modifier = Modifier.size(if (isCompact) 38.dp else 44.dp).clip(RoundedCornerShape(11.dp)).background(iconColor.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(if (isCompact) 18.dp else 22.dp))
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(text = title, color = Color.White, fontSize = if (isCompact) 13.sp else 14.sp, fontWeight = FontWeight.SemiBold)
            Text(text = desc, color = Color.White.copy(alpha = 0.52f), fontSize = if (isCompact) 11.sp else 12.sp, lineHeight = 17.sp)
        }
    }
}

@Composable
private fun InfoStatCard(icon: ImageVector, iconColor: Color, value: String, subtext: String, modifier: Modifier, isCompact: Boolean) {
    Column(modifier = modifier.clip(RoundedCornerShape(14.dp)).background(Color(0xFF1C1233)).border(1.dp, Color(0xFF2D1B4E), RoundedCornerShape(14.dp)).padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(modifier = Modifier.size(if (isCompact) 36.dp else 42.dp).clip(RoundedCornerShape(10.dp)).background(iconColor.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(if (isCompact) 18.dp else 22.dp))
        }
        Text(text = value, color = Color.White, fontSize = if (isCompact) 12.sp else 13.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
        Text(text = subtext, color = Color.White.copy(alpha = 0.45f), fontSize = if (isCompact) 10.sp else 11.sp, textAlign = TextAlign.Center, lineHeight = 15.sp)
    }
}

@Composable
private fun TipBox(text: String, isCompact: Boolean) {
    Row(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0xFF1C1233)).border(1.dp, Color(0xFFFBBF24).copy(alpha = 0.25f), RoundedCornerShape(12.dp)).padding(horizontal = 14.dp, vertical = if (isCompact) 10.dp else 12.dp), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Icon(imageVector = Icons.Filled.Lightbulb, contentDescription = null, tint = Color(0xFFFBBF24), modifier = Modifier.size(16.dp).padding(top = 1.dp))
        Text(text = text, color = Color.White.copy(alpha = 0.58f), fontSize = if (isCompact) 11.sp else 12.sp, lineHeight = 17.sp)
    }
}

@Composable
private fun ChecklistItem(icon: ImageVector, iconColor: Color, text: String, isCompact: Boolean) {
    Row(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0xFF1C1233)).border(1.dp, Color(0xFF2D1B4E), RoundedCornerShape(12.dp)).padding(horizontal = 14.dp, vertical = if (isCompact) 10.dp else 13.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(modifier = Modifier.size(if (isCompact) 32.dp else 36.dp).clip(RoundedCornerShape(9.dp)).background(iconColor.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(if (isCompact) 16.dp else 18.dp))
        }
        Text(text = text, color = Color.White.copy(alpha = 0.82f), fontSize = if (isCompact) 13.sp else 14.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = null, tint = Color(0xFF4ADE80), modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun PillIndicator(currentPage: Int, totalPages: Int) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        repeat(totalPages) { index ->
            val isActive = index == currentPage
            val width by animateDpAsState(targetValue = if (isActive) 28.dp else 6.dp, animationSpec = tween(300), label = "indicatorWidth")
            val color by animateColorAsState(targetValue = if (isActive) Color(0xFF8B5CF6) else Color.White.copy(alpha = 0.22f), animationSpec = tween(300), label = "indicatorColor")
            Box(modifier = Modifier.height(6.dp).width(width).clip(CircleShape).background(color))
        }
    }
}

@Composable
private fun OnboardingCTAButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().height(54.dp).shadow(elevation = 20.dp, shape = RoundedCornerShape(18.dp), spotColor = Color(0xFF8B5CF6), ambientColor = Color(0xFF8B5CF6)).clip(RoundedCornerShape(18.dp)).background(Brush.linearGradient(listOf(Color(0xFF7C3AED), Color(0xFF4C1D95)))).clickable(
            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }, indication = null, onClick = onClick
        ), contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
    }
}

private fun DrawScope.drawOrbBackground(offset: Float) {
    drawCircle(brush = Brush.radialGradient(colors = listOf(Color(0xFF7C3AED).copy(alpha = 0.18f), Color.Transparent), center = Offset(size.width * 0.1f, size.height * 0.08f + offset * 0.04f), radius = size.width * 0.55f), center = Offset(size.width * 0.1f, size.height * 0.08f + offset * 0.04f), radius = size.width * 0.55f)
    drawCircle(brush = Brush.radialGradient(colors = listOf(Color(0xFF4C1D95).copy(alpha = 0.14f), Color.Transparent), center = Offset(size.width * 0.92f, size.height * 0.80f - offset * 0.05f), radius = size.width * 0.50f), center = Offset(size.width * 0.92f, size.height * 0.80f - offset * 0.05f), radius = size.width * 0.50f)
}