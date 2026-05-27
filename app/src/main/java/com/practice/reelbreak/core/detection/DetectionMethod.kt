package com.practice.reelbreak.core.detection

//sealed class DetectionMethod {
//    data class ViewId(val viewId: String) : DetectionMethod()
//    data class AnyOf(val methods: List<DetectionMethod>) : DetectionMethod()
//}




/**
 * Describes how to detect if blocked content is visible for a given app.
 * Sourced from Scrolless open-source project.
 */
sealed class DetectionMethod {

    /** Match a stable view ID in the accessibility tree */
    data class ViewId(val viewId: String) : DetectionMethod()

    /** Match any visible node with one of these exact content descriptions */
    data class ContentDescriptions(val contentDescriptions: Set<String>) : DetectionMethod()

    data class ContentDescriptionPrefix(
        val prefixes: Set<String>,
        val requireSelected: Boolean = false,
        val maxTopScreenFraction: Float? = null
    ) : DetectionMethod()

    /** Match any of the given detection methods (OR logic) */
    data class AnyOf(val detectionMethods: List<DetectionMethod>) : DetectionMethod()
}