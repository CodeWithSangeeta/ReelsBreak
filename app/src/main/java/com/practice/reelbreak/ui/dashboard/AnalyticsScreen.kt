package com.practice.reelbreak.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.practice.reelbreak.ui.component.GradientColor
import com.practice.reelbreak.ui.component.MainScaffold

@Composable
fun AnalyticsScreen(
    navController: NavController,
    selectedTab: Int = 1,
    onTabSelected: (Int) -> Unit = {}
) {
    MainScaffold(selectedTab, onTabSelected) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            item { Text("Analytics", fontSize = 32.sp, fontWeight = FontWeight.Bold) }
            items(50) { index ->
              //  AnalyticsCardItem(index)  // Your chart/data cards
            }
        }
    }
}

