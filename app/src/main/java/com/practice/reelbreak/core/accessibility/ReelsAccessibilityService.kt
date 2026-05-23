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

//package com.practice.reelbreak.core.accessibility
//
//import android.accessibilityservice.AccessibilityService
//import android.accessibilityservice.AccessibilityServiceInfo
//import android.os.PowerManager
//import android.util.Log
//import android.view.accessibility.AccessibilityEvent
//import com.practice.reelbreak.ReelBreakApplication
//import com.practice.reelbreak.core.action.ActionController
//import com.practice.reelbreak.core.detection.AppDetectorRouter
//import com.practice.reelbreak.core.detection.ReelsDetectionManager
//import com.practice.reelbreak.core.engine.BlockingDecisionEngine
//import com.practice.reelbreak.core.registry.ReelsDetectionRegistry
//import com.practice.reelbreak.data.FocusStateHolder
//import com.practice.reelbreak.data.preferences.UserPreferencesRepository
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.SupervisorJob
//import kotlinx.coroutines.cancel
//import kotlinx.coroutines.launch
//
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





// FILE: app/src/main/java/com/practice/reelbreak/core/accessibility/ReelsAccessibilityService.kt

package com.practice.reelbreak.core.accessibility

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.os.PowerManager
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.practice.reelbreak.ReelBreakApplication
import com.practice.reelbreak.core.action.ActionController
import com.practice.reelbreak.core.detection.AppDetectorRouter
import com.practice.reelbreak.core.engine.BlockingDecisionEngine
import com.practice.reelbreak.core.registry.ReelsDetectionRegistry
import com.practice.reelbreak.data.FocusStateHolder
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

    private val DIAG_TAG = "RB_DIAGNOSTIC"

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.i(DIAG_TAG, "==================================================")
        Log.i(DIAG_TAG, "SERVICE INITIALIZED: onServiceConnected called")

        val info = serviceInfo ?: return

        // 🔒 Set your target isolation array
        info.packageNames = arrayOf(
            "com.google.android.youtube",
            "com.instagram.android",
            "com.snapchat.android"
        )

        // 🔧 FIX: Re-enforce your XML flags explicitly so they don't get lost in translation!
        info.flags = info.flags or AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS

        serviceInfo = info

//        // Log the initial configuration directly as loaded from your XML
//        logCurrentServiceInfo("Initial XML Configuration")

        val app = applicationContext as ReelBreakApplication
        engine = BlockingDecisionEngine(app.repository)
        actionController = ActionController(this)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        val packageName = event.packageName?.toString() ?: ""

        // 🔍 DIAGNOSTIC LOG: Capture EVERY event that hits this service before any filters.
        // This will tell us if a banking app package name is leaking into your service.
        Log.d(DIAG_TAG, "EVENT RECEIVED -> pkg: $packageName | eventType: ${AccessibilityEvent.eventTypeToString(event.eventType)}")

        if (!powerManager.isInteractive) return

        val isTargetApp = ReelsDetectionRegistry.isDetectionSupported(packageName)
        val isFocusBlocked = FocusStateHolder.isFocusActive && FocusStateHolder.blockedPackages.contains(packageName)

        if (!isTargetApp && !isFocusBlocked) {
            // 🔍 DIAGNOSTIC LOG: Log exactly when we reject an app and drop it
            Log.v(DIAG_TAG, "GUARD TRIGGERED: Dropping event for unmanaged package: $packageName")
            return
        }

        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            Log.i(DIAG_TAG, "WINDOW STATE CHANGED FOR MANAGED APP: $packageName")

            // Log service info right before processing to check if packageNames has dynamically become null
            logCurrentServiceInfo("Pre-Processing State ($packageName)")

            // Focus Mode Interceptor
            if (FocusStateHolder.isFocusActive && isFocusBlocked) {
                if (FocusStateHolder.getRemainingMillis() > 0L) {
                    Log.w(DIAG_TAG, "FOCUS MODE ACTION: Bouncing user back from $packageName")
                    performGlobalAction(GLOBAL_ACTION_BACK)
                } else {
                    FocusStateHolder.isFocusActive = false
                }
                return
            }

            // Distraction Limit Interceptor (Reels/Shorts target)
            if (isTargetApp) {
                prefsCacheScope?.cancel()
                prefsCacheScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
                prefsCacheScope?.launch {
                    val decision = engine.decide()
                    Log.d(DIAG_TAG, "DECISION ENGINE RESULT: $decision for $packageName")
                    if (decision == BlockingDecisionEngine.Decision.BLOCK) {
                        actionController.triggerBlock(packageName)
                    } else {
                        engine.onReelAllowed()
                    }
                }
            }
        }
    }

    private fun logCurrentServiceInfo(stage: String) {
        val info = serviceInfo
        if (info == null) {
            Log.w(DIAG_TAG, "[$stage] serviceInfo is NULL!")
            return
        }

        val packages = info.packageNames?.joinToString(", ") ?: "ALL PACKAGES (GLOBAL SCOPE / NULL)"
        val flags = info.flags

        Log.i(DIAG_TAG, "--- Accessibility Info Snapshot: $stage ---")
        Log.i(DIAG_TAG, "Mapped Packages: $packages")
        Log.i(DIAG_TAG, "Active Flags: $flags (flagRetrieveInteractiveWindows = ${flags and AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS != 0})")
        Log.i(DIAG_TAG, "Can Retrieve Window Content: ${info.capabilities and AccessibilityServiceInfo.CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT != 0}")
        Log.i(DIAG_TAG, "-------------------------------------------------------")
    }

    override fun onInterrupt() {
        Log.w(DIAG_TAG, "SERVICE INTERRUPTED")
        AppDetectorRouter.resetAll()
    }

    override fun onDestroy() {
        Log.w(DIAG_TAG, "SERVICE DESTROYED")
        super.onDestroy()
        prefsCacheScope?.cancel()
        prefsCacheScope = null
    }
}