package com.practice.reelbreak


import android.app.Application
import com.practice.reelbreak.data.preferences.UserPreferencesRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ReelBreakApplication : Application() {

    val repository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(this)
    }
}