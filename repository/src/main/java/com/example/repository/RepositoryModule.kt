package com.example.repository

import com.example.repository.notification.NotificationRepository
import com.example.repository.notification.NotificationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(repositoryImpl: RepositoryImpl): Repository

    @Binds
    @Singleton
    abstract fun bindNotificationsRepository(
        repositoryImpl: NotificationRepositoryImpl
    ): NotificationRepository
}