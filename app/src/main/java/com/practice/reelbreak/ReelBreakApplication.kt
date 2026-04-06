package com.practice.reelbreak


import android.app.Application
import com.practice.reelbreak.data.preferences.UserPreferencesRepository

/**
 * ReelBreakApplication — App-level singleton holder.
 *
 * WHY: Application class is created ONCE when the app process starts
 * and lives until the process dies. Perfect place to hold singletons
 * like our repository that need to be shared across:
 *   - ViewModels (via UI process)
 *   - AccessibilityService (runs in same process)
 *
 * HOW to access anywhere:
 *   val repo = (context.applicationContext as ReelBreakApplication).repository
 */
class ReelBreakApplication : Application() {

    // 'lazy' means it's only created the first time someone accesses it
    // not when the app starts — saves memory
    val repository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(this)
    }
}