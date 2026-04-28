package com.practice.reelbreak.core.accessibility

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.practice.reelbreak.ReelBreakApplication
import com.practice.reelbreak.core.action.ActionController
import com.practice.reelbreak.core.detection.AppDetectorRouter
import com.practice.reelbreak.core.detection.ReelsDetectionManager
import com.practice.reelbreak.core.engine.BlockingDecisionEngine
import android.graphics.PixelFormat
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import com.practice.reelbreak.core.registry.SupportedAppsRegistry
import com.practice.reelbreak.data.FocusStateHolder
import com.practice.reelbreak.data.preferences.UserPreferencesRepository
import com.practice.reelbreak.domain.model.ActiveBlockMode
import com.practice.reelbreak.ui.focused_mode.AppBlockedActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

//class ReelsAccessibilityService : AccessibilityService() {
//
//    private lateinit var detectionManager: ReelsDetectionManager
//
//    override fun onServiceConnected() {
//        super.onServiceConnected()
//        val repository = (applicationContext as ReelBreakApplication).repository
//        val engine = BlockingDecisionEngine(repository)
//        val actionController = ActionController(this)
//        detectionManager = ReelsDetectionManager(actionController, engine)
//        Log.d("REELSBREAK", "Service connected ✅")
//    }
//
//
//    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
//        if (event == null) return
//        val rootNode: AccessibilityNodeInfo? = rootInActiveWindow
//        detectionManager.processEvent(event, rootNode)
//    }
//
//    override fun onInterrupt() {
//        Log.d("REELSBREAK", "Service interrupted")
//        AppDetectorRouter.resetAll()
//    }
//}






//class ReelsAccessibilityService : AccessibilityService() {
//
//    private lateinit var detectionManager: ReelsDetectionManager
//
//    // Overlay-related fields
//    private var windowManager: WindowManager? = null
//    private var overlayView: View? = null
//    private var userPrefs: UserPreferencesRepository? = null
//
//    override fun onServiceConnected() {
//        super.onServiceConnected()
//
//        val app = applicationContext as ReelBreakApplication
//        val repository = app.repository
//        userPrefs = repository
//
//        val engine = BlockingDecisionEngine(repository)
//        val actionController = ActionController(this)
//        detectionManager = ReelsDetectionManager(actionController, engine)
//
//        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
//
//        Log.d("REELSBREAK", "Service connected ✅")
//    }
//
//    private val hideHandler = Handler(Looper.getMainLooper())
//    private val hideRunnable = Runnable { hideOverlay() }
//    private val HIDE_DELAY_MS = 1500L  // 1.5 seconds debounce
//
//    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
//        if (event == null) return
//
//        val rootNode: AccessibilityNodeInfo? = rootInActiveWindow
//        val packageName = event.packageName?.toString()
//
//        // Run detection FIRST so session.reelsMode is updated
//        detectionManager.processEvent(event, rootNode)
//
//        val isOverlayConditionMet = shouldShowOverlayForApp(packageName)
//        val isOnReels = detectionManager.isOnReelsScreen
//
//        if (isOverlayConditionMet && isOnReels) {
//            // Cancel any pending hide, show overlay
//            hideHandler.removeCallbacks(hideRunnable)
//            showOverlayIfNeeded()
//        } else {
//            // Debounce the hide — don't flicker on brief non-reels events
//            hideHandler.removeCallbacks(hideRunnable)
//            hideHandler.postDelayed(hideRunnable, HIDE_DELAY_MS)
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        hideHandler.removeCallbacks(hideRunnable)
//        hideOverlay()
//    }
//
//    override fun onInterrupt() {
//        Log.d("REELSBREAK", "Service interrupted")
//        AppDetectorRouter.resetAll()
//        hideOverlay()
//    }
//
//    // ───────────────────── Overlay helpers ─────────────────────
//
//    private fun shouldShowOverlayForApp(packageName: String?): Boolean {
//        if (packageName == null) return false
//
//        if (!SupportedAppsRegistry.isSupported(packageName)) {
//            Log.d("OVERLAY_DEBUG", "app not supported: $packageName")
//            return false
//        }
//
//        val prefs = userPrefs ?: run {
//            Log.d("OVERLAY_DEBUG", "userPrefs is null")
//            return false
//        }
//
//        val activeMode = prefs.getActiveModeBlocking()
//        val isOverlayEnabled = prefs.isOverlayEnabledBlocking()
//
//        Log.d(
//            "OVERLAY_DEBUG",
//            "shouldShowOverlayForApp pkg=$packageName activeMode=$activeMode overlayEnabled=$isOverlayEnabled"
//        )
//
//        return isOverlayEnabled && activeMode == ActiveBlockMode.LIMIT.value
//    }
//
//
//    private var overlayReelsText: TextView? = null
//    private var overlayTimeText: TextView? = null
//    private var overlayScope: CoroutineScope? = null
//
//    private fun showOverlayIfNeeded() {
//        val wm = windowManager ?: run {
//            Log.d("OVERLAY_DEBUG", "windowManager null")
//            return
//        }
//        val prefs = userPrefs ?: run {
//            Log.d("OVERLAY_DEBUG", "userPrefs null")
//            return
//        }
//        if (overlayView != null) {
//            Log.d("OVERLAY_DEBUG", "overlay already shown")
//            return
//        }
//
//        Log.d("OVERLAY_DEBUG", "creating plain view overlay")
//
//        // Root container
//        val layout = LinearLayout(this).apply {
//            orientation = LinearLayout.VERTICAL
//            background = GradientDrawable().apply {
//                setColor(0xCC111111.toInt())
//                cornerRadius = 32f
//            }
//            setPadding(32, 20, 32, 20)
//            elevation = 10f
//        }
//
//        // "Reels: X" text
//        val reelsText = TextView(this).apply {
//            text = "Reels: 0"
//            setTextColor(0xFFFFFFFF.toInt())
//            textSize = 13f
//            setTypeface(typeface, Typeface.BOLD)
//        }
//
//        // "Time: Xm" text
//        val timeText = TextView(this).apply {
//            text = "Time: 0m"
//            setTextColor(0xFFBBBBBB.toInt())
//            textSize = 12f
//        }
//
//        layout.addView(reelsText)
//        layout.addView(timeText)
//
//        overlayReelsText = reelsText
//        overlayTimeText = timeText
//        overlayView = layout
//
//        // Collect Flow updates on Main dispatcher — no Compose needed
//        overlayScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
//        overlayScope?.launch {
//            prefs.reelsWatchedToday.collect { count ->
//                Log.d("OVERLAY_DEBUG", "reels updated: $count")
//                reelsText.text = "Reels: $count"
//            }
//        }
//        overlayScope?.launch {
//            prefs.timeSpentTodayMinutes.collect { mins ->
//                Log.d("OVERLAY_DEBUG", "time updated: $mins")
//                timeText.text = "Time: ${mins}m"
//            }
//        }
//
//        val params = WindowManager.LayoutParams(
//            WindowManager.LayoutParams.WRAP_CONTENT,
//            WindowManager.LayoutParams.WRAP_CONTENT,
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//                WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
//            else
//                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
//            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
//                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
//            PixelFormat.TRANSLUCENT
//        ).apply {
//            gravity = Gravity.TOP or Gravity.END
//            x = 24
//            y = 200
//        }
//
//        wm.addView(layout, params)
//        Log.d("OVERLAY_SERVICE", "plain overlay added successfully")
//    }
//
//
//
//    private fun hideOverlay() {
//        val wm = windowManager ?: return
//        val view = overlayView ?: return
//        try {
//            wm.removeView(view)
//        } catch (e: Exception) {
//            Log.e("OVERLAY_SERVICE", "removeView failed: ${e.message}", e)
//        } finally {
//            overlayScope?.cancel()
//            overlayScope = null
//            overlayView = null
//            overlayReelsText = null
//            overlayTimeText = null
//        }
//    }
//}





class ReelsAccessibilityService : AccessibilityService() {

    private lateinit var detectionManager: ReelsDetectionManager

    private var windowManager: WindowManager? = null
    private var overlayView: View? = null
    private var userPrefs: UserPreferencesRepository? = null

    override fun onServiceConnected() {
        super.onServiceConnected()

        val app = applicationContext as ReelBreakApplication
        val repository = app.repository
        userPrefs = repository

        val engine = BlockingDecisionEngine(repository)
        val actionController = ActionController(this)
        detectionManager = ReelsDetectionManager(actionController, engine)

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        Log.d("REELSBREAK", "Service connected ✅")
    }

    private val hideHandler = Handler(Looper.getMainLooper())
    private val hideRunnable = Runnable { hideOverlay() }
    private val HIDE_DELAY_MS = 1500L

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        // ── Focus Mode blocking (TYPE_WINDOW_STATE_CHANGED only) ────────────
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val pkg = event.packageName?.toString() ?: return

            if (
                FocusStateHolder.isFocusActive &&
                FocusStateHolder.blockedPackages.contains(pkg) &&
                pkg != applicationContext.packageName
            ) {
                if (FocusStateHolder.getRemainingMillis() > 0L) {
                    launchBlockedScreen(pkg)
                } else {
                    // Session expired — auto stop
                    FocusStateHolder.isFocusActive = false
                }
                return  // exit here; don't run existing detection logic
            }
        }
        // ── End Focus Mode block ─────────────────────────────────────────────

        // ── Your existing logic below — completely unchanged ─────────────────
        val rootNode: AccessibilityNodeInfo? = rootInActiveWindow
        val packageName = event.packageName?.toString()

        detectionManager.processEvent(event, rootNode)

        val isOverlayConditionMet = shouldShowOverlayForApp(packageName)
        val isOnReels = detectionManager.isOnReelsScreen

        if (isOverlayConditionMet && isOnReels) {
            hideHandler.removeCallbacks(hideRunnable)
            showOverlayIfNeeded()
        } else {
            hideHandler.removeCallbacks(hideRunnable)
            hideHandler.postDelayed(hideRunnable, HIDE_DELAY_MS)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        hideHandler.removeCallbacks(hideRunnable)
        hideOverlay()
    }

    override fun onInterrupt() {
        Log.d("REELSBREAK", "Service interrupted")
        AppDetectorRouter.resetAll()
        hideOverlay()
    }

    // ── Overlay helpers (unchanged) ──────────────────────────────────────────

    private fun shouldShowOverlayForApp(packageName: String?): Boolean {
        if (packageName == null) return false
        if (!SupportedAppsRegistry.isSupported(packageName)) {
            Log.d("OVERLAY_DEBUG", "app not supported: $packageName")
            return false
        }
        val prefs = userPrefs ?: run {
            Log.d("OVERLAY_DEBUG", "userPrefs is null")
            return false
        }
        val activeMode = prefs.getActiveModeBlocking()
        val isOverlayEnabled = prefs.isOverlayEnabledBlocking()
        Log.d("OVERLAY_DEBUG",
            "shouldShowOverlayForApp pkg=$packageName activeMode=$activeMode overlayEnabled=$isOverlayEnabled")
        return isOverlayEnabled && activeMode == ActiveBlockMode.LIMIT.value
    }

    private var overlayReelsText: TextView? = null
    private var overlayTimeText: TextView? = null
    private var overlayScope: CoroutineScope? = null

    private fun showOverlayIfNeeded() {
        val wm = windowManager ?: run {
            Log.d("OVERLAY_DEBUG", "windowManager null"); return
        }
        val prefs = userPrefs ?: run {
            Log.d("OVERLAY_DEBUG", "userPrefs null"); return
        }
        if (overlayView != null) {
            Log.d("OVERLAY_DEBUG", "overlay already shown"); return
        }

        Log.d("OVERLAY_DEBUG", "creating plain view overlay")

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = GradientDrawable().apply {
                setColor(0xCC111111.toInt())
                cornerRadius = 32f
            }
            setPadding(32, 20, 32, 20)
            elevation = 10f
        }

        val reelsText = TextView(this).apply {
            text = "Reels: 0"
            setTextColor(0xFFFFFFFF.toInt())
            textSize = 13f
            setTypeface(typeface, Typeface.BOLD)
        }

        val timeText = TextView(this).apply {
            text = "Time: 0m"
            setTextColor(0xFFBBBBBB.toInt())
            textSize = 12f
        }

        layout.addView(reelsText)
        layout.addView(timeText)

        overlayReelsText = reelsText
        overlayTimeText = timeText
        overlayView = layout

        overlayScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
        overlayScope?.launch {
            prefs.reelsWatchedToday.collect { count ->
                Log.d("OVERLAY_DEBUG", "reels updated: $count")
                reelsText.text = "Reels: $count"
            }
        }
        overlayScope?.launch {
            prefs.timeSpentTodayMinutes.collect { mins ->
                Log.d("OVERLAY_DEBUG", "time updated: $mins")
                timeText.text = "Time: ${mins}m"
            }
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.END
            x = 24
            y = 200
        }

        wm.addView(layout, params)
        Log.d("OVERLAY_SERVICE", "plain overlay added successfully")
    }

    private fun hideOverlay() {
        val wm = windowManager ?: return
        val view = overlayView ?: return
        try {
            wm.removeView(view)
        } catch (e: Exception) {
            Log.e("OVERLAY_SERVICE", "removeView failed: ${e.message}", e)
        } finally {
            overlayScope?.cancel()
            overlayScope = null
            overlayView = null
            overlayReelsText = null
            overlayTimeText = null
        }
    }

    // ── Focus Mode: launch blocked screen ───────────────────────────────────
    private fun launchBlockedScreen(blockedPackage: String) {
        val intent = Intent(applicationContext, AppBlockedActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("blocked_package", blockedPackage)
            putExtra("remaining_formatted", FocusStateHolder.getRemainingFormatted())
            putExtra("focus_end_ts", FocusStateHolder.focusEndTimestamp)
        }
        applicationContext.startActivity(intent)
    }
}