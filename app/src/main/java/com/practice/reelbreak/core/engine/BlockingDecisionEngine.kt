package com.practice.reelbreak.core.engine


import android.util.Log
import com.practice.reelbreak.data.preferences.UserPreferencesRepository
import com.practice.reelbreak.domain.model.ActiveBlockMode
import kotlinx.coroutines.flow.first



/**
 * BlockingDecisionEngine — The brain of the blocking system.
 *
 * SINGLE RESPONSIBILITY: Given the current state (mode settings,
 * usage today, creator name), decide what action to take.
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
        repository.addTimeSpent(minutes = 1)
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
        // ---- DEBUG SNAPSHOT: log everything the engine sees ----
        val activeMode = repository.activeMode.first()
        val isStrictMode = repository.isStrictMode.first()
        val dailyReelLimit = repository.dailyReelLimit.first()
        val dailyTimeLimit = repository.dailyTimeLimitMinutes.first()
        val reelsWatched = repository.reelsWatchedToday.first()
        val timeSpent = repository.timeSpentTodayMinutes.first()
        val isLimitExceeded = repository.isLimitExceededToday.first()

        Log.d(
            "ENGINE_DEBUG",
            "mode=$activeMode strict=$isStrictMode " +
                    "reelLimit=$dailyReelLimit timeLimit=$dailyTimeLimit " +
                    "reelsToday=$reelsWatched timeToday=$timeSpent " +
                    "limitExceeded=$isLimitExceeded"
        )
        // --------------------------------------------------------

        // Step 1: Check Strict Mode
        if (activeMode == ActiveBlockMode.STRICT && isStrictMode) {
            Log.d("ENGINE_DEBUG", "Decision=BLOCK (Strict mode)")
            return Decision.BLOCK
        }

        // Step 2: Limit mode – only if active
        if (activeMode == ActiveBlockMode.LIMIT) {
            if (isLimitExceeded) {
                Log.d("ENGINE_DEBUG", "Decision=BLOCK (Limit already exceeded today)")
                return Decision.BLOCK
            }

            val reelLimitHit =
                dailyReelLimit > 0 && reelsWatched >= dailyReelLimit
            val timeLimitHit =
                dailyTimeLimit > 0 && timeSpent >= dailyTimeLimit

            if (reelLimitHit || timeLimitHit) {
                Log.d(
                    "ENGINE_DEBUG",
                    "Limit just hit (reelHit=$reelLimitHit, timeHit=$timeLimitHit) → markLimitExceeded + BLOCK"
                )
                repository.markLimitExceededToday()
                return Decision.BLOCK
            }
        }

        // Step 3: Smart/Curated mode – later
        Log.d("ENGINE_DEBUG", "Decision=ALLOW (no strict, no limit hit)")
        return Decision.ALLOW
    }
}