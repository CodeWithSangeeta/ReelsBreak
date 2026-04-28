//package com.practice.reelbreak.ui.dashboard
//
//import androidx.compose.animation.animateColorAsState
//import androidx.compose.animation.animateContentSize
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.practice.reelbreak.ui.theme.LocalAppColors
//
//
//@Composable
//fun BlockModeCard(
//    option: BlockModeOption,
//    isSelected: Boolean,
//    isExpanded: Boolean,
//    isOn: Boolean,
//    onClick: () -> Unit,
//    detailContent: @Composable (() -> Unit)?
//) {
//    val colors = LocalAppColors.current
//    val gradient = when (option.mode) {
//        BlockMode.BLOCK_NOW -> colors.modeBlock
//        BlockMode.LIMIT_BASED -> colors.modeLimit
//        BlockMode.SMART_FILTER -> colors.modeSmart
//    }
//
//    val borderColor by animateColorAsState(
//        targetValue = if (isSelected) colors.borderActive else colors.borderSubtle,
//        animationSpec = tween(250),
//        label = "border"
//    )
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//           // .padding(horizontal = 20.dp)
//            .shadow(
//                elevation = if (isSelected) 16.dp else 4.dp,
//                shape = RoundedCornerShape(18.dp),
//                spotColor = if (isSelected) option.glowColor else Color.Transparent,
//                ambientColor = if (isSelected) option.glowColor else Color.Transparent
//            )
//            .clip(RoundedCornerShape(18.dp))
//            .background(if (isSelected) gradient else colors.cardSurface)
//            .border(
//                width = if (isSelected) 1.5.dp else 1.dp,
//                color = borderColor,
//                shape = RoundedCornerShape(18.dp)
//            )
//            .clickable(
//                interactionSource = remember { MutableInteractionSource() },
//                indication = null,
//                onClick = onClick
//            )
//            .padding(18.dp)
//            .animateContentSize() // smooth expand/collapse
//    ) {
//        Column(
//            modifier = Modifier.fillMaxWidth(),
//            verticalArrangement = Arrangement.spacedBy(10.dp)
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                // Icon container
//                Box(
//                    modifier = Modifier
//                        .size(46.dp)
//                        .clip(RoundedCornerShape(12.dp))
//                        .background(Color(0x33FFFFFF)),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Icon(
//                        imageVector = option.icon,
//                        contentDescription = option.title,
//                        tint = Color.White,
//                        modifier = Modifier.size(24.dp)
//                    )
//                }
//
//                Column(
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        Text(
//                            text = option.title,
//                            color = colors.textPrimary,
//                            fontSize = 15.sp,
//                            fontWeight = FontWeight.SemiBold
//                        )
//
//                        // Tag badge (Strict / Balanced / Smart)
//                        Box(
//                            modifier = Modifier
//                                .clip(RoundedCornerShape(6.dp))
//                                .background(Color(0x33FFFFFF))
//                                .padding(horizontal = 7.dp, vertical = 2.dp)
//                        ) {
//                            Text(
//                                text = option.tag,
//                                color = colors.textPrimary,
//                                fontSize = 10.sp,
//                                fontWeight = FontWeight.Medium
//                            )
//                        }
//                    }
//
//                    Spacer(modifier = Modifier.height(4.dp))
//
//                    Text(
//                        text = option.subtitle,
//                        color = if (isSelected) Color(0xCCFFFFFF) else colors.textSecondary,
//                        fontSize = 12.sp,
//                        lineHeight = 17.sp
//                    )
//                }
//
//                // On / Off pill – this mirrors your screenshot
//                Box(
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(12.dp))
//                        .background(
//                            if (isOn) Color(0x332ECC71) else Color(0x33FFFFFF)
//                        )
//                        .padding(horizontal = 10.dp, vertical = 4.dp)
//                ) {
//                    Text(
//                        text = if (isOn) "On" else "Off",
//                        color = if (isOn) colors.successGreen else colors.textPrimary,
//                        fontSize = 11.sp,
//                        fontWeight = FontWeight.Medium
//                    )
//                }
//            }
//
//            // Expanded details
//            if (isExpanded && detailContent != null) {
//                Spacer(modifier = Modifier.height(4.dp))
//                detailContent()
//            }
//        }
//    }
//}


//package com.practice.reelbreak.ui.dashboard
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.animateColorAsState
//import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.animation.core.tween
//import androidx.compose.animation.expandVertically
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.animation.shrinkVertically
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ExpandMore
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.rotate
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.practice.reelbreak.ui.theme.LocalAppColors
//
//@Composable
//fun BlockModeCard(
//    option: BlockModeOption,
//    isSelected: Boolean,
//    isExpanded: Boolean,
//    isOn: Boolean,
//    onClick: () -> Unit,
//    detailContent: (@Composable () -> Unit)?
//) {
//    val colors = LocalAppColors.current
//
//    // Gradient is ALWAYS shown — switches between full opacity and dimmed
//    val gradient = when (option.mode) {
//        BlockMode.BLOCK_NOW    -> colors.modeBlock
//        BlockMode.LIMIT_BASED  -> colors.modeLimit
//        BlockMode.SMART_FILTER -> colors.modeSmart
//    }
//
//    val borderColor by animateColorAsState(
//        targetValue = if (isSelected) colors.borderActive
//        else colors.borderSubtle.copy(alpha = 0.5f),
//        animationSpec = tween(250),
//        label = "border"
//    )
//
//    val chevronRotation by animateFloatAsState(
//        targetValue = if (isExpanded) 180f else 0f,
//        animationSpec = tween(250),
//        label = "chevron"
//    )
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .shadow(
//                elevation = if (isSelected) 14.dp else 4.dp,
//                shape = RoundedCornerShape(20.dp),
//                spotColor = if (isSelected) option.glowColor else Color.Transparent,
//                ambientColor = if (isSelected) option.glowColor else Color.Transparent
//            )
//            .clip(RoundedCornerShape(20.dp))
//            // ── Always colored gradient ──
//            .background(gradient)
//            .border(
//                width = if (isSelected) 1.5.dp else 0.8.dp,
//                color = borderColor,
//                shape = RoundedCornerShape(20.dp)
//            )
//    ) {
//        Column(modifier = Modifier.fillMaxWidth()) {
//
//            // ── Main row (always visible) — clicking turns mode On/Off ──
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable(
//                        interactionSource = remember { MutableInteractionSource() },
//                        indication = null,
//                        onClick = onClick
//                    )
//                    .padding(horizontal = 18.dp, vertical = 16.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(14.dp)
//            ) {
//                // Icon box
//                Box(
//                    modifier = Modifier
//                        .size(44.dp)
//                        .clip(RoundedCornerShape(13.dp))
//                        .background(Color.White.copy(alpha = 0.15f))
//                        .border(
//                            width = 1.dp,
//                            color = Color.White.copy(alpha = 0.2f),
//                            shape = RoundedCornerShape(13.dp)
//                        ),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Icon(
//                        imageVector = option.icon,
//                        contentDescription = option.title,
//                        tint = Color.White,
//                        modifier = Modifier.size(22.dp)
//                    )
//                }
//
//                // Title + subtitle
//                Column(
//                    modifier = Modifier.weight(1f),
//                    verticalArrangement = Arrangement.spacedBy(4.dp)
//                ) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        Text(
//                            text = option.title,
//                            color = Color.White,
//                            fontSize = 15.sp,
//                            fontWeight = FontWeight.SemiBold
//                        )
//                        // Tag badge
//                        Box(
//                            modifier = Modifier
//                                .clip(RoundedCornerShape(999.dp))
//                                .background(Color.White.copy(alpha = 0.15f))
//                                .padding(horizontal = 8.dp, vertical = 2.dp)
//                        ) {
//                            Text(
//                                text = option.tag,
//                                color = Color.White,
//                                fontSize = 9.sp,
//                                fontWeight = FontWeight.Bold
//                            )
//                        }
//                    }
//                    Text(
//                        text = option.subtitle,
//                        color = Color.White.copy(alpha = 0.72f),
//                        fontSize = 12.sp,
//                        lineHeight = 17.sp
//                    )
//                }
//
//                // On/Off pill
//                Box(
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(999.dp))
//                        .background(
//                            if (isOn) colors.successGreen.copy(alpha = 0.25f)
//                            else Color.White.copy(alpha = 0.12f)
//                        )
//                        .border(
//                            width = 1.dp,
//                            color = if (isOn) colors.successGreen.copy(alpha = 0.6f)
//                            else Color.White.copy(alpha = 0.25f),
//                            shape = RoundedCornerShape(999.dp)
//                        )
//                        .padding(horizontal = 11.dp, vertical = 5.dp)
//                ) {
//                    Text(
//                        text = if (isOn) "On" else "Off",
//                        color = if (isOn) colors.successGreen else Color.White.copy(alpha = 0.8f),
//                        fontSize = 11.sp,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                }
//            }
//
//            // ── Divider + chevron row — tapping expands details ──
//            if (detailContent != null) {
//                HorizontalDivider(
//                    color = Color.White.copy(alpha = 0.08f),
//                    thickness = 0.8.dp
//                )
//
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable(
//                            interactionSource = remember { MutableInteractionSource() },
//                            indication = null,
//                            onClick = onClick   // same click = same expandedMode toggle
//                        )
//                        .padding(horizontal = 18.dp, vertical = 8.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Text(
//                        text = if (isExpanded) "Hide details" else "How it works",
//                        color = Color.White.copy(alpha = 0.55f),
//                        fontSize = 11.sp,
//                        fontWeight = FontWeight.Medium
//                    )
//                    Icon(
//                        imageVector = Icons.Filled.ExpandMore,
//                        contentDescription = if (isExpanded) "Collapse" else "Expand",
//                        tint = Color.White.copy(alpha = 0.55f),
//                        modifier = Modifier
//                            .size(18.dp)
//                            .rotate(chevronRotation)
//                    )
//                }
//
//                // ── Expandable details area ──
//                AnimatedVisibility(
//                    visible = isExpanded,
//                    enter = fadeIn(tween(200)) + expandVertically(tween(250)),
//                    exit  = fadeOut(tween(150)) + shrinkVertically(tween(200))
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .background(Color.Black.copy(alpha = 0.18f))
//                            .padding(horizontal = 18.dp, vertical = 14.dp)
//                    ) {
//                        detailContent()
//                    }
//                }
//            }
//        }
//    }
//}




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
    onClick: () -> Unit,          // turns mode On/Off — permission checked
    onExpandToggle: () -> Unit,   // expands "How it works" — NO permission needed
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

            // ── TOP ROW: clicking this turns mode On/Off (permission checked) ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onClick   // ← permission check happens here
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