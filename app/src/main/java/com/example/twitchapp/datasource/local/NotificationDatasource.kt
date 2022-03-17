package com.example.twitchapp.datasource.local

import com.example.twitchapp.database.notification.GameNotificationDao
import com.example.twitchapp.database.notification.GameNotificationEntity
import com.example.twitchapp.model.notifications.TwitchNotification
import javax.inject.Inject

class NotificationDatasource @Inject constructor(
    private val gameNotificationDao: GameNotificationDao
) {

    suspend fun getAllGameNotifications() =
        gameNotificationDao.getAllGameNotifications()
            .map { it.toModel() }

    suspend fun saveNotification(
        notification: TwitchNotification
    ) {
        when(notification) {
            is TwitchNotification.GameNotification ->
                gameNotificationDao.insert(GameNotificationEntity.fromModel(notification))
            is TwitchNotification.StreamNotification ->
                TODO()
        }
    }
}