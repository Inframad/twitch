package com.example.twitchapp.repository.notification

import com.example.twitchapp.model.notifications.TwitchNotification

interface NotificationRepository {

    suspend fun getAll(): List<TwitchNotification>
    fun save(notification: TwitchNotification)
}