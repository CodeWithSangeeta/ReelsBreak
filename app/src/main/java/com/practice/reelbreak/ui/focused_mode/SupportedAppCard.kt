package com.practice.reelbreak.ui.focused_mode

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.reelbreak.core.registry.SupportedAppsCatalog
import com.practice.reelbreak.ui.theme.LocalAppColors

@Composable
fun SupportedAppsCard(
    selectedPackages: Set<String>,
    isEnabled: Boolean = true,
    onToggle: (String) -> Unit
) {
    val colors = LocalAppColors.current
    val focusApps = SupportedAppsCatalog.apps.filter { it.availableInFocusMode }
    val selectedApps = focusApps.filter { selectedPackages.contains(it.packageName) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        SelectedAppsPreview(
            selectedApps = selectedApps,
            isEnabled = isEnabled,
            onRemove = { pkg -> onToggle(pkg) }
        )
      Spacer(modifier = Modifier.height(22.dp))
        Text(
            text = "Supported Apps",
            modifier = Modifier.fillMaxWidth(),
            color = colors.textPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Choose the apps you want to block",
            modifier = Modifier.fillMaxWidth(),
            color = colors.textSecondary,
            fontSize = 12.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.padding(top = 2.dp))

        focusApps.chunked(6).forEach { rowApps ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                rowApps.forEach { app ->
                    val isSelected = selectedPackages.contains(app.packageName)

                    AppIconItem(
                        name = app.displayName,
                        iconRes = app.iconRes,
                        isSelected = isSelected,
                        isEnabled = isEnabled,
                        onClick = { onToggle(app.packageName) },
                        modifier = Modifier.weight(1f)
                    )
                }

                repeat(6 - rowApps.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}





@Composable
 fun AppIconItem(
    name: String,
    iconRes: Int,
    isSelected: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current

    Column(
        modifier = modifier
            .then(
                if (isEnabled) Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                ) else Modifier
            )
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(contentAlignment = Alignment.TopEnd) {

                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = name,
                    modifier = Modifier
                        .size( 42.dp )
                        .clip(CircleShape)
                )
        }

        Text(
            text = name,
            color = colors.textPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}