package com.practice.reelbreak.ui.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.reelbreak.ui.theme.LocalAppColors
import com.practice.reelbreak.ui.theme.PremiumCard
import com.practice.reelbreak.ui.theme.PremiumIconBubble
import com.practice.reelbreak.ui.theme.PremiumPill
import com.practice.reelbreak.ui.theme.PremiumShapes
import com.practice.reelbreak.ui.theme.premiumChipColor

@Composable
fun BlockModeCard(
    option: BlockModeOption,
    isSelected: Boolean,
    isExpanded: Boolean,
    isOn: Boolean,
    onClick: () -> Unit,
    onExpandToggle: () -> Unit,
    detailContent: @Composable (() -> Unit)?
) {
    val colors = LocalAppColors.current

    val accentColor = when (option.mode) {
        BlockMode.BLOCK_NOW -> colors.purplePrimary
        BlockMode.LIMIT_BASED -> colors.blueAccent
    }

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) colors.borderActive else colors.borderSubtle,
        animationSpec = tween(220),
        label = "borderColor"
    )

    val chevronRotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(220),
        label = "chevronRotation"
    )

    val statusBackground = if (isOn) {
        accentColor.copy(alpha = if (colors.isDark) 0.16f else 0.10f)
    } else {
        premiumChipColor()
    }

    val statusBorder = if (isOn) {
        accentColor.copy(alpha = if (colors.isDark) 0.50f else 0.32f)
    } else {
        colors.borderSubtle
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(PremiumShapes.card)
    ) {
        PremiumCard(
            modifier = Modifier.fillMaxWidth(),
            padding = androidx.compose.foundation.layout.PaddingValues(0.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        accentColor.copy(alpha = if (colors.isDark) 0.16f else 0.08f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onClick
                            )
                            .padding(horizontal = 18.dp, vertical = 18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        PremiumIconBubble(
                            modifier = Modifier.size(46.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(
                                        accentColor.copy(
                                            alpha = if (colors.isDark) 0.10f else 0.08f
                                        ),
                                        shape = CircleShape
                                    )
                            )
                            Icon(
                                imageVector = option.icon,
                                contentDescription = option.title,
                                tint = accentColor,
                                modifier = Modifier
                                    .size(22.dp)
                                    .align(Alignment.Center)
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = option.title,
                                    color = colors.textPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                PremiumPill(
                                    background = premiumChipColor(),
                                    border = colors.borderSubtle,
                                    modifier = Modifier
                                ) {
                                    Text(
                                        text = option.tag,
                                        color = colors.textSecondary,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Text(
                                text = option.subtitle,
                                color = colors.textSecondary,
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            )
                        }

                        PremiumPill(
                            background = statusBackground,
                            border = statusBorder
                        ) {
                            Text(
                                text = if (isOn) "On" else "Off",
                                color = if (isOn) accentColor else colors.textSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 11.dp, vertical = 5.dp)
                            )
                        }
                    }

                    if (detailContent != null) {
                        HorizontalDivider(
                            color = borderColor.copy(alpha = 0.65f),
                            thickness = 0.8.dp
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = onExpandToggle
                                )
                                .padding(horizontal = 18.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (isExpanded) "Hide details" else "How it works",
                                color = colors.textMuted,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Icon(
                                imageVector = Icons.Filled.ExpandMore,
                                contentDescription = null,
                                tint = colors.textMuted,
                                modifier = Modifier
                                    .size(18.dp)
                                    .rotate(chevronRotation)
                            )
                        }

                        AnimatedVisibility(
                            visible = isExpanded,
                            enter = fadeIn(tween(180)) + expandVertically(tween(220)),
                            exit = fadeOut(tween(140)) + shrinkVertically(tween(180))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 14.dp, end = 14.dp, bottom = 14.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        if (colors.isDark) {
                                            Color.White.copy(alpha = 0.04f)
                                        } else {
                                            accentColor.copy(alpha = 0.05f)
                                        }
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (colors.isDark) {
                                            colors.borderSubtle
                                        } else {
                                            accentColor.copy(alpha = 0.14f)
                                        },
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(horizontal = 14.dp, vertical = 14.dp)
                            ) {
                                detailContent()
                            }
                        }
                    }
                }
            }
        }
    }
}