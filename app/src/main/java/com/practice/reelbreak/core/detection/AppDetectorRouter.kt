package com.practice.reelbreak.core.detection


import com.practice.reelbreak.core.detection.detectors.AppDetector
//import com.practice.reelbreak.core.detection.detectors.FacebookReelsDetector
import com.practice.reelbreak.core.detection.detectors.InstagramReelsDetector
import com.practice.reelbreak.core.detection.detectors.SnapchatSpotlightDetector
import com.practice.reelbreak.core.detection.detectors.YouTubeReelsDetector
import com.practice.reelbreak.core.registry.SupportedAppsRegistry

object AppDetectorRouter {

    private val youtubeDetector = YouTubeReelsDetector()
    private val instagramDetector = InstagramReelsDetector()
    private val snapchatDetector = SnapchatSpotlightDetector()
  //  private val facebookDetector = FacebookReelsDetector()
    fun getDetector(packageName: String?): AppDetector? {

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


//            SupportedAppsRegistry.FACEBOOK -> {
//                facebookDetector
//            }
//            SupportedAppsRegistry.FACEBOOK_LITE -> {
//                facebookDetector
//            }

            else -> {
              null
            }
        }
    }


    fun resetAll() {
      //  facebookDetector.reset()
    }
}