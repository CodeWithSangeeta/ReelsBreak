package com.practice.reelbreak.core.engine


import android.util.Log
import com.practice.reelbreak.data.preferences.UserPreferencesRepository
import com.practice.reelbreak.domain.model.ActiveBlockMode
import kotlinx.coroutines.flow.first

class BlockingDecisionEngine(
    private val repository: UserPreferencesRepository
) {
    enum class Decision {
        BLOCK,
        ALLOW,
        SKIP_REEL
    }


    suspend fun onReelAllowed() {
        val activeMode = repository.activeMode.first()
        if (activeMode != ActiveBlockMode.LIMIT) return
        repository.incrementReelsWatched()
    }


    suspend fun onMindfulTimeSpent(deltaMillis: Long) {
        val activeMode = repository.activeMode.first()
        if (activeMode != ActiveBlockMode.LIMIT) return
        repository.addTimeSpentMillis(deltaMillis)
    }

    suspend fun decide(detectedCreator: String? = null): Decision {
        repository.ensureCountersAreFresh()
        // ---- DEBUG SNAPSHOT: log everything the engine sees ----
        val activeMode = repository.activeMode.first()
        val isStrictMode = repository.isStrictMode.first()
        val dailyReelLimit = repository.dailyReelLimit.first()
        val dailyTimeLimitMinutes = repository.dailyTimeLimitMinutes.first()
        val reelsWatched = repository.reelsWatchedToday.first()
        val timeSpentMillis = repository.timeSpentTodayMillis.first()
        val isLimitExceeded = repository.isLimitExceededNow.first()


        if (activeMode == ActiveBlockMode.STRICT && isStrictMode) {
            Log.d("ENGINE_DEBUG", "Decision=BLOCK (Strict mode)")
            return Decision.BLOCK
        }

        // Step 2: Limit mode – only if active
        if (activeMode == ActiveBlockMode.LIMIT) {
            if (isLimitExceeded) return Decision.BLOCK

            val reelLimitHit = dailyReelLimit > 0 && reelsWatched >= dailyReelLimit
            val timeLimitHit = dailyTimeLimitMinutes > 0 &&
                    timeSpentMillis >= dailyTimeLimitMinutes * 60_000L

            if (reelLimitHit || timeLimitHit) {
                repository.markLimitExceededForCurrentWindow()
                return Decision.BLOCK
            }
        }

        // Step 3: Smart/Curated mode – later
        Log.d("ENGINE_DEBUG", "Decision=ALLOW (no strict, no limit hit)")
        return Decision.ALLOW
    }
}