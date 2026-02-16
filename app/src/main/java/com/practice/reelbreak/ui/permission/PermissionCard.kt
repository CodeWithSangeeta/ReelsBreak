package com.practice.reelbreak.ui.permission

import android.R.attr.icon
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionCard(
    model: PermissionUiModel,
    isExpanded: Boolean,
    onExpandedToggle: () -> Unit,
    onEnableClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.7f)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = model.icon,
                    contentDescription = model.title,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = model.title,
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )

                if (model.isOptional) {
                    Badge(containerColor = Color(0xFF27AE60)) {
                        Text("Optional", color = Color.White, fontSize = 11.sp)
                    }
                }

            }

            Text(
                modifier = Modifier.padding(start = 40.dp),
                text = model.description,
                fontSize = 15.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onEnableClick,
                enabled = model.status is PermissionStatus.NotGranted,
                modifier = Modifier.padding(start = 40.dp),
                colors = if (model.status is PermissionStatus.Granted) {
                    ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0C6F3E)
                    )
                } else {
                    ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8E44AD)
                    )
                }
            ) {
                Text(
                    text = if (model.status is PermissionStatus.Granted) "Enabled ✓" else "Enable",
                    color = Color.White
                )
            }

            TextButton(onClick = onExpandedToggle) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isExpanded) "Hide details" else "Learn more",
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ArrowDropDown else Icons.Default.ArrowRight,
                        contentDescription = model.title,
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // EXPANDABLE BULLET POINTS
            if (isExpanded && model.bulletPoints.isNotEmpty()) {
                model.bulletPoints.forEach { bullet ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = bullet.contains("No") || bullet.contains("Detect"),
                            onCheckedChange = null,
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF2ECC71),
                                uncheckedColor = Color(0xFFE74C3C)
                            )
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            bullet, style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }

        }
    }
}
