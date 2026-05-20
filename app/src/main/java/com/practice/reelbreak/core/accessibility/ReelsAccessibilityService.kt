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
import com.practice.reelbreak.core.debug.AccessibilityDebugLogger
import com.practice.reelbreak.data.FocusStateHolder
import com.practice.reelbreak.data.preferences.UserPreferencesRepository
import com.practice.reelbreak.domain.model.ActiveBlockMode
import com.practice.reelbreak.core.engine.FocusAppBlockedActivity
import com.practice.reelbreak.core.registry.ReelsDetectionRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch








//class ReelsAccessibilityService : AccessibilityService() {
//
//    private lateinit var detectionManager: ReelsDetectionManager
//    private var windowManager: WindowManager? = null
//    private var overlayView: View? = null
//    private var userPrefs: UserPreferencesRepository? = null
//    private var overlayScope: CoroutineScope? = null
//
//    private var overlayReelCountText: TextView? = null
//    private var overlayTimerText: TextView? = null
//
//    // Timer runs only while on reels screen
//    private val timerHandler = Handler(Looper.getMainLooper())
//    private var overlayTimerRunnable: Runnable? = null
//    private var timerElapsedSeconds = 0
//
//    // Debounce to avoid overlay flicker on brief normal events
//    private val hideHandler = Handler(Looper.getMainLooper())
//    private val HIDE_DELAY_MS = 600L
//    private val hideRunnable = Runnable { stopTimerAndHideOverlay() }
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
//        Log.d("REELSBREAK", "Service connected")
//    }
//
//    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
//        if (event == null) return
//
//        // Focus mode block
//        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
//            val pkg = event.packageName?.toString() ?: return
//            if (FocusStateHolder.isFocusActive &&
//                FocusStateHolder.blockedPackages.contains(pkg) &&
//                pkg != applicationContext.packageName
//            ) {
//                if (FocusStateHolder.getRemainingMillis() > 0L) {
//                    launchBlockedScreen(pkg)
//                } else {
//                    FocusStateHolder.isFocusActive = false
//                }
//                return
//            }
//        }
//
//        val rootNode: AccessibilityNodeInfo? = rootInActiveWindow
//        val packageName = event.packageName?.toString()
//
//        detectionManager.processEvent(event, rootNode)
//
//        val isOnReels = detectionManager.isOnReelsScreen
//        val overlayEnabled = shouldShowOverlay(packageName)
//
//        if (overlayEnabled && isOnReels) {
//            // Cancel any pending hide, ensure overlay is visible + timer running
//            hideHandler.removeCallbacks(hideRunnable)
//            showOverlayIfNeeded()
//        } else {
//            // Debounced hide — don't flicker on brief normal events
//            hideHandler.removeCallbacks(hideRunnable)
//            hideHandler.postDelayed(hideRunnable, HIDE_DELAY_MS)
//        }
//    }
//
//    override fun onInterrupt() {
//        AppDetectorRouter.resetAll()
//        stopTimerAndHideOverlay()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        hideHandler.removeCallbacks(hideRunnable)
//        stopTimerAndHideOverlay()
//    }
//
//    // ── Overlay visibility condition ───────────────────────────────────────────
//    private fun shouldShowOverlay(packageName: String?): Boolean {
//        if (packageName == null) return false
//        if (!SupportedAppsRegistry.isSupported(packageName)) return false
//        val prefs = userPrefs ?: return false
//        val isOverlayEnabled = prefs.isOverlayEnabledBlocking()
//        val activeMode = prefs.getActiveModeBlocking()
//        return isOverlayEnabled && activeMode == ActiveBlockMode.LIMIT.value
//    }
//
//    // ── Build and show overlay ─────────────────────────────────────────────────
//    private fun showOverlayIfNeeded() {
//        val wm = windowManager ?: return
//        val prefs = userPrefs ?: return
//        if (overlayView != null) return // already shown
//
//        val density = resources.displayMetrics.density
//        fun Int.dp(): Int = (this * density).toInt()
//
//        // Pill card — horizontal
//        val card = LinearLayout(this).apply {
//            orientation = LinearLayout.HORIZONTAL
//            gravity = Gravity.CENTER_VERTICAL
//            background = GradientDrawable().apply {
//                setColor(0xF01A1A2E.toInt())   // deep navy
//                cornerRadius = 999f             // pill
//                setStroke(1.dp(), 0x40FFFFFF)
//            }
//            elevation = 20f
//            setPadding(14.dp(), 8.dp(), 14.dp(), 8.dp())
//        }
//
//        // [RB] badge
//        val badge = TextView(this).apply {
//            text = "RB"
//            setTextColor(0xFFFFFFFF.toInt())
//            textSize = 8f
//            setTypeface(typeface, Typeface.BOLD)
//            background = GradientDrawable().apply {
//                setColor(0xFF6C63FF.toInt())
//                cornerRadius = 6.dp().toFloat()
//            }
//            setPadding(5.dp(), 2.dp(), 5.dp(), 2.dp())
//        }
//
//        // App name
//        val appNameText = TextView(this).apply {
//            text = "ReelBreak"
//            setTextColor(0xFFFFFFFF.toInt())
//            textSize = 10.5f
//            setTypeface(typeface, Typeface.BOLD)
//            setPadding(7.dp(), 0, 0, 0)
//        }
//
//        // Divider helper
//        fun makeDivider() = View(this).apply {
//            layoutParams = LinearLayout.LayoutParams(1.dp(), 14.dp()).apply {
//                leftMargin = 10.dp(); rightMargin = 10.dp()
//            }
//            background = GradientDrawable().apply { setColor(0x33FFFFFF) }
//        }
//
//        // Reel count
//        val reelEmoji = TextView(this).apply { text = "🎬"; textSize = 11f }
//        val reelCount = TextView(this).apply {
//            text = "0"
//            setTextColor(0xFFFFFFFF.toInt())
//            textSize = 12f
//            setTypeface(typeface, Typeface.BOLD)
//            setPadding(5.dp(), 0, 0, 0)
//        }
//        overlayReelCountText = reelCount
//
//        // Timer
//        val timerEmoji = TextView(this).apply { text = "⏱"; textSize = 11f }
//        val timerText = TextView(this).apply {
//            text = "0:00"
//            setTextColor(0xFFAAAAAA.toInt())
//            textSize = 12f
//            setPadding(5.dp(), 0, 0, 0)
//        }
//        overlayTimerText = timerText
//
//        card.apply {
//            addView(badge)
//            addView(appNameText)
//            addView(makeDivider())
//            addView(reelEmoji)
//            addView(reelCount)
//            addView(makeDivider())
//            addView(timerEmoji)
//            addView(timerText)
//        }
//
//        overlayView = card
//
//        // Collect reels count + limit live
//        overlayScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
//        overlayScope?.launch {
//            combine(prefs.reelsWatchedToday, prefs.dailyReelLimit) { c, l -> c to l }
//                .collect { (count, limit) ->
//                    overlayReelCountText?.text = if (limit > 0) "$count / $limit" else "$count"
//                }
//        }
//
//        // Start live timer from 0
//        startTimer()
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
//            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
//            x = 0
//            y = 44.dp()
//        }
//
//        wm.addView(card, params)
//        Log.d("OVERLAYDEBUG", "Overlay shown at top-center")
//    }
//
//    // ── Timer: only ticks while overlay is on screen ───────────────────────────
//    private fun startTimer() {
//        stopTimer()
//        timerElapsedSeconds = 0
//        val runnable = object : Runnable {
//            override fun run() {
//                timerElapsedSeconds++
//                val m = timerElapsedSeconds / 60
//                val s = timerElapsedSeconds % 60
//                overlayTimerText?.text = String.format("%d:%02d", m, s)
//                timerHandler.postDelayed(this, 1000L)
//            }
//        }
//        overlayTimerRunnable = runnable
//        timerHandler.postDelayed(runnable, 1000L)
//    }
//
//    private fun stopTimer() {
//        overlayTimerRunnable?.let { timerHandler.removeCallbacks(it) }
//        overlayTimerRunnable = null
//    }
//
//    private fun stopTimerAndHideOverlay() {
//        stopTimer()
//        hideOverlay()
//    }
//
//    private fun hideOverlay() {
//        val wm = windowManager ?: return
//        val view = overlayView ?: return
//        try {
//            wm.removeView(view)
//        } catch (e: Exception) {
//            Log.e("OVERLAYSERVICE", "removeView failed: ${e.message}", e)
//        } finally {
//            overlayScope?.cancel()
//            overlayScope = null
//            overlayView = null
//            overlayReelCountText = null
//            overlayTimerText = null
//        }
//    }
//
//    private fun launchBlockedScreen(blockedPackage: String) {
//        val intent = Intent(applicationContext, AppBlockedActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
//                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
//                    Intent.FLAG_ACTIVITY_SINGLE_TOP
//            putExtra("blocked_package", blockedPackage)
//            putExtra("remaining_formatted", FocusStateHolder.getRemainingFormatted())
//            putExtra("focus_end_ts", FocusStateHolder.focusEndTimestamp)
//        }
//        applicationContext.startActivity(intent)
//    }
//}


import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ReelsAccessibilityService : AccessibilityService() {

    private lateinit var detectionManager: ReelsDetectionManager
    private var windowManager: WindowManager? = null
    private var userPrefs: UserPreferencesRepository? = null

    private var overlayView: View? = null
    private var overlayScope: CoroutineScope? = null
    private var overlayReelCountText: TextView? = null
    private var overlayTimerText: TextView? = null

    private val timerHandler = Handler(Looper.getMainLooper())
    private var overlayTimerRunnable: Runnable? = null
    private var timerElapsedSeconds = 0

    private val hideHandler = Handler(Looper.getMainLooper())
    private val HIDE_DELAY_MS = 600L
    private val hideRunnable = Runnable { stopTimerAndHideOverlay() }

    override fun onServiceConnected() {
        super.onServiceConnected()
        val app = applicationContext as ReelBreakApplication
        val repository = app.repository
        userPrefs = repository

        val engine = BlockingDecisionEngine(repository)
        val actionController = ActionController(this)
        detectionManager = ReelsDetectionManager(actionController, engine)

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        Log.d("REELSBREAK", "Service connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        val packageName = event.packageName?.toString()

        // NEW CRITICAL GUARD: If the user is NOT in a supported app,
        // kill the overlay INSTANTLY. No delay.
        if (packageName != null && !ReelsDetectionRegistry.isDetectionSupported(packageName)) {
            hideHandler.removeCallbacks(hideRunnable)
            stopTimerAndHideOverlay() // Remove the overlay immediately
            AppDetectorRouter.resetAll()
            return

        }

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
                    FocusStateHolder.isFocusActive = false
                }
                return
            }
        }

//        val rootNode: AccessibilityNodeInfo? = rootInActiveWindow
//
//        detectionManager.processEvent(event, rootNode)
//
//        val isOnReels = detectionManager.isOnReelsScreen
//        val overlayEnabled = shouldShowOverlay(packageName)

        val shouldInspectTree = when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED,
            AccessibilityEvent.TYPE_VIEW_SCROLLED -> true
            else -> false
        }

        val rootNode: AccessibilityNodeInfo? =
            if (shouldInspectTree) rootInActiveWindow else null

        detectionManager.processEvent(event, rootNode)

        val isOnReels = detectionManager.isOnReelsScreen
        val overlayEnabled = shouldShowOverlay(packageName)

        AccessibilityDebugLogger.logEvent(
            event = event,
            rootNodeRequested = shouldInspectTree,
            rootNode = rootNode,
            detectorState = "isOnReels=$isOnReels overlayEnabled=$overlayEnabled"
        )

        if (overlayEnabled && isOnReels) {
            hideHandler.removeCallbacks(hideRunnable)
            showOverlayIfNeeded()
        } else {
            hideHandler.removeCallbacks(hideRunnable)
            hideHandler.postDelayed(hideRunnable, HIDE_DELAY_MS)
        }
    }

    override fun onInterrupt() {
        AppDetectorRouter.resetAll()
        stopTimerAndHideOverlay()
    }

    override fun onDestroy() {
        super.onDestroy()
        hideHandler.removeCallbacks(hideRunnable)
        stopTimerAndHideOverlay()
    }

    private fun shouldShowOverlay(packageName: String?): Boolean {
        if (packageName == null) return false
        if (!ReelsDetectionRegistry.isDetectionSupported(packageName)) return false
        val prefs = userPrefs ?: return false
      //  if (!prefs.isOverlayEnabledBlocking()) return false
        if (prefs.getActiveModeBlocking() != ActiveBlockMode.LIMIT.value) return false
        val isLimitHit = runBlocking { prefs.isLimitExceededToday.first() }
        if (isLimitHit) return false
        return true
    }

    private fun showOverlayIfNeeded() {
        val wm = windowManager ?: return
        val prefs = userPrefs ?: return
        if (overlayView != null) return

        val density = resources.displayMetrics.density
        fun Int.dp(): Int = (this * density).toInt()

        val card = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            background = GradientDrawable().apply {
                colors = intArrayOf(0xFF8A2BE2.toInt(), 0xFF5B21B6.toInt())
                gradientType = GradientDrawable.LINEAR_GRADIENT
                orientation = GradientDrawable.Orientation.LEFT_RIGHT
                cornerRadius = 999f
                setStroke(1.dp(), 0x40FFFFFF)
            }
            elevation = 24f
            setPadding(12.dp(), 8.dp(), 12.dp(), 8.dp())
        }

        val badge = TextView(this).apply {
            text = "RB"
            setTextColor(0xFFFFFFFF.toInt())
            textSize = 8f
            setTypeface(typeface, Typeface.BOLD)
            background = GradientDrawable().apply {
                setColor(0xFF1F1147.toInt())
                cornerRadius = 6.dp().toFloat()
            }
            setPadding(5.dp(), 2.dp(), 5.dp(), 2.dp())
        }

        val appNameText = TextView(this).apply {
            text = "ReelBreak"
            setTextColor(0xFFFFFFFF.toInt())
            textSize = 10.5f
            setTypeface(typeface, Typeface.BOLD)
            setPadding(7.dp(), 0, 0, 0)
        }

        fun divider() = View(this).apply {
            layoutParams = LinearLayout.LayoutParams(1.dp(), 14.dp()).apply {
                leftMargin = 9.dp()
                rightMargin = 9.dp()
            }
            background = GradientDrawable().apply { setColor(0x33FFFFFF) }
        }

        val reelIcon = TextView(this).apply { text = "🎬"; textSize = 11f }
        val reelCount = TextView(this).apply {
            text = "0"
            setTextColor(0xFFFFFFFF.toInt())
            textSize = 12f
            setTypeface(typeface, Typeface.BOLD)
            setPadding(4.dp(), 0, 0, 0)
        }
        overlayReelCountText = reelCount

        val timerIcon = TextView(this).apply { text = "⏱"; textSize = 11f }
        val timerText = TextView(this).apply {
            text = "0:00"
            setTextColor(0xFFEDE9FE.toInt())
            textSize = 12f
            setPadding(4.dp(), 0, 0, 0)
        }
        overlayTimerText = timerText

        card.addView(badge)
        card.addView(appNameText)
        card.addView(divider())
        card.addView(reelIcon)
        card.addView(reelCount)
        card.addView(divider())
        card.addView(timerIcon)
        card.addView(timerText)

        overlayView = card

        overlayScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
        overlayScope?.launch {
            combine(prefs.reelsWatchedToday, prefs.dailyReelLimit) { c, l -> c to l }
                .collect { (count, limit) ->
                    overlayReelCountText?.text = if (limit > 0) "$count / $limit" else "$count"
                }
        }

        timerElapsedSeconds = 0
        startTimer()

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
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            x = 0
            y = 44.dp()
        }

        wm.addView(card, params)
    }

    private fun startTimer() {
        stopTimer()
        val runnable = object : Runnable {
            override fun run() {
                timerElapsedSeconds++
                val m = timerElapsedSeconds / 60
                val s = timerElapsedSeconds % 60
                overlayTimerText?.text = String.format("%d:%02d", m, s)
                timerHandler.postDelayed(this, 1000L)
            }
        }
        overlayTimerRunnable = runnable
        timerHandler.postDelayed(runnable, 1000L)
    }

    private fun stopTimer() {
        overlayTimerRunnable?.let { timerHandler.removeCallbacks(it) }
        overlayTimerRunnable = null
    }

    private fun stopTimerAndHideOverlay() {
        stopTimer()
        hideOverlay()
    }

    private fun hideOverlay() {
        val wm = windowManager ?: return
        val view = overlayView ?: return
        try {
            wm.removeView(view)
        } catch (e: Exception) {
            Log.e("OVERLAYSERVICE", "removeView failed: ${e.message}", e)
        } finally {
            overlayScope?.cancel()
            overlayScope = null
            overlayView = null
            overlayReelCountText = null
            overlayTimerText = null
        }
    }

    private fun launchBlockedScreen(blockedPackage: String) {
        val intent = Intent(applicationContext, FocusAppBlockedActivity::class.java).apply {
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




