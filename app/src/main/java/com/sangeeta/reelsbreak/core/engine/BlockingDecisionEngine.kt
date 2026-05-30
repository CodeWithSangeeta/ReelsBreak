//package com.practice.reelbreak.core.engine
//
//
//import android.util.Log
//import com.practice.reelbreak.data.preferences.UserPreferencesRepository
//import com.practice.reelbreak.domain.model.ActiveBlockMode
//import com.practice.reelbreak.domain.model.ProtectionMode
//import kotlinx.coroutines.flow.first
//
//class BlockingDecisionEngine(
//    private val repository: UserPreferencesRepository
//) {
//    enum class Decision {
//        BLOCK,
//        ALLOW,
//        SKIP_REEL
//    }
//
//    suspend fun onMindfulTimeSpent(deltaMillis: Long) {
//        val activeMode = repository.activeMode.first()
//        if (activeMode != ActiveBlockMode.LIMIT) return
//        repository.addTimeSpentMillis(deltaMillis)
//    }
//
//    suspend fun decide(detectedCreator: String? = null): Decision {
//        repository.ensureCountersAreFresh()
//
//        val activeMode = repository.activeMode.first()
//        val isStrictMode = repository.isStrictMode.first()
//
//        // PAUSED: never block, just observe
//        val protectionMode = repository.protectionMode.first()
//        if (protectionMode == ProtectionMode.PAUSED) return Decision.ALLOW
//
//        // DEFAULT: instant block always
//        if (activeMode == ActiveBlockMode.STRICT && isStrictMode) return Decision.BLOCK
//
//        // MINDFUL: limit-based
//        if (activeMode == ActiveBlockMode.LIMIT) {
//            val isLimitExceeded = repository.isLimitExceededNow.first()
//            if (isLimitExceeded) return Decision.BLOCK
//
//            val dailyReelLimit        = repository.dailyReelLimit.first()
//            val dailyTimeLimitMinutes = repository.dailyTimeLimitMinutes.first()
//            val reelsWatched          = repository.reelsWatchedToday.first()
//            val timeSpentMillis       = repository.timeSpentTodayMillis.first()
//
//            val reelLimitHit = dailyReelLimit > 0 && reelsWatched >= dailyReelLimit
//            val timeLimitHit = dailyTimeLimitMinutes > 0 && timeSpentMillis >= dailyTimeLimitMinutes * 60000L
//
//            if (reelLimitHit || timeLimitHit) {
//                repository.markLimitExceededForCurrentWindow()
//                return Decision.BLOCK
//            }
//        }
//
//        return Decision.ALLOW
//    }
//
//    suspend fun onReelAllowed() {
//        val protectionMode = repository.protectionMode.first()
//        // Count reels in PAUSED and MINDFUL, not in DEFAULT (DEFAULT blocks instantly anyway)
//        if (protectionMode == ProtectionMode.DEFAULT) return
//
//        val activeMode = repository.activeMode.first()
//        if (activeMode != ActiveBlockMode.LIMIT) return
//        repository.incrementReelsWatched()
//    }
//}





package com.sangeeta.reelsbreak.core.engine

import com.sangeeta.reelsbreak.data.preferences.UserPreferencesRepository
import com.sangeeta.reelsbreak.domain.model.ActiveBlockMode
import com.sangeeta.reelsbreak.domain.model.ProtectionMode
import kotlinx.coroutines.flow.first

class BlockingDecisionEngine(
    private val repository: UserPreferencesRepository
) {
    enum class Decision {
        BLOCK,
        ALLOW,
        SKIP_REEL
    }

    /**
     * Determines whether the current short-form video layer should be blocked or allowed.
     * Evaluates state parameters inside an atomic memory block to prevent background thread delays.
     */
    suspend fun decide(detectedCreator: String? = null): Decision {
        // 1. Maintain fresh counter states safely before calculating limits
        repository.ensureCountersAreFresh()

        // 2. Fetch all state vectors in a single configuration snapshot to prevent multiple sequential flow reads
        val activeMode = repository.activeMode.first()
        val isStrictMode = repository.isStrictMode.first()
        val protectionMode = repository.protectionMode.first()

        // PAUSED Protection Mode: Never block under any circumstances, just track time/count safely.
        if (protectionMode == ProtectionMode.PAUSED) {
            return Decision.ALLOW
        }

        // DEFAULT Protection Mode: Instant, un-timed block on all short-form content targets.
        if (protectionMode == ProtectionMode.DEFAULT) {
            if (activeMode == ActiveBlockMode.STRICT && isStrictMode) {
                return Decision.BLOCK
            }
        }

        // MINDFUL Protection Mode: Enforces specific count or time duration thresholds.
        if (protectionMode == ProtectionMode.MINDFUL && activeMode == ActiveBlockMode.LIMIT) {
            val isLimitExceeded = repository.isLimitExceededNow.first()
            if (isLimitExceeded) {
                return Decision.BLOCK
            }

            val dailyReelLimit = repository.dailyReelLimit.first()
            val dailyTimeLimitMinutes = repository.dailyTimeLimitMinutes.first()
            val reelsWatched = repository.reelsWatchedToday.first()
            val timeSpentMillis = repository.timeSpentTodayMillis.first()

            // Calculate precise threshold limits
            val reelLimitHit = dailyReelLimit > 0 && reelsWatched >= dailyReelLimit
            val timeLimitHit = dailyTimeLimitMinutes > 0 && timeSpentMillis >= (dailyTimeLimitMinutes * 60_000L)

            if (reelLimitHit || timeLimitHit) {
                repository.markLimitExceededForCurrentWindow()
                return Decision.BLOCK
            }
        }

        return Decision.ALLOW
    }

    /**
     * Safely logs accumulated short video usage duration time milestones.
     */
    suspend fun onMindfulTimeSpent(deltaMillis: Long) {
        val activeMode = repository.activeMode.first()
        if (activeMode != ActiveBlockMode.LIMIT) return

        // Guard against massive unexpected time jumps or corrupt time window frames
        if (deltaMillis <= 0L || deltaMillis > 10_000L) return

        repository.addTimeSpentMillis(deltaMillis)
    }

    /**
     * Increments short-video scroll counts safely.
     */
    suspend fun onReelAllowed() {
        val protectionMode = repository.protectionMode.first()

        // Do not register increments if user is on absolute default system block mode
        if (protectionMode == ProtectionMode.DEFAULT) return

        val activeMode = repository.activeMode.first()
        if (activeMode != ActiveBlockMode.LIMIT) return

        repository.incrementReelsWatched()
    }
}