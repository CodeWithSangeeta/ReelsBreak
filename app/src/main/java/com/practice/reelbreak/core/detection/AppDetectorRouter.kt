package com.practice.reelbreak.core.detection


import com.practice.reelbreak.core.detection.detectors.AppDetector
import com.practice.reelbreak.core.detection.detectors.GenericReelsDetector
import com.practice.reelbreak.core.detection.detectors.InstagramReelsDetector
import com.practice.reelbreak.core.detection.detectors.SnapchatSpotlightDetector
import com.practice.reelbreak.core.detection.detectors.YouTubeDetector
import com.practice.reelbreak.core.registry.SupportedAppsRegistry

object AppDetectorRouter {

    private val genericDetector = GenericReelsDetector()
    private val youtubeDetector = YouTubeDetector()
    private val instagramDetector = InstagramReelsDetector()
    private val snapchatDetector = SnapchatSpotlightDetector()
    fun getDetector(packageName: String?): AppDetector {

        return when (packageName) {

            SupportedAppsRegistry.YOUTUBE -> {
                youtubeDetector
            }

            SupportedAppsRegistry.INSTAGRAM -> {
                instagramDetector
            }

            SupportedAppsRegistry.SNAPCHAT -> {
                snapchatDetector
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