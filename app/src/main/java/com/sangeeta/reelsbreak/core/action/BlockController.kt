package com.sangeeta.reelsbreak.core.action

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.sangeeta.reelsbreak.core.detection.AppDetectorRouter

class BlockController(
    private val service: AccessibilityService
) {
    fun goBackInsideApp(expectedPackage: String?) {
        val root = service.rootInActiveWindow
        val activePackage = root?.packageName?.toString()

        if (expectedPackage != null && activePackage != expectedPackage) {
            root?.recycle()
            return
        }
        root?.recycle()
//   service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
        service.performGlobalAction(
            AccessibilityService.GLOBAL_ACTION_BACK
        )

        Handler(Looper.getMainLooper()).postDelayed({
//            val pkg = service.rootInActiveWindow
//                ?.packageName
//                ?.toString()
//
////            if (pkg == expectedPackage)
//            if (
//                AppDetectorRouter.isBlockedContentVisible(
//                    expectedPackage,
//                    root
//                )
//            )
//            {
//
//
//                service.performGlobalAction(
//                    AccessibilityService.GLOBAL_ACTION_BACK
//                )
//            }

            val expectedPkg = expectedPackage ?: return@postDelayed

            val root = service.rootInActiveWindow

            val stillOnReels =
                AppDetectorRouter.isBlockedContentVisible(
                    expectedPkg,
                    root
                )

            if (stillOnReels) {
             val success = service.performGlobalAction(
                    AccessibilityService.GLOBAL_ACTION_BACK
                )
                Log.d(
                    "RB_ACTION",
                    "BACK requested pkg=$expectedPackage"
                )
            }

            root?.recycle()

            Log.d(
                "RB_BLOCK",
                "stillOnReels=" +
                        AppDetectorRouter.isBlockedContentVisible(
                            expectedPkg,
                            root
                        )
            )

        }, 150)
    }


    fun goHome(expectedPackage: String?) {
        val root = service.rootInActiveWindow
        val activePackage = root?.packageName?.toString()
        root?.recycle()
      service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME)
    }
}