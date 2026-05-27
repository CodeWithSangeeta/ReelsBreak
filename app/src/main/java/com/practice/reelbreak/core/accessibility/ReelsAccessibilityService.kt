package com.practice.reelbreak.core.accessibility

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.practice.reelbreak.ReelBreakApplication
import com.practice.reelbreak.core.action.ActionController
import com.practice.reelbreak.core.detection.AppDetectorRouter
import com.practice.reelbreak.core.detection.ReelsDetectionManager
import com.practice.reelbreak.core.engine.BlockingDecisionEngine
import com.practice.reelbreak.core.debug.AccessibilityDebugLogger
import com.practice.reelbreak.data.FocusStateHolder
import com.practice.reelbreak.data.preferences.UserPreferencesRepository
import com.practice.reelbreak.domain.model.ActiveBlockMode
import com.practice.reelbreak.core.registry.ReelsDetectionRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import android.os.PowerManager
//import com.practice.reelbreak.core.detection.BlockableSurface
//import com.practice.reelbreak.core.detection.matchesDetection
import kotlinx.coroutines.launch


//
//class ReelsAccessibilityService : AccessibilityService() {
//
//    private lateinit var detectionManager: ReelsDetectionManager
//    private var userPrefs: UserPreferencesRepository? = null
//
//    // Cached reactive values for zero blocking
//    private var cachedActiveMode: Int = ActiveBlockMode.STRICT.value
//    private var cachedLimitExceeded: Boolean = false
//    private var prefsCacheScope: CoroutineScope? = null
//
//    private val powerManager by lazy { getSystemService(POWER_SERVICE) as PowerManager }
//
//    override fun onServiceConnected() {
//        super.onServiceConnected()
//
//        // 1. Configure initial safe package scope listening array
//        configureServiceScope(listenToAll = false)
//
//        // 2. Properly initialize repositories and engine (Moved back from your commented code)
//        val app = applicationContext as ReelBreakApplication
//        val repository = app.repository
//        userPrefs = repository
//
//        val engine = BlockingDecisionEngine(repository)
//        val actionController = ActionController(this)
//        detectionManager = ReelsDetectionManager(actionController, engine)
//
//        // 3. Collect active user settings asynchronously
//        prefsCacheScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
//        prefsCacheScope?.launch {
//            repository.activeMode.collect { mode ->
//                cachedActiveMode = mode.value
//            }
//        }
//        prefsCacheScope?.launch {
//            repository.isLimitExceededToday.collect { exceeded ->
//                cachedLimitExceeded = exceeded
//            }
//        }
//
//        Log.d("REELSBREAK", "Service connected and secure tracking verified")
//    }
//
//    private fun configureServiceScope(listenToAll: Boolean) {
//        val info = serviceInfo ?: return
//        if (listenToAll) {
//            // Expand ONLY when a target app is running to catch Home exit actions safely
//            info.packageNames = null
//        } else {
//            // Restrict base engine strictly to non-sensitive targeted apps
//            info.packageNames = arrayOf(
//                "com.google.android.youtube",
//                "com.instagram.android",
//                "com.snapchat.android",
//                "com.zhiliaoapp.musically"
//            )
//        }
//        serviceInfo = info
//    }
//
//    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
//        if (event == null) return
//
//        // 🛑 STEP 0: BATTERY & INFRASTRUCTURE GUARD
//        // If the user's screen is off, exit immediately to stop scanning
//        if (!powerManager.isInteractive) return
//
//        val packageName = event.packageName?.toString() ?: return
//
//        // 🛑 STEP 1: ULTRA-STRICT SECURITY GUARD
//        // If the signature is outside our verified operational bounds (e.g. Banking App),
//        // drop the execution line immediately. No layout tree parsing can touch it.
//        val isDistractorApp = ReelsDetectionRegistry.isDetectionSupported(packageName)
//        val isCustomBlockedApp = FocusStateHolder.isFocusActive && FocusStateHolder.blockedPackages.contains(packageName)
//        val isOurOwnApp = packageName == applicationContext.packageName
//
//        if (!isDistractorApp && !isCustomBlockedApp && !isOurOwnApp) {
//            return
//        }
//
//        // ── STEP 2: DYNAMIC LISTENING SCOPE MANAGEMENT ──
//        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
//            configureServiceScope(listenToAll = isDistractorApp)
//        }
//
//        // ── STEP 3: FOCUS MODE WINDOW INTRUSION BLOCK ──
//        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
//            if (FocusStateHolder.isFocusActive && isCustomBlockedApp) {
//                // ✅ CHANGE THIS: Instead of launchBlockedScreen, bounce the user back instantly!
//                performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
//                Log.d("REELSBREAK", "Focus Mode Active: Bounced user back natively from $packageName")
//                } else {
//                    FocusStateHolder.isFocusActive = false
//                }
//                return
//            }
//
//
//        // ── STEP 4: TREE INSPECTIONS AND ATOMIC CLEAN RECYCLING ──
//        val shouldInspectTree = when (event.eventType) {
//            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
//            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED,
//            AccessibilityEvent.TYPE_VIEW_SCROLLED -> true
//            else -> false
//        }
//
//        val rootNode: AccessibilityNodeInfo? = if (shouldInspectTree) rootInActiveWindow else null
//
//        // Safe check to verify engine initialization isn't skipped
//        if (::detectionManager.isInitialized) {
//            detectionManager.processEvent(event, rootNode)
//        }
//
//        // Always release the window memory hook right after execution
//        rootNode?.recycle()
//    }
//
//    override fun onInterrupt() {
//        AppDetectorRouter.resetAll()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        prefsCacheScope?.cancel()
//        prefsCacheScope = null
//    }
//
//}

//class ReelsAccessibilityService : AccessibilityService() {
//
//    private lateinit var engine: BlockingDecisionEngine
//    private lateinit var actionController: ActionController
//    private var prefsCacheScope: CoroutineScope? = null
//    private val powerManager by lazy { getSystemService(POWER_SERVICE) as PowerManager }
//
//    override fun onServiceConnected() {
//        super.onServiceConnected()
//
//        // 🔒 Enforce rigid target isolation right at boot time
//        val info = serviceInfo ?: return
//        info.packageNames = arrayOf(
//            "com.google.android.youtube",
//            "com.instagram.android",
//            "com.snapchat.android",
//            "com.zhiliaoapp.musically"
//        )
//        serviceInfo = info
//
//        val app = applicationContext as ReelBreakApplication
//        engine = BlockingDecisionEngine(app.repository)
//        actionController = ActionController(this)
//    }
//
//    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
//        if (event == null || !powerManager.isInteractive) return
//
//        val packageName = event.packageName?.toString() ?: return
//
//        // 🛑 STEP 1: ULTRAPASSIVE SECURITY SHIELD
//        // Check if this app is on our trusted operational target list.
//        val isTargetApp = ReelsDetectionRegistry.isDetectionSupported(packageName)
//        val isFocusBlocked = FocusStateHolder.isFocusActive && FocusStateHolder.blockedPackages.contains(packageName)
//
//        // If it is an unverified package (like a Bank app), DROP the execution line instantly.
//        // Do not log it, do not evaluate event types, do not pull windows, do not touch updateServiceScope.
//        if (!isTargetApp && !isFocusBlocked) {
//            return
//        }
//
//        // ── STEP 2: DYNAMIC LISTENING SCOPE MANAGEMENT ──
//        // Only alter parameters if we have passed the initial security shield above
//        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
//            updateServiceScope(listenToAll = isTargetApp)
//        }
//
//        // ── STEP 3: FOCUS MODE WINDOW INTRUSION BLOCK ──
//        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
//            if (FocusStateHolder.isFocusActive && isFocusBlocked) {
//                if (FocusStateHolder.getRemainingMillis() > 0L) {
//                    performGlobalAction(GLOBAL_ACTION_BACK)
//                } else {
//                    FocusStateHolder.isFocusActive = false
//                }
//                return
//            }
//        }
//
//        // ── STEP 4: ROOM DB LIMIT VALUATION & LOGIC PROCESSING ──
//        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && isTargetApp) {
//            prefsCacheScope?.cancel()
//            prefsCacheScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
//            prefsCacheScope?.launch {
//                if (engine.decide() == BlockingDecisionEngine.Decision.BLOCK) {
//                    actionController.triggerBlock(packageName)
//                } else {
//                    engine.onReelAllowed()
//                }
//            }
//        }
//    }
//
//    override fun onInterrupt() {
//        AppDetectorRouter.resetAll()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        prefsCacheScope?.cancel()
//        prefsCacheScope = null
//    }
//
//
//    private fun updateServiceScope(listenToAll: Boolean) {
//        val info = serviceInfo ?: return
//        if (listenToAll) {
//            // Expand scope ONLY when we are actively tracking a distracted session
//            info.packageNames = null
//            Log.d("REELSBREAK", "Scope expanded globally to track app exit boundaries safely.")
//        } else {
//            // Restrict base engine strictly to targeted apps to hide from banking apps
//            info.packageNames = arrayOf(
//                "com.google.android.youtube",
//                "com.instagram.android",
//                "com.snapchat.android",
//                "com.zhiliaoapp.musically"
//            )
//            Log.d("REELSBREAK", "Scope restricted strictly to distraction targets.")
//        }
//        serviceInfo = info
//    }
//}



class ReelsAccessibilityService : AccessibilityService() {
    private lateinit var engine: BlockingDecisionEngine
    private lateinit var actionController: ActionController
    private lateinit var detectionManager: ReelsDetectionManager

//    private var prefsCacheScope: CoroutineScope? = null
    private val powerManager by lazy { getSystemService(POWER_SERVICE) as PowerManager }


    override fun onServiceConnected() {
        super.onServiceConnected()

        val info = serviceInfo ?: return

        // 🔒 Set your target isolation array
        info.packageNames = arrayOf(
            "com.google.android.youtube",
            "com.google.android.apps.youtube.kids",
            "app.revanced.android.youtube",
            "com.instagram.android",
            "com.instagram.lite",
            "com.snapchat.android",
            "com.facebook.katana",
            "com.facebook.lite",
            "com.zhiliaoapp.musically",
            "com.ss.android.ugc.trill",
            "com.ss.android.ugc.aweme",
            "com.zhiliaoapp.musically.go"
        )
        info.flags = info.flags or
                AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
                AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS

        serviceInfo = info
        Log.i("RB_CFG", "eventTypes=${serviceInfo?.eventTypes}")
        Log.i("RB_CFG", "flags=${serviceInfo?.flags}")

        val app = applicationContext as ReelBreakApplication
        engine = BlockingDecisionEngine(app.repository)
        actionController = ActionController(this)
        detectionManager = ReelsDetectionManager(actionController, engine)
        Log.i("RB_CFG", "Service connected. packages=${serviceInfo?.packageNames?.joinToString()}")

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


//        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
//
//            // Log service info right before processing to check if packageNames has dynamically become null
//            logCurrentServiceInfo("Pre-Processing State ($packageName)")
//
//            // Focus Mode Interceptor
//            if (FocusStateHolder.isFocusActive && isFocusBlocked) {
//                if (FocusStateHolder.getRemainingMillis() > 0L) {
//                    performGlobalAction(GLOBAL_ACTION_HOME)
//                } else {
//                    FocusStateHolder.isFocusActive = false
//                }
//                return
//            }

            val shouldInspectTree = when (event.eventType) {
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED,
                AccessibilityEvent.TYPE_VIEW_SCROLLED -> true
                else -> false
            }
            Log.d("RB_DEBUG", "pkg=${event.packageName} type=${AccessibilityEvent.eventTypeToString(event.eventType)}")
            val rootNode: AccessibilityNodeInfo? = if (shouldInspectTree) rootInActiveWindow else null
            Log.d("RB_DEBUG", "rootNode=${rootNode != null} childCount=${rootNode?.childCount}")
//            if (::detectionManager.isInitialized) {
//                detectionManager.processEvent(event, rootNode)
//            }
//            rootNode?.recycle()  // ✅ Always free the node
//
//        }

        try {
            if (::detectionManager.isInitialized) {
                detectionManager.processEvent(event, rootNode)
            }
        } finally {
            rootNode?.recycle()
        }
    }

//    private fun logCurrentServiceInfo(stage: String) {
//        val info = serviceInfo
//        if (info == null) return
//    }

    override fun onInterrupt() {
        AppDetectorRouter.resetAll()
    }

    override fun onDestroy() {
        super.onDestroy()
//        prefsCacheScope?.cancel()
//        prefsCacheScope = null
//        if (::detectionManager.isInitialized) detectionManager.cancel()
        if (::detectionManager.isInitialized) {
            detectionManager.cancel()
        }
    }
}



//class ReelsAccessibilityService : AccessibilityService() {
//
//    private lateinit var engine: BlockingDecisionEngine
//    private lateinit var actionController: ActionController
//    private val powerManager by lazy { getSystemService(POWER_SERVICE) as PowerManager }
//
//    override fun onServiceConnected() {
//        super.onServiceConnected()
//
//        val info = serviceInfo ?: return
//        info.packageNames = arrayOf(
//            "com.google.android.youtube",
//            "com.instagram.android",
//            "com.snapchat.android"
//        )
//        info.flags = info.flags or
//                AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
//                AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
//        serviceInfo = info
//
//        Log.i("RB_CFG", "eventTypes=${serviceInfo?.eventTypes}")
//        Log.i("RB_CFG", "flags=${serviceInfo?.flags}")
//
//        val app = applicationContext as ReelBreakApplication
//        engine = BlockingDecisionEngine(app.repository)
//        actionController = ActionController(this)
//    }
//
//    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
//        if (event == null) return
//        if (!powerManager.isInteractive) return
//
//        val packageName = event.packageName?.toString() ?: return
//        Log.d("RB_EVT", "pkg=$packageName type=${AccessibilityEvent.eventTypeToString(event.eventType)}")
//
//        val isFocusBlocked =
//            FocusStateHolder.isFocusActive && FocusStateHolder.blockedPackages.contains(packageName)
//
//        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && isFocusBlocked) {
//            if (FocusStateHolder.getRemainingMillis() > 0L) {
//                performGlobalAction(GLOBAL_ACTION_HOME)
//            } else {
//                FocusStateHolder.isFocusActive = false
//            }
//            return
//        }
//
//        val surface = resolveSurface(packageName) ?: return
//        if (!shouldInspectTree(event.eventType)) return
//
//        val rootNode = rootInActiveWindow ?: return
//        val isBlockedSurface = try {
//            rootNode.matchesDetection(surface.getDetectionMethod(), packageName)
//        } finally {
//            rootNode.recycle()
//        }
//
//        Log.d("RB_DETECT", "pkg=$packageName matched=$isBlockedSurface")
//
//        if (!isBlockedSurface) return
//
//        handleBlockedSurface(packageName, event.eventType)
//    }
//
//    private fun resolveSurface(packageName: String): BlockableSurface? {
//        return BlockableSurface.entries.firstOrNull { it.matchesPackage(packageName) }
//    }
//
//    private fun shouldInspectTree(eventType: Int): Boolean {
//        return when (eventType) {
//            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
//            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED,
//            AccessibilityEvent.TYPE_VIEW_SCROLLED -> true
//            else -> false
//        }
//    }
//
//    private fun handleBlockedSurface(packageName: String, eventType: Int) {
//        CoroutineScope(Dispatchers.Main).launch {
//            when (eventType) {
//                AccessibilityEvent.TYPE_VIEW_SCROLLED -> {
//                    when (engine.decide()) {
//                        BlockingDecisionEngine.Decision.BLOCK -> actionController.triggerReelsBlock(packageName)
//                        BlockingDecisionEngine.Decision.ALLOW -> engine.onReelAllowed()
//                        BlockingDecisionEngine.Decision.SKIP_REEL -> Unit
//                    }
//                }
//
//                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
//                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
//                    when (engine.decide()) {
//                        BlockingDecisionEngine.Decision.BLOCK -> actionController.triggerReelsBlock(packageName)
//                        BlockingDecisionEngine.Decision.ALLOW -> Unit
//                        BlockingDecisionEngine.Decision.SKIP_REEL -> Unit
//                    }
//                }
//            }
//        }
//    }
//
//    override fun onInterrupt() {
//        // no-op for now
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//    }
//}