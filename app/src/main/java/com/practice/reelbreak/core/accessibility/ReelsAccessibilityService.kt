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
class ReelsAccessibilityService : AccessibilityService() {

    private lateinit var detectionManager: ReelsDetectionManager
    private var userPrefs: UserPreferencesRepository? = null

    // ✅ Cached reactive values — NO runBlocking, NO blocking calls ever
    private var cachedActiveMode: Int = ActiveBlockMode.STRICT.value
    private var cachedLimitExceeded: Boolean = false
    private var prefsCacheScope: CoroutineScope? = null

    override fun onServiceConnected() {
        super.onServiceConnected()

        val app = applicationContext as ReelBreakApplication
        val repository = app.repository
        userPrefs = repository

        val engine = BlockingDecisionEngine(repository)
        val actionController = ActionController(this)
        detectionManager = ReelsDetectionManager(actionController, engine)

        // Observe prefs reactively — zero blocking
        prefsCacheScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
        prefsCacheScope?.launch {
            repository.activeMode.collect { mode ->
                cachedActiveMode = mode.value
            }
        }
        prefsCacheScope?.launch {
            repository.isLimitExceededToday.collect { exceeded ->
                cachedLimitExceeded = exceeded
            }
        }

        Log.d("REELSBREAK", "Service connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        val packageName = event.packageName?.toString()

        // CRITICAL GUARD: exit immediately for any non-supported app
        if (packageName != null && !ReelsDetectionRegistry.isDetectionSupported(packageName)) {
            AppDetectorRouter.resetAll()
            return
        }

        // Focus mode block
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val pkg = event.packageName?.toString() ?: return
            if (FocusStateHolder.isFocusActive &&
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

        AccessibilityDebugLogger.logEvent(
            event = event,
            rootNodeRequested = shouldInspectTree,
            rootNode = rootNode,
            detectorState = "isOnReels=$isOnReels cachedMode=$cachedActiveMode limitExceeded=$cachedLimitExceeded"
        )
    }

    override fun onInterrupt() {
        AppDetectorRouter.resetAll()
    }

    override fun onDestroy() {
        super.onDestroy()
        prefsCacheScope?.cancel()
        prefsCacheScope = null
    }

    private fun launchBlockedScreen(blockedPackage: String) {
        val intent = Intent(applicationContext, FocusAppBlockedActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("blockedpackage", blockedPackage)
            putExtra("remainingformatted", FocusStateHolder.getRemainingFormatted())
            putExtra("focusendts", FocusStateHolder.focusEndTimestamp)
        }
        applicationContext.startActivity(intent)
    }
}


