package com.sangeeta.reelsbreak.core.accessibility

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.sangeeta.reelsbreak.core.action.ActionController
import com.sangeeta.reelsbreak.core.detection.ReelsDetectionManager
import com.sangeeta.reelsbreak.core.engine.BlockingDecisionEngine
import com.sangeeta.reelsbreak.data.FocusStateHolder
import com.sangeeta.reelsbreak.core.registry.ReelsDetectionRegistry
import android.os.PowerManager
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.sangeeta.reelsbreak.core.overlay.OverlayLifecycleOwner
import kotlinx.coroutines.flow.combine
import android.graphics.PixelFormat
import com.sangeeta.reelsbreak.core.overlay.ReelBreakOverlayCard
import com.sangeeta.reelsbreak.data.preferences.UserPreferencesRepository
import com.sangeeta.reelsbreak.domain.model.LimitResetPeriod
import com.sangeeta.reelsbreak.domain.model.ProtectionMode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

//@AndroidEntryPoint
//class ReelsAccessibilityService : AccessibilityService() {
//    @Inject
//    lateinit var repository: UserPreferencesRepository
//
//    private lateinit var engine: BlockingDecisionEngine
//    private lateinit var actionController: ActionController
//    private lateinit var detectionManager: ReelsDetectionManager
//
//    private val powerManager by lazy { getSystemService(POWER_SERVICE) as PowerManager }
//    private lateinit var windowManager: WindowManager
//
//    private var overlayView: ComposeView? = null
//    private val overlayLifecycleOwner = OverlayLifecycleOwner()
//    private var overlayScope: CoroutineScope? = null
//    private var isOverlayReminderEnabled = false
//
//    private var currentProtectionMode: ProtectionMode = ProtectionMode.DEFAULT
//    private var currentOverlayUi: OverlayUiModel? = null
//    private var liveTimerJob: Job? = null
//    private var reelsScreenEnteredAt: Long? = null
//    private var persistedBaseTimeMillis: Long = 0L
//
//    override fun onServiceConnected() {
//        super.onServiceConnected()
//        val info = serviceInfo ?: return
//        info.packageNames = arrayOf(
//            "com.google.android.youtube",
//            "com.instagram.android",
//            "com.instagram.lite",
//            "com.snapchat.android",
//            "com.facebook.katana",
//            "com.facebook.lite"
//        )
//        info.flags = info.flags or
//                AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
//                AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
//
//        serviceInfo = info
//
//        engine = BlockingDecisionEngine(repository)
//        actionController = ActionController(this)
//        detectionManager = ReelsDetectionManager(actionController, engine)
//
//        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
//
//        overlayLifecycleOwner.onCreate()
//        overlayLifecycleOwner.onStart()
//        overlayLifecycleOwner.onResume()
//
//        startOverlayPreferenceCollection(repository)
//        startOverlayUpdates(repository)
//    }
//
//    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
//        if (event == null) return
//        if (!powerManager.isInteractive) return
//
//        val packageName = event.packageName?.toString() ?: ""
//        val isTargetApp = ReelsDetectionRegistry.isDetectionSupported(packageName)
//        val isFocusBlocked = FocusStateHolder.isFocusActive && FocusStateHolder.blockedPackages.contains(packageName)
//
//        if (!isTargetApp && !isFocusBlocked) return
//
//        if (isFocusBlocked && event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
//            if (FocusStateHolder.getRemainingMillis() > 0L) {
//                actionController.triggerFullAppBlock(packageName)
//            } else {
//                FocusStateHolder.isFocusActive = false
//            }
//            return
//        }
//
//        if (!isTargetApp) return
//
//        val shouldInspectTree = when (event.eventType) {
//            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
//            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED,
//            AccessibilityEvent.TYPE_VIEW_SCROLLED -> true
//            else -> false
//        }
//
//        val rootNode: AccessibilityNodeInfo? = if (shouldInspectTree) rootInActiveWindow else null
//
//        try {
//            if (::detectionManager.isInitialized) {
//                detectionManager.processEvent(event, rootNode)
//
//                if (!detectionManager.isOnReelsScreen) {
//                    reelsScreenEnteredAt = null
//                    liveTimerJob?.cancel()
//                    liveTimerJob = null
//                    hideOverlay()
//                } else if (reelsScreenEnteredAt == null) {
//                    reelsScreenEnteredAt = System.currentTimeMillis()
//                    currentOverlayUi?.let { ui ->
//                        persistedBaseTimeMillis = ui.timeSpentMillis
//                    }
//                    startLiveOverlayTimer()
//                }
//            }
//        } finally {
//            rootNode?.recycle()
//        }
//    }
//
//    override fun onInterrupt() {}
//
//    override fun onDestroy() {
//        super.onDestroy()
//        overlayScope?.cancel()
//        overlayScope = null
//
//        hideOverlay()
//
//        overlayLifecycleOwner.onPause()
//        overlayLifecycleOwner.onStop()
//        overlayLifecycleOwner.onDestroy()
//    }
//
//    private fun startOverlayUpdates(repository: UserPreferencesRepository) {
//        overlayScope?.cancel()
//        overlayScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
//
//        overlayScope?.launch {
//            combine(
//                repository.isOverlayReminderEnabled,
//                combine(
//                    repository.protectionMode,
//                    repository.dailyReelLimit,
//                    repository.dailyTimeLimitMinutes
//                ) { protectionMode, reelLimit, timeLimitMinutes ->
//                    Triple(protectionMode, reelLimit, timeLimitMinutes)
//                },
//                combine(
//                    repository.reelsWatchedToday,
//                    repository.timeSpentTodayMillis,
//                    repository.limitResetPeriod
//                ) { reelsWatched, timeSpentMillis, resetPeriod ->
//                    Triple(reelsWatched, timeSpentMillis, resetPeriod)
//                }
//            ) { overlayEnabled, left, right ->
//                overlayEnabled to OverlayUiModel(
//                    protectionMode = left.first,
//                    reelLimit = left.second,
//                    timeLimitMinutes = left.third,
//                    reelsWatched = right.first,
//                    timeSpentMillis = right.second,
//                    resetPeriod = right.third
//                )
//            }.collect { (overlayEnabled, ui) ->
//                currentOverlayUi = ui
//                persistedBaseTimeMillis = ui.timeSpentMillis
//
//                val shouldShow = detectionManager.isOnReelsScreen && when (ui.protectionMode) {
//                    ProtectionMode.PAUSED -> overlayEnabled
//                    ProtectionMode.MINDFUL -> true
//                    ProtectionMode.DEFAULT -> false
//                }
//
//                if (!shouldShow) {
//                    liveTimerJob?.cancel()
//                    liveTimerJob = null
//                    reelsScreenEnteredAt = null
//                    hideOverlay()
//                    return@collect
//                }
//
//                showOverlayIfNeeded()
//                if (reelsScreenEnteredAt == null) {
//                    reelsScreenEnteredAt = System.currentTimeMillis()
//                }
//                startLiveOverlayTimer()
//            }
//        }
//    }
//
//    private fun showOverlayIfNeeded() {
//        if (overlayView != null) return
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
//            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
//            x = 0
//            y = 56
//        }
//
//        overlayView = ComposeView(this).apply {
//            setViewTreeLifecycleOwner(overlayLifecycleOwner)
//            setViewTreeSavedStateRegistryOwner(overlayLifecycleOwner)
//        }
//
//        try {
//            windowManager.addView(overlayView, params)
//        } catch (e: Exception) {
//            Log.e("RB_ACC_SERVICE", "Failed to add overlay view safely", e)
//            overlayView = null
//        }
//    }
//
//    private fun hideOverlay() {
//        overlayView?.let { view ->
//            try {
//                windowManager.removeViewImmediate(view)
//            } catch (e: IllegalArgumentException) {
//                Log.w("RB_ACC_SERVICE", "Overlay View was already unattached from window layout.")
//            } finally {
//                overlayView = null
//            }
//        }
//    }
//
//    private fun startOverlayPreferenceCollection(repository: UserPreferencesRepository) {
//        overlayScope?.launch {
//            repository.isOverlayReminderEnabled.collect { enabled ->
//                isOverlayReminderEnabled = enabled
//                if (!enabled) hideOverlay()
//            }
//        }
//        overlayScope?.launch {
//            repository.protectionMode.collect { mode ->
//                currentProtectionMode = mode
//                if (mode == ProtectionMode.DEFAULT) hideOverlay()
//            }
//        }
//    }
//
//    private fun startLiveOverlayTimer() {
//        if (liveTimerJob?.isActive == true) return
//
//        liveTimerJob = overlayScope?.launch {
//            while (isActive) {
//                val ui = currentOverlayUi
//                val enteredAt = reelsScreenEnteredAt
//
//                if (ui == null || enteredAt == null || !detectionManager.isOnReelsScreen) {
//                    hideOverlay()
//                    break
//                }
//
//                val liveTimeSpent = persistedBaseTimeMillis + (System.currentTimeMillis() - enteredAt)
//
//                val showReels = when (ui.protectionMode) {
//                    ProtectionMode.PAUSED -> true
//                    ProtectionMode.MINDFUL -> ui.reelLimit > 0
//                    ProtectionMode.DEFAULT -> false
//                }
//
//                val showTimer = when (ui.protectionMode) {
//                    ProtectionMode.PAUSED -> true
//                    ProtectionMode.MINDFUL -> ui.timeLimitMinutes > 0
//                    ProtectionMode.DEFAULT -> false
//                }
//
//                overlayView?.setContent {
//                    ReelBreakOverlayCard(
//                        reelsWatched = ui.reelsWatched,
//                        reelLimit = ui.reelLimit,
//                        timeDisplay = run {
//                            val totalSeconds = liveTimeSpent / 1000L
//                            String.format("%02d:%02d", totalSeconds / 60, totalSeconds % 60)
//                        },
//                        showReels = showReels,
//                        showTimer = showTimer
//                    )
//                }
//                delay(1000)
//            }
//        }
//    }
//}
//
//private data class OverlayUiModel(
//    val protectionMode: ProtectionMode,
//    val reelLimit: Int,
//    val timeLimitMinutes: Int,
//    val reelsWatched: Int,
//    val timeSpentMillis: Long,
//    val resetPeriod: LimitResetPeriod
//)

@AndroidEntryPoint
class ReelsAccessibilityService : AccessibilityService() {

    // Clean native injection handling via Hilt field tokens
    @Inject
    lateinit var repository: UserPreferencesRepository

    private lateinit var engine: BlockingDecisionEngine
    private lateinit var actionController: ActionController
    private lateinit var detectionManager: ReelsDetectionManager

    private val powerManager by lazy { getSystemService(POWER_SERVICE) as PowerManager }
    private lateinit var windowManager: WindowManager

    private var overlayView: ComposeView? = null
    private val overlayLifecycleOwner = OverlayLifecycleOwner()
    private var overlayScope: CoroutineScope? = null
    private var isOverlayReminderEnabled = false

    private var currentProtectionMode: ProtectionMode = ProtectionMode.DEFAULT
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
            "com.facebook.lite"
        )
        info.flags = info.flags or
                AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
                AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS

        serviceInfo = info

        // Set up execution blocks safely using our injected repository field reference
        engine = BlockingDecisionEngine(repository)
        actionController = ActionController(this)
        detectionManager = ReelsDetectionManager(actionController, engine)

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        overlayLifecycleOwner.onCreate()
        overlayLifecycleOwner.onStart()
        overlayLifecycleOwner.onResume()

        startOverlayPreferenceCollection(repository)
        startOverlayUpdates(repository)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        if (!powerManager.isInteractive) return

        val packageName = event.packageName?.toString() ?: ""
        val isTargetApp = ReelsDetectionRegistry.isDetectionSupported(packageName)
        val isFocusBlocked = FocusStateHolder.isFocusActive && FocusStateHolder.blockedPackages.contains(packageName)

        if (!isTargetApp && !isFocusBlocked) return

        if (isFocusBlocked && event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (FocusStateHolder.getRemainingMillis() > 0L) {
                actionController.triggerFullAppBlock(packageName)
            } else {
                FocusStateHolder.isFocusActive = false
            }
            return
        }

        if (!isTargetApp) return

        val shouldInspectTree = when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED,
            AccessibilityEvent.TYPE_VIEW_SCROLLED -> true
            else -> false
        }

        val rootNode: AccessibilityNodeInfo? = if (shouldInspectTree) rootInActiveWindow else null

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
        // Handled cleanly by system suspension hooks
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

    private fun startOverlayUpdates(repository: UserPreferencesRepository) {
        overlayScope?.cancel()
        overlayScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

        overlayScope?.launch {
            combine(
                repository.isOverlayReminderEnabled,
                combine(
                    repository.protectionMode,
                    repository.dailyReelLimit,
                    repository.dailyTimeLimitMinutes
                ) { protectionMode, reelLimit, timeLimitMinutes ->
                    Triple(protectionMode, reelLimit, timeLimitMinutes)
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
                    protectionMode = left.first,
                    reelLimit = left.second,
                    timeLimitMinutes = left.third,
                    reelsWatched = right.first,
                    timeSpentMillis = right.second,
                    resetPeriod = right.third
                )
            }.collect { (overlayEnabled, ui) ->
                currentOverlayUi = ui
                persistedBaseTimeMillis = ui.timeSpentMillis

                val shouldShow = detectionManager.isOnReelsScreen && when (ui.protectionMode) {
                    ProtectionMode.PAUSED -> overlayEnabled
                    ProtectionMode.MINDFUL -> true
                    ProtectionMode.DEFAULT -> false
                }

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
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            x = 0
            y = 56
        }

        overlayView = ComposeView(this).apply {
            setViewTreeLifecycleOwner(overlayLifecycleOwner)
            setViewTreeSavedStateRegistryOwner(overlayLifecycleOwner)
        }

        try {
            windowManager.addView(overlayView, params)
        } catch (e: Exception) {
            Log.e("RB_ACC_SERVICE", "Failed to add overlay view safely", e)
            overlayView = null
        }
    }

    private fun hideOverlay() {
        overlayView?.let { view ->
            try {
                windowManager.removeViewImmediate(view)
            } catch (e: IllegalArgumentException) {
                Log.w("RB_ACC_SERVICE", "Overlay View was already unattached from window layout.")
            } finally {
                overlayView = null
            }
        }
    }

    private fun startOverlayPreferenceCollection(repository: UserPreferencesRepository) {
        overlayScope?.launch {
            repository.isOverlayReminderEnabled.collect { enabled ->
                isOverlayReminderEnabled = enabled
                if (!enabled) hideOverlay()
            }
        }
        overlayScope?.launch {
            repository.protectionMode.collect { mode ->
                currentProtectionMode = mode
                if (mode == ProtectionMode.DEFAULT) hideOverlay()
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

                val showReels = when (ui.protectionMode) {
                    ProtectionMode.PAUSED -> true
                    ProtectionMode.MINDFUL -> ui.reelLimit > 0
                    ProtectionMode.DEFAULT -> false
                }

                val showTimer = when (ui.protectionMode) {
                    ProtectionMode.PAUSED -> true
                    ProtectionMode.MINDFUL -> ui.timeLimitMinutes > 0
                    ProtectionMode.DEFAULT -> false
                }

                overlayView?.setContent {
                    ReelBreakOverlayCard(
                        reelsWatched = ui.reelsWatched,
                        reelLimit = ui.reelLimit,
                        timeDisplay = run {
                            val totalSeconds = liveTimeSpent / 1000L
                            String.format("%02d:%02d", totalSeconds / 60, totalSeconds % 60)
                        },
                        showReels = showReels,
                        showTimer = showTimer
                    )
                }
                delay(1000)
            }
        }
    }
}

private data class OverlayUiModel(
    val protectionMode: ProtectionMode,
    val reelLimit: Int,
    val timeLimitMinutes: Int,
    val reelsWatched: Int,
    val timeSpentMillis: Long,
    val resetPeriod: LimitResetPeriod
)