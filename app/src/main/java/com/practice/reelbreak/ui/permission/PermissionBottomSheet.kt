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

// ─────────────────────────────────────────────────────────────────────────────
// Data model — one sealed class drives all three permission types
// ─────────────────────────────────────────────────────────────────────────────

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

private fun buildContent(type: PermissionSheetType): PermissionSheetContent = when (type) {
    is PermissionSheetType.ACCESSIBILITY -> PermissionSheetContent(
        icon = Icons.Outlined.AccessibilityNew,
        iconTint = Color(0xFF9B3DFF),
        iconGlowColor = Color(0x339B3DFF),
        badgeLabel = "Required",
        isMandatory = true,
        title = "Accessibility Permission\nRequired",
        description = "To detect and block short videos in real time, ReelsBreak needs Accessibility Service permission. This is the core permission that makes the app work.",
        steps = listOf(
            "Tap \"Agree & Open Settings\" below",
            "Find \"ReelsBreak\" under Installed Services",
            "Tap it and enable the toggle",
            "Press back — you're all set!"
        ),
        privacyNote = "ReelsBreak only reads which app is open and visible on screen. It does NOT read messages, passwords, or any personal content.",
        agreeButtonText = "Agree & Open Settings",
    )
    is PermissionSheetType.USAGE_ACCESS -> PermissionSheetContent(
        icon = Icons.Outlined.BarChart,
        iconTint = Color(0xFF3B82F6),
        iconGlowColor = Color(0x333B82F6),
        badgeLabel = "Required",
        isMandatory = true,
        title = "Usage Access Permission\nRequired",
        description = "To show how much time you spend on short video apps and enforce your daily limits, ReelsBreak needs Usage Access permission.",
        steps = listOf(
            "Tap \"Agree & Open Settings\" below",
            "Find \"ReelsBreak\" in the list",
            "Tap and enable \"Allow usage access\"",
            "Press back — limits are now active!"
        ),
        privacyNote = "ReelsBreak only reads per-app usage time summaries. It does NOT access your browsing history, app content, or personal data.",
        agreeButtonText = "Agree & Open Settings",
    )
    is PermissionSheetType.OVERLAY -> PermissionSheetContent(
        icon = Icons.Outlined.Layers,
        iconTint = Color(0xFF2ECC71),
        iconGlowColor = Color(0x332ECC71),
        badgeLabel = "Optional",
        isMandatory = false,
        title = "Display Overlay\nPermission",
        description = "To show a floating reel counter above other apps, ReelsBreak needs Display Over Other Apps permission. This is completely optional.",
        steps = listOf(
            "Tap \"Allow Overlay\" below",
            "Find \"ReelsBreak\" in the list",
            "Enable \"Allow display over other apps\"",
            "Press back — the counter will appear!"
        ),
        privacyNote = "The overlay only shows a small reel counter. ReelsBreak cannot read or interact with any content in other apps.",
        agreeButtonText = "Allow Overlay",
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Public API — call this from DashboardScreen / FocusScreen etc.
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionBottomSheet(
    type: PermissionSheetType,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false),
    onDismiss: () -> Unit,
    onAgree: () -> Unit,
) {
    val colors = LocalAppColors.current
    val content = buildContent(type)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF1C1B2E),   // slightly lighter than bg so sheet pops
        dragHandle = {
            // Subtle drag handle — signals sheet is dismissible
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 4.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.White.copy(alpha = 0.2f))
            )
        },
        tonalElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(8.dp))

            // ── Icon + Glow ──────────────────────────────────────────────────
            PermissionIconSection(
                icon = content.icon,
                iconTint = content.iconTint,
                glowColor = content.iconGlowColor,
            )

            Spacer(Modifier.height(16.dp))

            // ── Badge (Required / Optional) ──────────────────────────────────
            PermissionBadge(
                label = content.badgeLabel,
                isMandatory = content.isMandatory,
            )

            Spacer(Modifier.height(14.dp))

            // ── Title ────────────────────────────────────────────────────────
            Text(
                text = content.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 30.sp,
            )

            Spacer(Modifier.height(10.dp))

            // ── Description ──────────────────────────────────────────────────
            Text(
                text = content.description,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White.copy(alpha = 0.65f),
                textAlign = TextAlign.Center,
                lineHeight = 21.sp,
            )

            Spacer(Modifier.height(24.dp))

            // ── Steps section ────────────────────────────────────────────────
            StepsSection(steps = content.steps, accentColor = content.iconTint)

            Spacer(Modifier.height(20.dp))

            // ── Privacy note ─────────────────────────────────────────────────
            PrivacyNote(note = content.privacyNote)

            Spacer(Modifier.height(28.dp))

            // ── Action buttons ───────────────────────────────────────────────
            ActionButtons(
                agreeText = content.agreeButtonText,
                accentColor = content.iconTint,
                onDismiss = onDismiss,
                onAgree = onAgree,
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Sub-components
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun PermissionIconSection(
    icon: ImageVector,
    iconTint: Color,
    glowColor: Color,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(96.dp),
    ) {
        // Outer glow ring
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
        // Inner filled circle
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(iconTint.copy(alpha = 0.12f))
                .border(1.5.dp, iconTint.copy(alpha = 0.35f), CircleShape),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(34.dp),
            )
        }
    }
}

@Composable
private fun PermissionBadge(label: String, isMandatory: Boolean) {
    val bgColor = if (isMandatory) Color(0x33E05555) else Color(0x332ECC71)
    val textColor = if (isMandatory) Color(0xFFFF8080) else Color(0xFF2ECC71)
    val borderColor = if (isMandatory) Color(0x66E05555) else Color(0x662ECC71)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp),
    ) {
        Text(
            text = label.uppercase(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            letterSpacing = 1.2.sp,
        )
    }
}

@Composable
private fun StepsSection(steps: List<String>, accentColor: Color) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        // Section header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 2.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.4f),
                modifier = Modifier.size(14.dp),
            )
            Text(
                text = "HOW TO ENABLE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.4f),
                letterSpacing = 1.sp,
            )
        }

        steps.forEachIndexed { index, step ->
            StepRow(number = index + 1, text = step, accentColor = accentColor)
        }
    }
}

@Composable
private fun StepRow(number: Int, text: String, accentColor: Color) {
    val colors = LocalAppColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(colors.cardSurface)
            .border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        // Step number bubble
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(accentColor.copy(alpha = 0.15f))
                .border(1.dp, accentColor.copy(alpha = 0.4f), CircleShape),
        ) {
            Text(
                text = number.toString(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor,
            )
        }
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White.copy(alpha = 0.88f),
            lineHeight = 20.sp,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun PrivacyNote(note: String) {
    val greenColor = Color(0xFF2ECC71)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF0D2B1A))   // very dark green tint — signals "safe"
            .border(1.dp, greenColor.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Icon(
            imageVector = Icons.Outlined.VerifiedUser,
            contentDescription = null,
            tint = greenColor,
            modifier = Modifier
                .size(18.dp)
                .padding(top = 1.dp),
        )
        Text(
            text = note,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            color = greenColor.copy(alpha = 0.85f),
            lineHeight = 19.sp,
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
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // "Not Now" — text button, left side
        TextButton(
            onClick = onDismiss,
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = "Not Now",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.45f),
            )
        }

        // "Agree & Open Settings" — gradient filled pill, right side
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(2f)
                .height(52.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            accentColor,
                            accentColor.copy(alpha = 0.75f),
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
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Shield,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    text = agreeText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }
        }
    }
}