package com.practice.reelbreak.ui.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import com.practice.reelbreak.ui.theme.PremiumSectionTitle

@Composable
fun SectionTitle(
    title: String,
    subtitle: String
) {
    PremiumSectionTitle(
        title = title,
        subtitle = subtitle,
        modifier = Modifier.padding(horizontal = 2.dp)
    )
}