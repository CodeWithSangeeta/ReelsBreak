package com.practice.reelbreak.core.overlay

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
            OverlayBubble(
                reelsCount = reels,
                minutes = mins
            )
        }

        composeView = view

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.END
            x = 24
            y = 200
        }

        windowManager.addView(view, params)
        Log.d("OVERLAY_SERVICE", "addView done")
    }

//    companion object {
//        fun start(context: Context) {
//            Log.d("OVERLAY_SERVICE", "start() called")
//            val intent = Intent(context, OverlayService::class.java)
//            context.startService(intent)
//        }
//
//        fun stop(context: Context) {
//            Log.d("OVERLAY_SERVICE", "stop() called")
//            val intent = Intent(context, OverlayService::class.java)
//            context.stopService(intent)
//        }
//    }
}