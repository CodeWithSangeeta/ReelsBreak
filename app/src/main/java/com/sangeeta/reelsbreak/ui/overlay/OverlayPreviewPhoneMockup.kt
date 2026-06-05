package com.sangeeta.reelsbreak.ui.overlay

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.reelsbreak.ui.theme.LocalAppColors
import kotlinx.coroutines.delay


private data class ReelPreviewPageData(
    val top: Color,
    val middle: Color,
    val bottom: Color
)

private const val PREVIEW_REEL_LIMIT = 5
private const val PAGE_HOLD_MS = 1100L
private const val PAGE_SCROLL_MS = 500
private const val LIMIT_HOLD_MS = 2500L
private const val LOOP_DELAY_MS = 900L

@Composable
 fun OverlayPreviewPhoneMockup() {
    val colors = LocalAppColors.current

    var reelsWatched by remember { mutableIntStateOf(1) }
    var elapsedSeconds by remember { mutableLongStateOf(12L) }
    var currentPage by remember { mutableIntStateOf(0) }
    var showLimitReached by remember { mutableStateOf(false) }
    var cycleKey by remember { mutableIntStateOf(0) }

    LaunchedEffect(cycleKey) {
        reelsWatched = 1
        elapsedSeconds = 12L
        currentPage = 0
        showLimitReached = false

        while (reelsWatched < PREVIEW_REEL_LIMIT) {
            delay(PAGE_HOLD_MS)
            currentPage += 1
            reelsWatched += 1
            delay(PAGE_SCROLL_MS.toLong())
        }

        delay(PAGE_HOLD_MS)
        showLimitReached = true
        delay(LIMIT_HOLD_MS)
        showLimitReached = false
        delay(LOOP_DELAY_MS)
        cycleKey += 1
    }

    LaunchedEffect(cycleKey, showLimitReached) {
        while (!showLimitReached) {
            delay(1000L)
            elapsedSeconds += 1L
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth()
            .height(280.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.52f)
                .height(260.dp)
                .clip(RoundedCornerShape(26.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = if (colors.isDark) {
                            listOf(Color(0xFF10131A), Color(0xFF0C0F15))
                        } else {
                            listOf(Color(0xFFF4F0FF), Color(0xFFECE7FA))
                        }
                    )
                )
                .border(
                    width = 1.dp,
                    color = colors.borderSubtle.copy(alpha = if (colors.isDark) 0.42f else 0.16f),
                    shape = RoundedCornerShape(26.dp)
                )
                .padding(10.dp)
                .aspectRatio(0.60f)
        ) {
            ReelsFeedPreviewBackground(currentPage = currentPage)

            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 12.dp)
                    // .height(96.dp)
                    .scale(0.78f)
            ) {
                AnimatedContent(
                    targetState = showLimitReached,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(180)) togetherWith
                                fadeOut(animationSpec = tween(120)) using
                                SizeTransform(clip = false)
                    },
                    label = "overlay_card_content"
                ) { isLimitReached ->
                    if (isLimitReached) {
                        OverlayPreviewLimitReachedCard()
                    } else {
                        OverlayPreviewCard(
                            reelsWatched = reelsWatched,
                            timeDisplay = formatSeconds(elapsedSeconds),
                            showReels = true,
                            showTimer = true
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp)
                    .width(72.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = if (colors.isDark) 0.18f else 0.30f))
            )
        }
    }
}


@Composable
private fun BoxScope.ReelsFeedPreviewBackground(
    currentPage: Int
) {
    val pages = remember {
        listOf(
            ReelPreviewPageData(
                top = Color(0xFF7C8CFF),
                middle = Color(0xFF5FA8FF),
                bottom = Color(0xFF79D7FF)
            ),
            ReelPreviewPageData(
                top = Color(0xFF5FD6B6),
                middle = Color(0xFF48CFA9),
                bottom = Color(0xFF86E7C8)
            ),
            ReelPreviewPageData(
                top = Color(0xFFFF8FA8),
                middle = Color(0xFFFF7393),
                bottom = Color(0xFFFFC17D)
            ),
            ReelPreviewPageData(
                top = Color(0xFFB58CFF),
                middle = Color(0xFF8E7CFF),
                bottom = Color(0xFF7EA8FF)
            ),
            ReelPreviewPageData(
                top = Color(0xFF79B8FF),
                middle = Color(0xFF5AA1FF),
                bottom = Color(0xFF96DCFF)
            )
        )
    }

    val page = pages[currentPage % pages.size]
    val pulse = rememberInfiniteTransition(label = "reel_bg_pulse")

    val flashAlpha by animateFloatAsState(
        targetValue = 0.04f,
        animationSpec = tween(120),
        label = "bg_flash_alpha"
    )

    val ambientAlpha by pulse.animateFloat(
        initialValue = 0.10f,
        targetValue = 0.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ambient_alpha"
    )

    AnimatedContent(
        targetState = page,
        transitionSpec = {
            (fadeIn(animationSpec = tween(170)) + slideInVertically(
                initialOffsetY = { it / 8 },
                animationSpec = tween(260, easing = LinearOutSlowInEasing)
            )).togetherWith(
                fadeOut(animationSpec = tween(120))
            )
        },
        modifier = Modifier
            .matchParentSize()
            .clip(RoundedCornerShape(20.dp)),
        label = "reels_full_page_swap"
    ) { targetPage ->
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(targetPage.top, targetPage.middle, targetPage.bottom)
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.White.copy(alpha = flashAlpha))
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = ambientAlpha),
                                Color.Transparent
                            ),
                            radius = 700f
                        )
                    )
            )


            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.02f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.16f)
                            )
                        )
                    )
            )

            FakeSingleReelBackgroundPage(
                page = targetPage,
                modifier = Modifier.matchParentSize()
            )

            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
            ) {
                ReelSideActions()
            }
        }
    }
}

@Composable
private fun FakeSingleReelBackgroundPage(
    page: ReelPreviewPageData,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 14.dp, end = 54.dp, bottom = 18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.92f))
                )

                PreviewLine(width = 68.dp, alpha = 0.86f)
            }

            PreviewLine(width = 118.dp, alpha = 0.92f)
            PreviewLine(width = 88.dp, alpha = 0.70f)
            PreviewLine(width = 70.dp, alpha = 0.46f)
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 12.dp, top = 12.dp)
                .width(42.dp)
                .height(4.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.22f))
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 14.dp)
                .width(64.dp)
                .height(5.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.16f))
        )
    }
}

@Composable
private fun PreviewLine(
    width: Dp,
    alpha: Float
) {
    Box(
        modifier = Modifier
            .width(width)
            .height(8.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = alpha))
    )
}

private fun formatSeconds(seconds: Long): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", mins, secs)
}



@Composable
private fun ReelSideActions() {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repeat(4) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f))
            )
        }
    }
}



@Composable
private fun OverlayPreviewLimitReachedCard() {
    val colors = LocalAppColors.current
    val pulse = rememberInfiniteTransition(label = "limit_pulse")

    val iconScale by pulse.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "limit_icon_scale"
    )

    val iconAlpha by pulse.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "limit_icon_alpha"
    )

    Box(
        modifier = Modifier
            .widthIn(max = 200.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(if (colors.isDark) Color(0xF21A1E28) else Color(0xF8FFF7FB))
            .border(
                width = 1.dp,
                color = if (colors.isDark) Color.White.copy(alpha = 0.08f)
                else Color.Black.copy(alpha = 0.08f),
                shape = RoundedCornerShape(22.dp)
            )
            .padding(horizontal = 18.dp, vertical = 18.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .scale(iconScale)
                    .alpha(iconAlpha)
                    .clip(CircleShape)
                    .background(Color(0xFFFF5A76).copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.WarningAmber,
                    contentDescription = null,
                    tint = Color(0xFFFF5A76),
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "Limit reached",
                    color = colors.textPrimary,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "You've watched enough short videos for now.",
                    color = colors.textSecondary,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )

                Text(
                    text = "Take a break and come back later.",
                    color = colors.textSecondary,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
