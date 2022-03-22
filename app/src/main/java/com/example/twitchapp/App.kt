package com.example.twitchapp

import android.app.Activity
import android.app.Application
import android.os.Bundle
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    private var _currentAppState: AppState? = null
    val currentAppState get() = _currentAppState

    init {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
                _currentAppState = AppState.OnActivityCreated
            }

            override fun onActivityStarted(p0: Activity) {
                _currentAppState = AppState.OnActivityStarted
            }

            override fun onActivityResumed(p0: Activity) {
                _currentAppState = AppState.OnActivityResumed
            }

            override fun onActivityPaused(p0: Activity) {
                _currentAppState = AppState.OnActivityPaused
            }

            override fun onActivityStopped(p0: Activity) {
                _currentAppState = AppState.OnActivityStopped
            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
                _currentAppState = AppState.OnActivitySaveInstanceState
            }

            override fun onActivityDestroyed(p0: Activity) {
                _currentAppState = AppState.OnActivityDestroyed
            }

        })
    }
}

