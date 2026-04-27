package com.practice.reelbreak.ui.permission

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.AccessibilityNew
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.reelbreak.ui.theme.LocalAppColors

sealed class PermissionSheetType {
    object ACCESSIBILITY : PermissionSheetType()
    object USAGE_ACCESS : PermissionSheetType()
    object OVERLAY : PermissionSheetType()
}

private data class PermissionSheetContent(
    val icon: ImageVector,
    val iconTint: Color,
    val iconGlowColor: Color,
    val badgeLabel: String,
    val isMandatory: Boolean,
    val title: String,
    val description: String,
    val steps: List<String>,
    val privacyNote: String,
    val agreeButtonText: String,
)

private fun buildContent(type: PermissionSheetType): PermissionSheetContent =
    when (type) {
        is PermissionSheetType.ACCESSIBILITY -> PermissionSheetContent(
            icon = Icons.Outlined.AccessibilityNew,
            iconTint = Color(0xFF9B3DFF),
            iconGlowColor = Color(0x339B3DFF),
            badgeLabel = "Required",
            isMandatory = true,
            title = "Accessibility permission",
            description = "ReelsBreak needs Accessibility Service to detect and block short videos in real time.",
            steps = listOf(
                "Tap \"Agree & Open Settings\" below",
                "Find \"ReelsBreak\" under Installed Services",
                "Enable the toggle",
                "Press back to return"
            ),
            privacyNote = "Only checks which app is on screen. It never reads messages, passwords, or content.",
            agreeButtonText = "Agree & Open Settings",
        )

        is PermissionSheetType.USAGE_ACCESS -> PermissionSheetContent(
            icon = Icons.Outlined.BarChart,
            iconTint = Color(0xFF3B82F6),
            iconGlowColor = Color(0x333B82F6),
            badgeLabel = "Required",
            isMandatory = true,
            title = "Usage access permission",
            description = "ReelsBreak needs Usage Access to track time on short-video apps and apply your limits.",
            steps = listOf(
                "Tap \"Agree & Open Settings\" below",
                "Find \"ReelsBreak\" in the list",
                "Enable \"Permit usage access\"",
                "Press back to return"
            ),
            privacyNote = "Only reads app usage time. It never sees history, content, or personal data.",
            agreeButtonText = "Agree & Open Settings",
        )

        is PermissionSheetType.OVERLAY -> PermissionSheetContent(
            icon = Icons.Outlined.Layers,
            iconTint = Color(0xFF2ECC71),
            iconGlowColor = Color(0x332ECC71),
            badgeLabel = "Optional",
            isMandatory = false,
            title = "Overlay permission",
            description = "ReelsBreak uses an overlay bubble to show your live reel counter above other apps.",
            steps = listOf(
                "Tap \"Allow Overlay\" below",
                "Find \"ReelsBreak\" in the list",
                "Enable \"Allow display over other apps\"",
                "Press back to return"
            ),
            privacyNote = "Overlay only shows the counter. It cannot read or interact with anything on screen.",
            agreeButtonText = "Allow Overlay",
        )
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionBottomSheet(
    type: PermissionSheetType,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false),
    onDismiss: () -> Unit,
    onAgree: () -> Unit,
) {
    val content = buildContent(type)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF1C1B2E),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 2.dp)
                    .width(36.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color.White.copy(alpha = 0.25f))
            )
        },
        tonalElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp)
                .padding(bottom = 20.dp, top = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(4.dp))

            PermissionIconSection(
                icon = content.icon,
                iconTint = content.iconTint,
                glowColor = content.iconGlowColor,
            )

            Spacer(Modifier.height(16.dp))

            // Title + small badge on the same line
            TitleWithBadge(
                title = content.title,
                badgeLabel = content.badgeLabel,
                isMandatory = content.isMandatory
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = content.description,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White.copy(alpha = 0.72f),
                textAlign = TextAlign.Center,
                lineHeight = 19.sp,
            )

            Spacer(Modifier.height(18.dp))

            StepsSection(
                steps = content.steps,
                accentColor = content.iconTint
            )

            Spacer(Modifier.height(16.dp))

            PrivacyNote(note = content.privacyNote)

            Spacer(Modifier.height(22.dp))

            ActionButtons(
                agreeText = content.agreeButtonText,
                accentColor = content.iconTint,
                onDismiss = onDismiss,
                onAgree = onAgree,
            )
        }
    }
}

@Composable
private fun PermissionIconSection(
    icon: ImageVector,
    iconTint: Color,
    glowColor: Color,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(60.dp),
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(glowColor, Color.Transparent),
                    )
                )
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(55.dp)
                .clip(CircleShape)
                .background(iconTint.copy(alpha = 0.12f))
                .border(1.3.dp, iconTint.copy(alpha = 0.35f), CircleShape),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(30.dp),
            )
        }
    }
}

// NEW: compact title + badge row
@Composable
private fun TitleWithBadge(
    title: String,
    badgeLabel: String,
    isMandatory: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 19.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 26.sp,
            modifier = Modifier.weight(1f, fill = false)
        )

        Spacer(modifier = Modifier.width(8.dp))

        PermissionBadge(
            label = badgeLabel,
            isMandatory = isMandatory,
            compact = true
        )
    }
}

// changed: now supports compact mode
@Composable
private fun PermissionBadge(
    label: String,
    isMandatory: Boolean,
    compact: Boolean = false,
) {
    val bgColor = if (isMandatory) Color(0x33E05555) else Color(0x332ECC71)
    val textColor = if (isMandatory) Color(0xFFFF8080) else Color(0xFF2ECC71)
    val borderColor = if (isMandatory) Color(0x66E05555) else Color(0x662ECC71)

    val horizontalPadding = if (compact) 8.dp else 12.dp
    val verticalPadding = if (compact) 2.dp else 4.dp
    val fontSize = if (compact) 9.sp else 10.sp

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(999.dp))
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
    ) {
        Text(
            text = label.uppercase(),
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            color = textColor,
            letterSpacing = 1.1.sp,
        )
    }
}

@Composable
private fun StepsSection(
    steps: List<String>,
    accentColor: Color
) {
    val colors = LocalAppColors.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(bottom = 2.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.45f),
                modifier = Modifier.size(13.dp),
            )
            Text(
                text = "How to enable",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White.copy(alpha = 0.55f),
                letterSpacing = 0.6.sp,
            )
        }

        steps.forEachIndexed { index, step ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black)
                    .border(
                        0.8.dp,
                        Color.White.copy(alpha = 0.05f),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.16f))
                        .border(
                            0.8.dp,
                            accentColor.copy(alpha = 0.45f),
                            CircleShape
                        ),
                ) {
                    Text(
                        text = (index + 1).toString(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                    )
                }
                Text(
                    text = step,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.9f),
                    lineHeight = 18.sp,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun PrivacyNote(note: String) {
    val greenColor = Color(0xFF2ECC71)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0D2B1A))
            .border(0.8.dp, greenColor.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Icon(
            imageVector = Icons.Outlined.VerifiedUser,
            contentDescription = null,
            tint = greenColor,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = note,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = greenColor.copy(alpha = 0.88f),
            lineHeight = 17.sp,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun ActionButtons(
    agreeText: String,
    accentColor: Color,
    onDismiss: () -> Unit,
    onAgree: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextButton(
            onClick = onDismiss,
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = "Not now",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.5f),
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(2f)
                .height(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            accentColor,
                            accentColor.copy(alpha = 0.8f),
                        )
                    )
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onAgree,
                ),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Shield,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(15.dp),
                )
                Text(
                    text = agreeText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                )
            }
        }
    }
}