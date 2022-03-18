package com.example.twitchapp.repository.notification

import com.example.twitchapp.datasource.local.NotificationDatasource
import com.example.twitchapp.model.notifications.TwitchNotification
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val notificationDatasource: NotificationDatasource
) {

    fun getAll(): Flow<List<TwitchNotification>> = //TODO Naming
        notificationDatasource.getAllNotifications()

    suspend fun save(notification: TwitchNotification) {
        notificationDatasource.saveNotification(notification)
    }
}