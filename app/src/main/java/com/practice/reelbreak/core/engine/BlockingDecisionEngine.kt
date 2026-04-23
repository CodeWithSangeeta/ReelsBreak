package com.practice.reelbreak.core.engine


import com.practice.reelbreak.data.preferences.UserPreferencesRepository
import com.practice.reelbreak.domain.model.ActiveBlockMode
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

    suspend fun onReelAllowed() {
        // Count reel
        repository.incrementReelsWatched()
        // Time tracking: for v1 just add a fixed small chunk, later use real elapsed time
        repository.addTimeSpent(minutes = 1) // or a better estimate later
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

        val activeMode = repository.activeMode.first()
        // Step 1: Check Strict Mode
        // .first() reads the CURRENT value from the Flow (one-shot read)
        val isStrictMode = repository.isStrictMode.first()
        if (activeMode == ActiveBlockMode.STRICT && isStrictMode) {
            return Decision.BLOCK
        }

        // Step 2: Limit mode – only if active
        if (activeMode == ActiveBlockMode.LIMIT) {
            val isLimitExceeded = repository.isLimitExceededToday.first()
            if (isLimitExceeded) return Decision.BLOCK

            val dailyReelLimit = repository.dailyReelLimit.first()
            val dailyTimeLimit = repository.dailyTimeLimitMinutes.first()
            val reelsWatched = repository.reelsWatchedToday.first()
            val timeSpent = repository.timeSpentTodayMinutes.first()

            val reelLimitHit = dailyReelLimit > 0 && reelsWatched >= dailyReelLimit
            val timeLimitHit = dailyTimeLimit > 0 && timeSpent >= dailyTimeLimit

            if (reelLimitHit || timeLimitHit) {
                repository.markLimitExceededToday()
                return Decision.BLOCK
            }
        }

        // Step 3: Smart/Curated mode – later
        return Decision.ALLOW
    }
}