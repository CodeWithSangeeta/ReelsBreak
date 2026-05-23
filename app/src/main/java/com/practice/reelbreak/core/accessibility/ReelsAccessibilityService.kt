//package com.practice.reelbreak.core.accessibility
//
//import android.accessibilityservice.AccessibilityService
//import android.content.Intent
//import android.util.Log
//import android.view.accessibility.AccessibilityEvent
//import android.view.accessibility.AccessibilityNodeInfo
//import com.practice.reelbreak.ReelBreakApplication
//import com.practice.reelbreak.core.action.ActionController
//import com.practice.reelbreak.core.detection.AppDetectorRouter
//import com.practice.reelbreak.core.detection.ReelsDetectionManager
//import com.practice.reelbreak.core.engine.BlockingDecisionEngine
//import com.practice.reelbreak.core.debug.AccessibilityDebugLogger
//import com.practice.reelbreak.data.FocusStateHolder
//import com.practice.reelbreak.data.preferences.UserPreferencesRepository
//import com.practice.reelbreak.domain.model.ActiveBlockMode
//import com.practice.reelbreak.core.registry.ReelsDetectionRegistry
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.SupervisorJob
//import kotlinx.coroutines.cancel
//import kotlinx.coroutines.flow.combine
//import kotlinx.coroutines.launch
//import android.os.PowerManager
//
//
//
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
//
//
//

package com.practice.reelbreak.core.accessibility

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.os.PowerManager
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.practice.reelbreak.ReelBreakApplication
import com.practice.reelbreak.core.action.ActionController
import com.practice.reelbreak.core.detection.AppDetectorRouter
import com.practice.reelbreak.core.detection.ReelsDetectionManager
import com.practice.reelbreak.core.engine.BlockingDecisionEngine
import com.practice.reelbreak.core.registry.ReelsDetectionRegistry
import com.practice.reelbreak.data.FocusStateHolder
import com.practice.reelbreak.data.preferences.UserPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ReelsAccessibilityService : AccessibilityService() {

    private lateinit var engine: BlockingDecisionEngine
    private lateinit var actionController: ActionController
    private var prefsCacheScope: CoroutineScope? = null
    private val powerManager by lazy { getSystemService(POWER_SERVICE) as PowerManager }

    override fun onServiceConnected() {
        super.onServiceConnected()

        // 🔒 Enforce rigid target isolation right at boot time
        val info = serviceInfo ?: return
        info.packageNames = arrayOf(
            "com.google.android.youtube",
            "com.instagram.android",
            "com.snapchat.android",
            "com.zhiliaoapp.musically"
        )
        serviceInfo = info

        val app = applicationContext as ReelBreakApplication
        engine = BlockingDecisionEngine(app.repository)
        actionController = ActionController(this)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Safe filters to protect battery lifecycle
        if (event == null || !powerManager.isInteractive) return

        val packageName = event.packageName?.toString() ?: return

        // 🛑 BROAD SECURITY GUARD: If the app is not on our explicitly declared target list,
        // stop processing instantly. Bank applications will see zero background tracking.
        val isTargetApp = ReelsDetectionRegistry.isDetectionSupported(packageName)
        val isFocusBlocked = FocusStateHolder.isFocusActive && FocusStateHolder.blockedPackages.contains(packageName)

        if (!isTargetApp && !isFocusBlocked) return

        // Process blocking strictly at the package intent level
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {

            // Focus Mode Interceptor
            if (FocusStateHolder.isFocusActive && isFocusBlocked) {
                if (FocusStateHolder.getRemainingMillis() > 0L) {
                    performGlobalAction(GLOBAL_ACTION_BACK)
                } else {
                    FocusStateHolder.isFocusActive = false
                }
                return
            }

            // Distraction Limit Interceptor
            if (isTargetApp) {
                prefsCacheScope?.cancel()
                prefsCacheScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
                prefsCacheScope?.launch {
                    if (engine.decide() == BlockingDecisionEngine.Decision.BLOCK) {
                        Log.d("REELSBREAK", "Package block condition met for $packageName")
                        actionController.triggerBlock(packageName)
                    } else {
                        engine.onReelAllowed()
                    }
                }
            }
        }
    }

    override fun onInterrupt() {
        AppDetectorRouter.resetAll()
    }

    override fun onDestroy() {
        super.onDestroy()
        prefsCacheScope?.cancel()
        prefsCacheScope = null
    }
}
