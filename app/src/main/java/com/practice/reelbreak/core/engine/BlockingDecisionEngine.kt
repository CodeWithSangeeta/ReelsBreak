package com.practice.reelbreak.core.engine


import com.practice.reelbreak.data.preferences.UserPreferencesRepository
import kotlinx.coroutines.flow.first

/**
 * BlockingDecisionEngine — The brain of the blocking system.
 *
 * SINGLE RESPONSIBILITY: Given the current state (mode settings,
 * usage today, creator name), decide what action to take.
 *
 * WHY a separate class?
 * - Easy to unit test (no Android dependencies needed)
 * - DetectionManager stays clean — it only detects, doesn't decide
 * - Future modes (Limit, Curated) just add cases here
 *
 * Called from: ReelsDetectionManager (inside AccessibilityService)
 */
class BlockingDecisionEngine(
    private val repository: UserPreferencesRepository
) {

    /**
     * The three possible decisions the engine can make.
     *
     * BLOCK     → Send user away from shorts (GLOBAL_ACTION_BACK)
     * ALLOW     → Let user continue watching
     * SKIP_REEL → Future use for Curated Mode (skip one reel, stay in app)
     */
    enum class Decision {
        BLOCK,
        ALLOW,
        SKIP_REEL
    }

    /**
     * Main decision function.
     *
     * 'suspend' because reading from DataStore is async.
     * Called from a coroutine inside the AccessibilityService.
     *
     * @param detectedCreator — name of the reel creator (for Curated Mode later)
     *                          null means we couldn't read it from the UI tree
     */
    suspend fun decide(detectedCreator: String? = null): Decision {

        // Step 1: Check Strict Mode
        // .first() reads the CURRENT value from the Flow (one-shot read)
        val isStrictMode = repository.isStrictMode.first()
        if (isStrictMode) {
            return Decision.BLOCK  // Strict mode → always block, no questions asked
        }

        // Step 2: Check if today's limit is already exceeded
        // (Limit Mode — we'll fully implement this in next iteration)
        val isLimitExceeded = repository.isLimitExceededToday.first()
        if (isLimitExceeded) {
            return Decision.BLOCK  // Limit hit today → behaves like strict mode
        }

        // Step 3: All checks passed → Allow
        // (Curated Mode check will go here in next iteration)
        return Decision.ALLOW
    }
}