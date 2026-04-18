package com.practice.reelbreak.ui.permission

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.reelbreak.ui.theme.LocalAppColors

@Composable
fun HelpSection() {
    var showSteps by remember { mutableStateOf(false) }
    val colors = LocalAppColors.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF805A5F).copy(alpha = 0.6f)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
        shape = RoundedCornerShape(24.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {  Box(modifier = Modifier
                .height(36.dp)
                .width(40.dp)
                .background(brush = colors.button, RoundedCornerShape(8.dp))) {
                Icon(
                    imageVector =Icons.Default.HelpOutline,
                    contentDescription = "Need Help",
                    tint = Color.White,
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.Center)
                )
            }
                Text(
                    text = "Need Help?",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )

            }
            Text(
                modifier = Modifier.padding(start = 50.dp,top =8.dp),
                text = "If the app closes after enabling permission...",
                fontSize = 15.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                modifier = Modifier.padding(start = 50.dp,top =8.dp),
                text = "Some devices restart apps after enabling Accessibility.",
                fontSize = 15.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                modifier = Modifier.padding(start = 50.dp,top =8.dp),
                text = "Simply reopen ReelsGuard – your settings will be saved.",
                fontSize = 15.sp,
                color = Color.White.copy(alpha = 0.7f)
            )

            TextButton(onClick = { showSteps = !showSteps },
                modifier = Modifier.align(Alignment.Start)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (showSteps) "Hide steps" else "Show steps",
                        color = Color(0xFFE78060),
                        fontSize = 14.sp
                    )
                    Spacer(modifier=Modifier.width(2.dp))
                    Icon(
                        imageVector = if (showSteps) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFFE78060),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            if (showSteps) {
                Column(modifier = Modifier.padding(start = 16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(
                        "1. Tap Enable",
                        "2. Find ReelsBreak in Accessibility list",
                        "3. Turn ON the toggle",
                        "4. Press back to return to the app"
                    ).forEach { step ->
                        Text(step, fontSize = 13.sp,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.95f),)
                    }
                }
            }
        }
    }
}