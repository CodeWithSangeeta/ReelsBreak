package com.practice.reelbreak.ui.focused_mode

import com.practice.reelbreak.ui.focusedmode.FocusViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practice.reelbreak.R

@Composable
fun StartFocusScreen(
    viewModel: FocusViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val selectedTime by viewModel.selectedTime.collectAsState()
    val blockedApps by viewModel.blockedApps.collectAsState()
    val isFocusActive by viewModel.isFocusActive.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2A0B4D),
                        Color(0xFF4A1C7A)
                    )
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Top Section - Icon & Title
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground), // Use your icon
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

            // Timer Options
            TimerOptionRow(
                selectedTime = selectedTime,
                onTimeSelected = { minutes -> viewModel.selectTime(minutes) }
            )

            // App Blocking Section
            AppBlockingCard(
                blockedApps = blockedApps,
                onAppToggled = { app -> viewModel.toggleApp(app) }
            )

            // Motivational Quote
            QuoteCard()

            // Primary Action Button
            Button(
                onClick = {
                    if (!isFocusActive) {
                        viewModel.startFocus()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(30.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF8B2CE8), Color(0xFF581176))
                            ),
                            shape = RoundedCornerShape(30.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Start Focus Mode",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
