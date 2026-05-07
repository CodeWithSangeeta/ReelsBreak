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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.reelbreak.ui.theme.LocalAppColors

@Composable
fun BlockModeCard(
    option: BlockModeOption,
    isSelected: Boolean,
    isExpanded: Boolean,
    isOn: Boolean,
    onClick: () -> Unit,
    onExpandToggle: () -> Unit,
    detailContent: (@Composable () -> Unit)?
) {
    val colors = LocalAppColors.current

    val gradient = when (option.mode) {
        BlockMode.BLOCK_NOW    -> colors.modeBlock
        BlockMode.LIMIT_BASED  -> colors.modeLimit
        BlockMode.SMART_FILTER -> colors.modeSmart
    }

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) colors.borderActive
        else colors.borderSubtle.copy(alpha = 0.5f),
        animationSpec = tween(250),
        label = "border"
    )

    val chevronRotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(250),
        label = "chevron"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isSelected) 14.dp else 4.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = if (isSelected) option.glowColor else Color.Transparent,
                ambientColor = if (isSelected) option.glowColor else Color.Transparent
            )
            .clip(RoundedCornerShape(20.dp))
            .background(gradient)
            .border(
                width = if (isSelected) 1.5.dp else 0.8.dp,
                color = borderColor,
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onClick
                    )
                    .padding(horizontal = 18.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Icon box
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(13.dp))
                        .background(Color.White.copy(alpha = 0.15f))
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(13.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = option.icon,
                        contentDescription = option.title,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = option.title,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(Color.White.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = option.tag,
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = option.subtitle,
                        color = Color.White.copy(alpha = 0.72f),
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }

                // On/Off pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(
                            if (isOn) colors.successGreen.copy(alpha = 0.25f)
                            else Color.White.copy(alpha = 0.12f)
                        )
                        .border(
                            width = 1.dp,
                            color = if (isOn) colors.successGreen.copy(alpha = 0.6f)
                            else Color.White.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(999.dp)
                        )
                        .padding(horizontal = 11.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = if (isOn) "On" else "Off",
                        color = if (isOn) colors.successGreen else Color.White.copy(alpha = 0.8f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // ── BOTTOM ROW: expand/collapse — NO permission check ──
            if (detailContent != null) {
                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.08f),
                    thickness = 0.8.dp
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onExpandToggle  // ← no permission, just expand
                        )
                        .padding(horizontal = 18.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isExpanded) "Hide details" else "How it works",
                        color = Color.White.copy(alpha = 0.55f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        imageVector = Icons.Filled.ExpandMore,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.55f),
                        modifier = Modifier
                            .size(18.dp)
                            .rotate(chevronRotation)
                    )
                }

                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn(tween(200)) + expandVertically(tween(250)),
                    exit  = fadeOut(tween(150)) + shrinkVertically(tween(200))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.18f))
                            .padding(horizontal = 18.dp, vertical = 14.dp)
                    ) {
                        detailContent()
                    }
                }
            }
        }
    }
}