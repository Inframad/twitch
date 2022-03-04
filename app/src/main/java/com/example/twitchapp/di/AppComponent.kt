package com.example.twitchapp.di

import android.content.Context
import com.example.twitchapp.di.data.DataModule
import com.example.twitchapp.di.data.NetworkModule
import com.example.twitchapp.di.ui.UiModule
import com.example.twitchapp.ui.MainActivity
import com.example.twitchapp.ui.fragment.GameStreamsFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        DataModule::class,
        UiModule::class
    ]
)
interface AppComponent {

    @Singleton
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: GameStreamsFragment)
}