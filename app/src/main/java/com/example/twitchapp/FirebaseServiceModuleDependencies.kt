package com.example.twitchapp

import com.example.repository.notification.NotificationRepositoryImpl
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FirebaseServiceModuleDependencies {

    fun bindRepository(): NotificationRepositoryImpl

    fun bindNotifier(): TwitchNotifier
}