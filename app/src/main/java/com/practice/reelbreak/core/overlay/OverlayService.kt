package com.practice.reelbreak.core.overlay

// ============================================================
// ✅ BANKING APP FIX — OverlayService DISABLED
// ============================================================
// This entire service has been commented out because it was
// using TYPE_APPLICATION_OVERLAY + FLAG_LAYOUT_IN_SCREEN +
// FLAG_LAYOUT_NO_LIMITS — all three of which banking app
// security scanners (like those used by HDFC, SBI, ICICI,
// PhonePe, GPay) flag as suspicious/malware overlay behavior.
//
// The overlay UI is already handled safely inside
// ReelsAccessibilityService using TYPE_ACCESSIBILITY_OVERLAY
// which is scoped only to accessibility services and is NOT
// flagged by banking apps.
//
// HOW TO RE-ENABLE IN FUTURE (if needed):
//   1. Change TYPE_APPLICATION_OVERLAY → TYPE_ACCESSIBILITY_OVERLAY
//   2. Remove FLAG_LAYOUT_IN_SCREEN and FLAG_LAYOUT_NO_LIMITS
//   3. Remove SYSTEM_ALERT_WINDOW permission from Manifest
//   4. Re-uncomment this file
// ============================================================

/*
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import com.practice.reelbreak.data.preferences.UserPreferencesRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OverlayService : Service() {

    @Inject
    lateinit var userPrefs: UserPreferencesRepository

    private lateinit var windowManager: WindowManager
    private var composeView: ComposeView? = null

    override fun onCreate() {
        super.onCreate()
        Log.d("OVERLAY_SERVICE", "onCreate")
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        try {
            createComposeOverlay()
        } catch (e: Exception) {
            Log.e("OVERLAY_SERVICE", "Error in onCreate: ${e.message}", e)
            stopSelf()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            composeView?.let { windowManager.removeView(it) }
        } catch (e: Exception) {
            Log.e("OVERLAY_SERVICE", "Error in onDestroy: ${e.message}", e)
        } finally {
            composeView = null
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createComposeOverlay() {
        val view = ComposeView(this)
        view.setContent {
            val reels by userPrefs.reelsWatchedToday.collectAsState(initial = 0)
            val mins by userPrefs.timeSpentTodayMinutes.collectAsState(initial = 0)
            Log.d("OVERLAY_DEBUG", "bubble reels=$reels time=$mins")
            OverlayBubble(reelsCount = reels, minutes = mins)
        }
        composeView = view

        // ❌ THESE FLAGS CAUSED BANKING APP DETECTION — DO NOT USE:
        //    TYPE_APPLICATION_OVERLAY  → flagged as malware overlay
        //    FLAG_LAYOUT_IN_SCREEN     → flagged as trying to draw over secure windows
        //    FLAG_LAYOUT_NO_LIMITS     → flagged as bypassing system window bounds
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY  // ❌ DO NOT USE
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or      // ❌ DO NOT USE
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,        // ❌ DO NOT USE
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.END
            x = 24
            y = 200
        }
        windowManager.addView(view, params)
        Log.d("OVERLAY_SERVICE", "addView done")
    }
}
*/