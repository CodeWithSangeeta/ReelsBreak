package com.sangeeta.reelsbreak.core.detection

sealed class DetectionMethod {
    data class ViewId(val viewId: String) : DetectionMethod()
    data class ContentDescriptions(val contentDescriptions: Set<String>) : DetectionMethod()

    data class ContentDescriptionPrefix(
        val prefixes: Set<String>,
        val requireSelected: Boolean = false,
        val maxTopScreenFraction: Float? = null
    ) : DetectionMethod()

    data class AnyOf(val detectionMethods: List<DetectionMethod>) : DetectionMethod()
}