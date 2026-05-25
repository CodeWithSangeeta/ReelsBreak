package com.practice.reelbreak.core.detection

import com.practice.reelbreak.core.detection.detectors.AppDetector
import com.practice.reelbreak.core.detection.detectors.InstagramReelsDetector
import com.practice.reelbreak.core.detection.detectors.SnapchatReelsDetector
import com.practice.reelbreak.core.detection.detectors.YouTubeReelsDetector
import com.practice.reelbreak.core.registry.ReelsDetectionRegistry

object AppDetectorRouter {
    private val youtubeDetector = YouTubeReelsDetector()
    private val instagramDetector = InstagramReelsDetector()
    private val snapchatDetector = SnapchatReelsDetector()

    fun getDetector(packageName: String?): AppDetector? {

        return when (packageName) {

            ReelsDetectionRegistry.YOUTUBE -> {
                youtubeDetector
            }

            ReelsDetectionRegistry.INSTAGRAM -> {
                instagramDetector
            }

            ReelsDetectionRegistry.SNAPCHAT -> {
                snapchatDetector
            }

            else -> {
              null
            }
        }
    }


    fun resetAll() {
        instagramDetector.reset()
        snapchatDetector.reset()
        youtubeDetector.reset()
    }
}