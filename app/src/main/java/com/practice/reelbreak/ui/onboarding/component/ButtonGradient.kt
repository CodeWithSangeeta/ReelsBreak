package com.practice.reelbreak.ui.onboarding.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.practice.reelbreak.ui.component.GradientColor


@Composable
fun ButtonGradient(
    text: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(36.dp),
                ambientColor = Color(0x99000000),
                spotColor = Color(0x33000000)
            ),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(),
        shape = RoundedCornerShape(14.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    GradientColor.button,
                    RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Row {
                Text(
                    text = text,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }

}
