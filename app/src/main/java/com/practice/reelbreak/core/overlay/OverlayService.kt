package com.practice.reelbreak.core.overlay


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.practice.reelbreak.R
import com.practice.reelbreak.data.preferences.UserPreferencesRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.lifecycle.ViewTreeLifecycleOwner

@AndroidEntryPoint
class OverlayService : Service() {

    @Inject
    lateinit var userPrefs: UserPreferencesRepository

    private lateinit var windowManager: WindowManager
    private var composeView: ComposeView? = null
   // private lateinit var composeView: ComposeView

    override fun onCreate() {
        super.onCreate()
        Log.d("OVERLAY_SERVICE", "onCreate")
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        try {
            createComposeOverlay()
        } catch (e: Exception) {
            Log.e("OVERLAY_SERVICE", "Error initializing overlay: ${e.message}", e)
            stopSelf() // fail gracefully instead of crashing app
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        composeView?.let {
            try {
                windowManager.removeView(it)
            } catch (e: Exception) {
                Log.e("OVERLAY_SERVICE", "removeView failed: ${e.message}", e)
            }
        }
        composeView = null
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createComposeOverlay() {
        val view = ComposeView(this)

        val lifecycleOwner = object : LifecycleOwner {
            private val lifecycleRegistry = LifecycleRegistry(this)
            override val lifecycle: Lifecycle
                get() = lifecycleRegistry

            init {
                // Mark as started so Compose can run
                lifecycleRegistry.currentState = Lifecycle.State.STARTED
            }
        }

        // 2) Attach LifecycleOwner to the view tree
        ViewTreeLifecycleOwner.set(view, lifecycleOwner)

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
        )

        params.gravity = Gravity.TOP or Gravity.END
        params.x = 24
        params.y = 200

        view.setContent {
            val reels by userPrefs.reelsWatchedToday.collectAsState(initial = 0)
            val mins by userPrefs.timeSpentTodayMinutes.collectAsState(initial = 0)
            android.util.Log.d("OVERLAY_DEBUG", "bubble reels=$reels time=$mins")
            OverlayBubble(
                reelsCount = reels,
                minutes = mins
            )
        }

        windowManager.addView(view, params)
        Log.d("OVERLAY_SERVICE", "addView done")
    }
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, OverlayService::class.java)
            context.startService(intent)
        }

        fun stop(context: Context) {
            val intent = Intent(context, OverlayService::class.java)
            context.stopService(intent)
        }
    }
}
