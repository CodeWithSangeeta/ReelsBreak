package com.practice.reelbreak.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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

// ui/analytics/AnalyticsScreen.kt
@Composable
fun AnalyticsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GradientColor.background)
            .padding(24.dp)
    ) {
        Text("📊 Analytics", fontSize = 32.sp, fontWeight = FontWeight.Bold)

        // Placeholder charts - replace with real data later
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Today vs Yesterday", fontSize = 18.sp)
                // Add BarChart/LineChart here later
                Text("No data yet - grant permissions first ✨")
            }
        }
    }
}
