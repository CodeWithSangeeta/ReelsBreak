package com.practice.reelbreak.ui.permission

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PermissionCard(
    model: PermissionUiModel,
    onEnableClick: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
           .glassmorphism() // Our custom glass effect
            .padding(16.dp)
            .animateContentSize() // Smoothly expand when "Learn more" is clicked
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = model.title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            if (model.isOptional) {
                Spacer(Modifier.width(8.dp))
                Badge { Text("Optional") }
            }
        }

        Text(
            text = model.description,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 4.dp)
        )


        Button(
            onClick = onEnableClick,
            enabled = model.status is PermissionStatus.NotGranted,
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (model.id == PermissionType.ACCESSIBILITY)
                    Color(0xFF8E44AD) else Color(0xFF2E86C1)
            )
        ) {
            Text(if (model.status is PermissionStatus.Granted) "Enabled ✓" else "Enable")
        }


    }
}