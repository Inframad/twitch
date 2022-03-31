package com.example.notificationservice

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface NotificationServiceModule {

    @Binds
    @Singleton
    fun bindNotifier(twitchNotifier: TwitchNotifier): Notifier
}