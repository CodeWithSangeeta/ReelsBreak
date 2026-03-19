package com.practice.reelbreak.core.detection


import com.practice.reelbreak.core.detection.detectors.AppDetector
import com.practice.reelbreak.core.detection.detectors.GenericReelsDetector
import com.practice.reelbreak.core.detection.detectors.YouTubeDetector
import com.practice.reelbreak.core.registry.SupportedAppsRegistry

object AppDetectorRouter {

    private val genericDetector = GenericReelsDetector()
    private val youtubeDetector = YouTubeDetector()
    fun getDetector(packageName: String?): AppDetector {

        return when (packageName) {

            SupportedAppsRegistry.YOUTUBE -> {
                youtubeDetector
            }

            SupportedAppsRegistry.INSTAGRAM -> {
                genericDetector
            }

            SupportedAppsRegistry.SNAPCHAT -> {
                genericDetector
            }

            SupportedAppsRegistry.TIKTOK -> {
                genericDetector
            }

            SupportedAppsRegistry.FACEBOOK -> {
                genericDetector
            }

            else -> {
                genericDetector
            }
        }
    }
}