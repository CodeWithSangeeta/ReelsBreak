package com.practice.reelbreak.ui.focused_mode


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.practice.reelbreak.ui.theme.ReelBreakTheme

class AppBlockedActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val blockedPackage = intent.getStringExtra("blocked_package") ?: ""
        val remainingFormatted = intent.getStringExtra("remaining_formatted") ?: ""
        val focusEndTs = intent.getLongExtra("focus_end_ts", 0L)

        setContent {
         //   ReelBreakTheme {
                AppBlockedScreen(
                    blockedPackage = blockedPackage,
                    remainingFormatted = remainingFormatted,
                    focusEndTs = focusEndTs,
                    onGoBack = { finish() }
                )
            //}
        }
    }

    // Prevent back press from going back to blocked app
    @Deprecated("Deprecated")
    override fun onBackPressed() {
        // Go to home instead of blocked app
        val homeIntent = android.content.Intent(android.content.Intent.ACTION_MAIN).apply {
            addCategory(android.content.Intent.CATEGORY_HOME)
            flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(homeIntent)
    }
}