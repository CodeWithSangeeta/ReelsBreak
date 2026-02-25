package com.practice.reelbreak.ui.focused_mode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practice.reelbreak.R

// Reusable Composable: FocusScreen.kt
@Composable
fun StartFocusScreen(
  //  viewModel: FocusViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
//    val selectedTime by viewModel.selectedTime
//    val blockedApps by viewModel.blockedApps
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    0f to Color(0xFF2A0B4D),
                    1f to Color(0xFF4A1C7A)
                )
            )
            .padding(16.dp)
            .clip(RoundedCornerShape(24.dp)) // Rounded screen corners
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Top Section
            Icon(
                painter = painterResource(id = R.drawable.page_tracker_img), // Add shield icon to drawable
                contentDescription = "Focus Shield",
                modifier = Modifier.size(80.dp),
                tint = Color(0xFFB77CFF)
            )
            Text(
                text = "Start Focusing",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = "Block distractions and boost productivity",
                color = Color(0xFFB0B0D0),
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )

            // Timer Options Row
//            TimerOptionRow(
//                selectedTime = selectedTime,
//                onTimeSelected = { minutes -> viewModel.selectTime(minutes) }
//            )

            // App Blocking Section Card
//            AppBlockingCard(
//                blockedApps = blockedApps,
//                onAppToggled = { app -> viewModel.toggleApp(app) }
//            )

            // Motivational Quote Card
            QuoteCard()

            // Primary Action Button
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(30.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    "Start Focus Mode",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
