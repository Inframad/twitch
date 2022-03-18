package com.example.twitchapp.repository.notification

import com.example.twitchapp.datasource.local.NotificationDatasource
import com.example.twitchapp.model.notifications.TwitchNotification
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val notificationDatasource: NotificationDatasource
) {

    suspend fun getAll(): List<TwitchNotification> =
        notificationDatasource.getAllNotifications()

    suspend fun save(notification: TwitchNotification) {
        notificationDatasource.saveNotification(notification)
    }
}