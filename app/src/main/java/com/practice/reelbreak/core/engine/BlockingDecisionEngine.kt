package com.practice.reelbreak.core.engine


import android.util.Log
import com.practice.reelbreak.data.preferences.UserPreferencesRepository
import com.practice.reelbreak.domain.model.ActiveBlockMode
import com.practice.reelbreak.domain.model.ProtectionMode
import kotlinx.coroutines.flow.first

class BlockingDecisionEngine(
    private val repository: UserPreferencesRepository
) {
    enum class Decision {
        BLOCK,
        ALLOW,
        SKIP_REEL
    }


//    suspend fun onReelAllowed() {
//        val activeMode = repository.activeMode.first()
//        if (activeMode != ActiveBlockMode.LIMIT) return
//        repository.incrementReelsWatched()
//    }


    suspend fun onMindfulTimeSpent(deltaMillis: Long) {
        val activeMode = repository.activeMode.first()
        if (activeMode != ActiveBlockMode.LIMIT) return
        repository.addTimeSpentMillis(deltaMillis)
    }

//    suspend fun decide(detectedCreator: String? = null): Decision {
//        repository.ensureCountersAreFresh()
//        // ---- DEBUG SNAPSHOT: log everything the engine sees ----
//        val activeMode = repository.activeMode.first()
//        val isStrictMode = repository.isStrictMode.first()
//        val dailyReelLimit = repository.dailyReelLimit.first()
//        val dailyTimeLimitMinutes = repository.dailyTimeLimitMinutes.first()
//        val reelsWatched = repository.reelsWatchedToday.first()
//        val timeSpentMillis = repository.timeSpentTodayMillis.first()
//        val isLimitExceeded = repository.isLimitExceededNow.first()
//
//
//        if (activeMode == ActiveBlockMode.STRICT && isStrictMode) {
//            Log.d("ENGINE_DEBUG", "Decision=BLOCK (Strict mode)")
//            return Decision.BLOCK
//        }
//
//        // Step 2: Limit mode – only if active
//        if (activeMode == ActiveBlockMode.LIMIT) {
//            if (isLimitExceeded) return Decision.BLOCK
//
//            val reelLimitHit = dailyReelLimit > 0 && reelsWatched >= dailyReelLimit
//            val timeLimitHit = dailyTimeLimitMinutes > 0 &&
//                    timeSpentMillis >= dailyTimeLimitMinutes * 60_000L
//
//            if (reelLimitHit || timeLimitHit) {
//                repository.markLimitExceededForCurrentWindow()
//                return Decision.BLOCK
//            }
//        }
//
//        // Step 3: Smart/Curated mode – later
//        Log.d("ENGINE_DEBUG", "Decision=ALLOW (no strict, no limit hit)")
//        return Decision.ALLOW
//    }

    suspend fun decide(detectedCreator: String? = null): Decision {
        repository.ensureCountersAreFresh()

        val activeMode = repository.activeMode.first()
        val isStrictMode = repository.isStrictMode.first()

        // PAUSED: never block, just observe
        val protectionMode = repository.protectionMode.first()
        if (protectionMode == ProtectionMode.PAUSED) return Decision.ALLOW

        // DEFAULT: instant block always
        if (activeMode == ActiveBlockMode.STRICT && isStrictMode) return Decision.BLOCK

        // MINDFUL: limit-based
        if (activeMode == ActiveBlockMode.LIMIT) {
            val isLimitExceeded = repository.isLimitExceededNow.first()
            if (isLimitExceeded) return Decision.BLOCK

            val dailyReelLimit        = repository.dailyReelLimit.first()
            val dailyTimeLimitMinutes = repository.dailyTimeLimitMinutes.first()
            val reelsWatched          = repository.reelsWatchedToday.first()
            val timeSpentMillis       = repository.timeSpentTodayMillis.first()

            val reelLimitHit = dailyReelLimit > 0 && reelsWatched >= dailyReelLimit
            val timeLimitHit = dailyTimeLimitMinutes > 0 && timeSpentMillis >= dailyTimeLimitMinutes * 60000L

            if (reelLimitHit || timeLimitHit) {
                repository.markLimitExceededForCurrentWindow()
                return Decision.BLOCK
            }
        }

        return Decision.ALLOW
    }

    suspend fun onReelAllowed() {
        val protectionMode = repository.protectionMode.first()
        // Count reels in PAUSED and MINDFUL, not in DEFAULT (DEFAULT blocks instantly anyway)
        if (protectionMode == ProtectionMode.DEFAULT) return

        val activeMode = repository.activeMode.first()
        if (activeMode != ActiveBlockMode.LIMIT) return
        repository.incrementReelsWatched()
    }
}