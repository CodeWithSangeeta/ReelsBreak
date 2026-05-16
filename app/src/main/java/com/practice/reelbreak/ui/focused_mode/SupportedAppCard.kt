//package com.practice.reelbreak.ui.focused_mode
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.practice.reelbreak.core.registry.SupportedAppsCatalog
//import com.practice.reelbreak.ui.theme.LocalAppColors
//
//@Composable
//fun SupportedAppsCard(
//    selectedPackages: Set<String>,
//    isEnabled: Boolean = true,
//    onToggle: (String) -> Unit
//) {
//    val colors = LocalAppColors.current
//    val focusApps = SupportedAppsCatalog.apps.filter { it.availableInFocusMode }
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth(),
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        Card(
//            modifier = Modifier.fillMaxWidth(),
//            padding  = PaddingValues(horizontal = 20.dp, vertical = 14.dp)
//        ) {
//            CardSectionTitle("Supported Apps")
//
//            Spacer(Modifier.height(16.dp))
//
//            focusApps.chunked(3).forEach { rowApps ->
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(0.dp)
//                ) {
//                    rowApps.forEach { app ->
//                        val isSelected = selectedPackages.contains(app.packageName)
//
//                        Column(
//                            modifier = Modifier
//                                .weight(1f)
//                                .then(
//                                    if (isEnabled) Modifier.clickable(
//                                        interactionSource = remember { MutableInteractionSource() },
//                                        indication        = null
//                                    ) { onToggle(app.packageName) } else Modifier
//                                )
//                                .padding(vertical = 8.dp),
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            verticalArrangement = Arrangement.spacedBy(6.dp)
//                        ) {
//                            Box(contentAlignment = Alignment.TopEnd) {
//                                // App icon — subtle selected ring
//                                Box(
//                                    modifier = Modifier
//                                        .size(52.dp)
//                                        .clip(RoundedCornerShape(16.dp))
//                                        .then(
//                                            if (isSelected)
//                                                Modifier.border(
//                                                    width = 2.dp,
//                                                    brush = Brush.linearGradient(
//                                                        listOf(colors.purplePrimary, colors.blueAccent)
//                                                    ),
//                                                    shape = RoundedCornerShape(16.dp)
//                                                )
//                                            else
//                                                Modifier.border(
//                                                    width = 1.dp,
//                                                    brush = Brush.linearGradient(
//                                                        listOf(
//                                                            colors.borderSubtle.copy(alpha = 0.40f),
//                                                            colors.borderSubtle.copy(alpha = 0.20f)
//                                                        )
//                                                    ),
//                                                    shape = RoundedCornerShape(16.dp)
//                                                )
//                                        )
//                                        .background(
//                                            if (isSelected)
//                                                colors.purplePrimary.copy(alpha = if (colors.isDark) 0.12f else 0.07f)
//                                            else
//                                                Color.Transparent
//                                        ),
//                                    contentAlignment = Alignment.Center
//                                ) {
//                                    Image(
//                                        painter            = painterResource(id = app.iconRes),
//                                        contentDescription = app.displayName,
//                                        modifier           = Modifier.size(34.dp)
//                                    )
//                                }
//
//                                // Checkmark badge — only when selected
//                                if (isSelected) {
//                                    Box(
//                                        modifier = Modifier
//                                            .size(16.dp)
//                                            .clip(CircleShape)
//                                            .background(
//                                                Brush.linearGradient(
//                                                    listOf(colors.purplePrimary, colors.blueAccent)
//                                                )
//                                            )
//                                            .border(1.5.dp, colors.background, CircleShape),
//                                        contentAlignment = Alignment.Center
//                                    ) {
//                                        Text(
//                                            text       = "✓",
//                                            color      = Color.White,
//                                            fontSize   = 8.sp,
//                                            fontWeight = FontWeight.Bold
//                                        )
//                                    }
//                                }
//                            }
//
//                            Text(
//                                text       = app.displayName,
//                                color      = if (isSelected) colors.textPrimary else colors.textSecondary,
//                                fontSize   = 11.sp,
//                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
//                                maxLines   = 1
//                            )
//                        }
//                    }
//                    // Fill empty slots in last row
//                    repeat(3 - rowApps.size) { Spacer(modifier = Modifier.weight(1f)) }
//                }
//            }
//        }
//    }
//}



package com.practice.reelbreak.ui.focused_mode

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.reelbreak.core.registry.SupportedAppsCatalog
import com.practice.reelbreak.ui.theme.LocalAppColors

@Composable
fun SupportedAppsCard(
    selectedPackages: Set<String>,
    isEnabled: Boolean = true,
    onToggle: (String) -> Unit
) {
    val colors = LocalAppColors.current
    val focusApps = SupportedAppsCatalog.apps.filter { it.availableInFocusMode }

    Card(
        modifier = Modifier.fillMaxWidth(),
        padding = PaddingValues(horizontal = 18.dp, vertical = 18.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            CardSectionTitle("Apps to block")

            Text(
                text = "Choose the apps that should stay blocked during your focus session.",
                color = colors.textSecondary,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )

            focusApps.chunked(3).forEach { rowApps ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowApps.forEach { app ->
                        val isSelected = selectedPackages.contains(app.packageName)

                        SupportedAppTile(
                            name = app.displayName,
                            iconRes = app.iconRes,
                            isSelected = isSelected,
                            isEnabled = isEnabled,
                            onClick = { onToggle(app.packageName) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    repeat(3 - rowApps.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun SupportedAppTile(
    name: String,
    iconRes: Int,
    isSelected: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current

    val tileBackground = when {
        isSelected && colors.isDark -> Brush.linearGradient(
            listOf(
                colors.purplePrimary.copy(alpha = 0.20f),
                colors.blueAccent.copy(alpha = 0.14f)
            )
        )
        isSelected -> Brush.linearGradient(
            listOf(
                colors.purplePrimary.copy(alpha = 0.10f),
                colors.blueAccent.copy(alpha = 0.06f)
            )
        )
        else -> colors.cardSurface
//            Brush.linearGradient(
//            listOf(colors.cardSurface, colors.cardSurface)
       // )
    }

    val borderBrush = if (isSelected) {
        Brush.linearGradient(listOf(colors.purplePrimary, colors.blueAccent))
    } else {
        Brush.linearGradient(
            listOf(
                colors.borderSubtle.copy(alpha = 0.55f),
                colors.borderSubtle.copy(alpha = 0.28f)
            )
        )
    }

    Column(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(20.dp))
            .background(tileBackground)
            .border(
                width = if (isSelected) 1.4.dp else 1.dp,
                brush = borderBrush,
                shape = RoundedCornerShape(20.dp)
            )
            .then(
                if (isEnabled) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onClick
                    )
                } else Modifier
            )
            .padding(horizontal = 10.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(56.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        if (colors.isDark)
                            Color.White.copy(alpha = if (isSelected) 0.10f else 0.05f)
                        else
                            Color.White.copy(alpha = if (isSelected) 0.90f else 0.72f)
                    )
                    .border(
                        width = 1.dp,
                        color = if (isSelected)
                            colors.purplePrimary.copy(alpha = 0.24f)
                        else
                            colors.borderSubtle.copy(alpha = 0.22f),
                        shape = RoundedCornerShape(18.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = name,
                    modifier = Modifier.size(34.dp)
                )
            }

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(colors.purplePrimary, colors.blueAccent)
                            )
                        )
                        .border(1.5.dp, colors.cardSurface, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✓",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = name,
            color = if (isSelected) colors.textPrimary else colors.textSecondary,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 2,
            lineHeight = 15.sp
        )
    }
}