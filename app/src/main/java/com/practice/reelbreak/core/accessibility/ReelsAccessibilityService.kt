package com.practice.reelbreak.core.accessibility

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.practice.reelbreak.ReelBreakApplication
import com.practice.reelbreak.core.action.ActionController
import com.practice.reelbreak.core.detection.AppDetectorRouter
import com.practice.reelbreak.core.detection.ReelsDetectionManager
import com.practice.reelbreak.core.engine.BlockingDecisionEngine
import com.practice.reelbreak.data.FocusStateHolder
import com.practice.reelbreak.domain.model.ActiveBlockMode
import com.practice.reelbreak.core.registry.ReelsDetectionRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import android.os.PowerManager
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.practice.reelbreak.core.overlay.OverlayLifecycleOwner
import kotlinx.coroutines.flow.combine
import android.graphics.PixelFormat
import com.practice.reelbreak.core.overlay.ReelBreakOverlayCard
import com.practice.reelbreak.data.preferences.UserPreferencesRepository
import com.practice.reelbreak.domain.model.LimitResetPeriod
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class ReelsAccessibilityService : AccessibilityService() {
    private lateinit var engine: BlockingDecisionEngine
    private lateinit var actionController: ActionController
    private lateinit var detectionManager: ReelsDetectionManager

//    private var prefsCacheScope: CoroutineScope? = null
    private val powerManager by lazy { getSystemService(POWER_SERVICE) as PowerManager }

    private lateinit var windowManager: WindowManager
    private var overlayView: ComposeView? = null
    private val overlayLifecycleOwner = OverlayLifecycleOwner()
    private var overlayScope: CoroutineScope? = null
    private var isOverlayReminderEnabled = false

    private var currentOverlayUi: OverlayUiModel? = null
    private var liveTimerJob: Job? = null
    private var reelsScreenEnteredAt: Long? = null
    private var persistedBaseTimeMillis: Long = 0L

    override fun onServiceConnected() {
        super.onServiceConnected()

        val info = serviceInfo ?: return

        info.packageNames = arrayOf(
            "com.google.android.youtube",
            "com.instagram.android",
            "com.instagram.lite",
            "com.snapchat.android",
            "com.facebook.katana",
            "com.facebook.lite",
            "com.zhiliaoapp.musically"
        )
        info.flags = info.flags or
                AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
                AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS

        serviceInfo = info
        Log.i("RB_CFG", "eventTypes=${serviceInfo?.eventTypes}")
        Log.i("RB_CFG", "flags=${serviceInfo?.flags}")

        val app = applicationContext as ReelBreakApplication
        val repository = app.repository
        engine = BlockingDecisionEngine(app.repository)
        actionController = ActionController(this)
        detectionManager = ReelsDetectionManager(actionController, engine)
        Log.i("RB_CFG", "Service connected. packages=${serviceInfo?.packageNames?.joinToString()}")

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        overlayLifecycleOwner.onCreate()
        overlayLifecycleOwner.onStart()
        overlayLifecycleOwner.onResume()

        startOverlayPreferenceCollection(repository)
        startOverlayUpdates(repository)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        Log.d("RB_EVT", "pkg=${event?.packageName} type=${AccessibilityEvent.eventTypeToString(event?.eventType ?: -1)}")
        if (!powerManager.isInteractive) return
        val packageName = event.packageName?.toString() ?: ""
        val isTargetApp = ReelsDetectionRegistry.isDetectionSupported(packageName)
        val isFocusBlocked = FocusStateHolder.isFocusActive && FocusStateHolder.blockedPackages.contains(packageName)

        if (!isTargetApp && !isFocusBlocked) return


        // Focus mode: block full app on window change
        if (isFocusBlocked && event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (FocusStateHolder.getRemainingMillis() > 0L) {
                actionController.triggerFullAppBlock(packageName)
            } else {
                FocusStateHolder.isFocusActive = false
            }
            return
        }

        // Reels detection: only for target apps, only on inspectable events
        if (!isTargetApp) return



            val shouldInspectTree = when (event.eventType) {
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED,
                AccessibilityEvent.TYPE_VIEW_SCROLLED -> true
                else -> false
            }
            Log.d("RB_DEBUG", "pkg=${event.packageName} type=${AccessibilityEvent.eventTypeToString(event.eventType)}")
            val rootNode: AccessibilityNodeInfo? = if (shouldInspectTree) rootInActiveWindow else null
            Log.d("RB_DEBUG", "rootNode=${rootNode != null} childCount=${rootNode?.childCount}")

//        try {
//            if (::detectionManager.isInitialized) {
//                detectionManager.processEvent(event, rootNode)
//            }
//        } finally {
//            rootNode?.recycle()
//        }


        try {
            if (::detectionManager.isInitialized) {
                detectionManager.processEvent(event, rootNode)

                if (!detectionManager.isOnReelsScreen) {
                    reelsScreenEnteredAt = null
                    liveTimerJob?.cancel()
                    liveTimerJob = null
                    hideOverlay()
                } else if (reelsScreenEnteredAt == null) {
                    reelsScreenEnteredAt = System.currentTimeMillis()
                    currentOverlayUi?.let { ui ->
                        persistedBaseTimeMillis = ui.timeSpentMillis
                    }
                    startLiveOverlayTimer()
                }
            }
        } finally {
            rootNode?.recycle()
        }
    }

    override fun onInterrupt() {
        AppDetectorRouter.resetAll()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::detectionManager.isInitialized) {
            detectionManager.cancel()
        }


        overlayScope?.cancel()
        overlayScope = null

        hideOverlay()

        overlayLifecycleOwner.onPause()
        overlayLifecycleOwner.onStop()
        overlayLifecycleOwner.onDestroy()
    }


//    private fun startOverlayUpdates(repository: UserPreferencesRepository) {
//        overlayScope?.cancel()
//        overlayScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
//
//        overlayScope?.launch {
//            combine(
//                repository.isOverlayReminderEnabled,
//                combine(
//                    repository.activeMode,
//                    repository.dailyReelLimit,
//                    repository.dailyTimeLimitMinutes
//                ) { activeMode, reelLimit, timeLimitMinutes ->
//                    Triple(activeMode, reelLimit, timeLimitMinutes)
//                },
//                combine(
//                    repository.reelsWatchedToday,
//                    repository.timeSpentTodayMillis,
//                    repository.limitResetPeriod
//                ) { reelsWatched, timeSpentMillis, resetPeriod ->
//                    Triple(reelsWatched, timeSpentMillis, resetPeriod)
//                }
//            ) { overlayEnabled, left, right ->
//                Triple(
//                    overlayEnabled,
//                    OverlayUiModel(
//                        activeMode = left.first,
//                        reelLimit = left.second,
//                        timeLimitMinutes = left.third,
//                        reelsWatched = right.first,
//                        timeSpentMillis = right.second,
//                        resetPeriod = right.third
//                    ),
//                    Unit
//                )
//            }.collect { (overlayEnabled, ui, _) ->
//                Log.d(
//                    "RB_OVERLAY",
//                    "enabled=$overlayEnabled, mode=${ui.activeMode}, reelLimit=${ui.reelLimit}, timeLimit=${ui.timeLimitMinutes}, reelsWatched=${ui.reelsWatched}, timeSpent=${ui.timeSpentMillis}, reset=${ui.resetPeriod}"
//                )
//
//                val shouldShow = overlayEnabled &&
//                        ui.activeMode == ActiveBlockMode.LIMIT &&
//                        detectionManager.isOnReelsScreen &&
//                        (ui.reelLimit > 0 || ui.timeLimitMinutes > 0)
//
//                if (!shouldShow) {
//                    Log.d("RB_OVERLAY", "Overlay hidden: shouldShow=false")
//                    hideOverlay()
//                    return@collect
//                }
//
//                Log.d("RB_OVERLAY", "Overlay should show now")
//                showOverlayIfNeeded()
//
//                overlayView?.setContent {
//                    ReelBreakOverlayCard(
//                        appLabel = "ReelBreak",
//                        reelsText = if (ui.reelLimit > 0) {
//                            "${ui.reelsWatched} / ${ui.reelLimit} reels"
//                        } else null,
//                        timeText = if (ui.timeLimitMinutes > 0) {
//                            val totalSeconds = ui.timeSpentMillis / 1000L
//                            val spentMinutes = totalSeconds / 60
//                            val spentSeconds = totalSeconds % 60
//                            String.format("%02d:%02d / %02d:00", spentMinutes, spentSeconds, ui.timeLimitMinutes)
//                        } else null,
//                        periodLabel = when (ui.resetPeriod) {
//                            LimitResetPeriod.HOUR -> "This hour"
//                            LimitResetPeriod.DAY -> "Today"
//                        }
//                    )
//                }
//            }
//        }
//    }



    private fun startOverlayUpdates(repository: UserPreferencesRepository) {
        overlayScope?.cancel()
        overlayScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

        overlayScope?.launch {
            combine(
                repository.isOverlayReminderEnabled,
                combine(
                    repository.activeMode,
                    repository.dailyReelLimit,
                    repository.dailyTimeLimitMinutes
                ) { activeMode, reelLimit, timeLimitMinutes ->
                    Triple(activeMode, reelLimit, timeLimitMinutes)
                },
                combine(
                    repository.reelsWatchedToday,
                    repository.timeSpentTodayMillis,
                    repository.limitResetPeriod
                ) { reelsWatched, timeSpentMillis, resetPeriod ->
                    Triple(reelsWatched, timeSpentMillis, resetPeriod)
                }
            ) { overlayEnabled, left, right ->
                overlayEnabled to OverlayUiModel(
                    activeMode = left.first,
                    reelLimit = left.second,
                    timeLimitMinutes = left.third,
                    reelsWatched = right.first,
                    timeSpentMillis = right.second,
                    resetPeriod = right.third
                )
            }.collect { (overlayEnabled, ui) ->
                currentOverlayUi = ui
                persistedBaseTimeMillis = ui.timeSpentMillis

                val shouldShow = overlayEnabled &&
                        ui.activeMode == ActiveBlockMode.LIMIT &&
                        detectionManager.isOnReelsScreen &&
                        (ui.reelLimit > 0 || ui.timeLimitMinutes > 0)

                if (!shouldShow) {
                    liveTimerJob?.cancel()
                    liveTimerJob = null
                    reelsScreenEnteredAt = null
                    hideOverlay()
                    return@collect
                }

                showOverlayIfNeeded()

                if (reelsScreenEnteredAt == null) {
                    reelsScreenEnteredAt = System.currentTimeMillis()
                }

                startLiveOverlayTimer()
            }
        }
    }

//    private fun showOverlayIfNeeded() {
//        if (overlayView != null) {
//            Log.d("RB_OVERLAY", "Overlay already attached")
//            return
//
//        }
//
//        val params = WindowManager.LayoutParams(
//            WindowManager.LayoutParams.WRAP_CONTENT,
//            WindowManager.LayoutParams.WRAP_CONTENT,
//            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
//            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
//                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
//                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
//            PixelFormat.TRANSLUCENT
//        ).apply {
//            gravity = Gravity.TOP or Gravity.END
//            x = 24
//            y = 120
//        }
//
//        overlayView = ComposeView(this).apply {
//            setViewTreeLifecycleOwner(overlayLifecycleOwner)
//            setViewTreeSavedStateRegistryOwner(overlayLifecycleOwner)
//        }
//
//        windowManager.addView(overlayView, params)
//    }

        private fun showOverlayIfNeeded() {
            if (overlayView != null) return

            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL  // ← center top
                x = 0
                y = 56   // distance from top of screen in pixels (adjust as needed)
            }

            overlayView = ComposeView(this).apply {
                setViewTreeLifecycleOwner(overlayLifecycleOwner)
                setViewTreeSavedStateRegistryOwner(overlayLifecycleOwner)
            }

            windowManager.addView(overlayView, params)
        }

    private fun hideOverlay() {
        overlayView?.let {
            windowManager.removeView(it)
            overlayView = null
        }
    }

    private fun startOverlayPreferenceCollection(repository: UserPreferencesRepository) {
        overlayScope?.launch {
            repository.isOverlayReminderEnabled.collect { enabled ->
                isOverlayReminderEnabled = enabled
                if (!enabled) hideOverlay()
            }
        }
    }


    private fun startLiveOverlayTimer() {
        if (liveTimerJob?.isActive == true) return

        liveTimerJob = overlayScope?.launch {
            while (isActive) {
                val ui = currentOverlayUi
                val enteredAt = reelsScreenEnteredAt

                if (ui == null || enteredAt == null || !detectionManager.isOnReelsScreen) {
                    hideOverlay()
                    break
                }

                val liveTimeSpent = persistedBaseTimeMillis + (System.currentTimeMillis() - enteredAt)

//                overlayView?.setContent {
//                    ReelBreakOverlayCard(
//                        appLabel = "ReelBreak",
//                        reelsText = if (ui.reelLimit > 0) {
//                            "${ui.reelsWatched} / ${ui.reelLimit} reels"
//                        } else null,
//                        timeText = if (ui.timeLimitMinutes > 0) {
//                            val totalSeconds = liveTimeSpent / 1000L
//                            val spentMinutes = totalSeconds / 60
//                            val spentSeconds = totalSeconds % 60
//                            String.format("%02d:%02d / %02d:00", spentMinutes, spentSeconds, ui.timeLimitMinutes)
//                        } else null,
//                        periodLabel = when (ui.resetPeriod) {
//                            LimitResetPeriod.HOUR -> "This hour"
//                            LimitResetPeriod.DAY -> "Today"
//                        }
//                    )
//                }

                overlayView?.setContent {
                    ReelBreakOverlayCard(
                        reelsWatched = ui.reelsWatched,
                        reelLimit = ui.reelLimit,
                        timeDisplay = run {
                            val totalSeconds = liveTimeSpent / 1000L
                            String.format("%02d:%02d", totalSeconds / 60, totalSeconds % 60)
                        },
                        showReels = ui.reelLimit > 0,
                        showTimer = ui.timeLimitMinutes > 0
                    )
                }

                delay(1000)
            }
        }
    }
}




private data class OverlayUiModel(
    val activeMode: ActiveBlockMode,
    val reelLimit: Int,
    val timeLimitMinutes: Int,
    val reelsWatched: Int,
    val timeSpentMillis: Long,
    val resetPeriod: LimitResetPeriod
)