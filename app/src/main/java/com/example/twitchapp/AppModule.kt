package com.example.twitchapp

import androidx.appcompat.app.AppCompatActivity
import com.example.notificationservice.DestinationActivity
import com.example.twitchapp.ui.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlin.reflect.KClass

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @DestinationActivity
    fun provideActivityClass(): KClass<out AppCompatActivity> =
        MainActivity::class
}