package com.example.twitchapp.di

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.twitchapp.di.data.DataModule
import com.example.twitchapp.di.data.NetworkModule
import com.example.twitchapp.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        DataModule::class
    ]
)
interface AppComponent {

    @Singleton
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: Fragment)
}