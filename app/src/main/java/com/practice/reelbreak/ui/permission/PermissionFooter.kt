package com.practice.reelbreak.ui.permission

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.reelbreak.R
import com.practice.reelbreak.ui.component.GradientColor

@Composable
fun PermissionFooter(isContinueEnabled: Boolean, onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .padding(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            onClick = onContinue,
            enabled = isContinueEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.White),
                modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .background(
                    brush = if (isContinueEnabled) GradientColor.button
                    else Brush.horizontalGradient(listOf(Color.Gray, Color.DarkGray)),
                    RoundedCornerShape(16.dp))
                    .then(Modifier.border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(16.dp)
                    )
                    ),
            contentPadding = ButtonDefaults.ContentPadding
        ) {
            Text(
                text = "Continue",
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.disable_permissions_anytime),
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White.copy(alpha = 0.5f),
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}