package com.example.twitchapp

import android.app.Application
import com.example.twitchapp.di.AppComponent
import com.example.twitchapp.di.DaggerAppComponent

class App: Application() {

    val appComponent: AppComponent by lazy {
        initializeComponent()
    }

    private fun initializeComponent(): AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
    }
}