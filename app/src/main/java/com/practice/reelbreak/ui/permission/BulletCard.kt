package com.practice.reelbreak.ui.permission

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BulletCard(
    bulletPoints: List<Pair<String, BulletIconType>>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(0.6f)),
        shape = RoundedCornerShape(16.dp)) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            bulletPoints.forEach { (bullet, iconType) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = when (iconType) {
                            BulletIconType.Check -> Icons.Default.CheckCircle
                            BulletIconType.Cross -> Icons.Default.Close
                            BulletIconType.Question -> Icons.Default.HelpOutline
                        },
                        contentDescription = null,
                        tint = when (iconType) {
                            BulletIconType.Check -> Color(0xFF2ECC71)
                            BulletIconType.Cross -> Color(0xFFE74C3C)
                            BulletIconType.Question -> Color.White.copy(alpha = 0.7f)
                        },
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = bullet,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.95f),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
