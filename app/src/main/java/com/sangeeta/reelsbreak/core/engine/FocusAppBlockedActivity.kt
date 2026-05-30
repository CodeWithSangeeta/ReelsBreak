//package com.practice.reelbreak.core.engine
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.addCallback
//import androidx.activity.compose.setContent
//import com.practice.reelbreak.MainActivity
//import com.practice.reelbreak.ui.focused_mode.AppBlockedScreen
//
//class FocusAppBlockedActivity : ComponentActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        val blockedPackage = intent.getStringExtra("blocked_package") ?: ""
//        val remainingFormatted = intent.getStringExtra("remaining_formatted") ?: ""
//        val focusEndTs = intent.getLongExtra("focus_end_ts", 0L)
//
//        onBackPressedDispatcher.addCallback(this) {
//            goToHomeScreen()
//        }
//
//        setContent {
//            AppBlockedScreen(
//                blockedPackage = blockedPackage,
//                remainingFormatted = remainingFormatted,
//                focusEndTs = focusEndTs,
//                onCloseApp = {
//                    goToHomeScreen()
//                },
//                onOpenFocusMode = {
//                    openFocusMode()
//                }
//            )
//        }
//    }
//
//    private fun goToHomeScreen() {
//        val homeIntent = Intent(Intent.ACTION_MAIN).apply {
//            addCategory(Intent.CATEGORY_HOME)
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        }
//        startActivity(homeIntent)
//        finish()
//    }
//
//    private fun openFocusMode() {
//        val intent = Intent(this, MainActivity::class.java).apply {
//            putExtra("open_tab", 1) // 1 = Focus tab
//            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//        }
//        startActivity(intent)
//        finish()
//    }
//}