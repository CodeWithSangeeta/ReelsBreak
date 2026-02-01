package com.sangeeta.reelbreak.ui.permission

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.reelbreak.viewmodel.MainViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.sangeeta.reelbreak.core.permission.AccessibilityPermissionChecker
import com.sangeeta.reelbreak.core.permission.UsagePermissionChecker

@Composable
fun PermissionEducationScreen(
    viewModel: MainViewModel,
    onContinue: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        Log.d("PERMISSION", "PermissionEducationScreen launched")
    }


    LaunchedEffect(Unit) {
        viewModel.refreshPermissionState(context)
    }

    //UI — handle permission actions
    LaunchedEffect(Unit) {
        viewModel.permissionAction.collect { action ->
            Log.d("PERMISSION", "Action received: $action")

            when (action) {
                PermissionAction.OpenAccessibilitySettings -> {
                    AccessibilityPermissionChecker.openAccessibilitySettings(context)
                }
                PermissionAction.OpenUsageAccessSettings -> {
                    UsagePermissionChecker.openUsageAccessSettings(context)
                }
            }
        }
    }




    //Observe App Lifecycle
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {

        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                // User returned from Settings
                viewModel.refreshPermissionState(context)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    val permissionState by viewModel.permissionState.collectAsState()

    val permissions = listOf(
        PermissionUiModel(
            title = "Accessibility Access",
            description = "Required to detect reel scrolling and show mindful reminders.",
            icon = Icons.Default.Visibility,
            isGranted = permissionState.accessibilityGranted
        ),
        PermissionUiModel(
            title = "Usage Access",
            description = "Used to measure time spent on short-form content.",
            icon = Icons.Default.BarChart,
            isGranted = permissionState.usageStatsGranted
        )
    )

    val canContinue =
        permissionState.accessibilityGranted &&
                permissionState.usageStatsGranted


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Permissions",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        permissions.forEach { model ->
            PermissionCard(
                model = model,
                onEnableClick = {
                    if (model.title == "Accessibility Access") {
                        viewModel.requestAccessibilityPermission()
                    } else {
                        viewModel.requestUsagePermission()
                    }
                }
            )
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onContinue,
            enabled = canContinue,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(text = "Can continue = $canContinue")

            Text(if (canContinue) "Continue" else "Grant required permissions")
        }
    }

}
