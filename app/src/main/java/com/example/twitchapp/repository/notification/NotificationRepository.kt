package com.example.twitchapp.repository.notification

import com.example.twitchapp.model.Result
import com.example.twitchapp.model.notifications.TwitchNotification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface NotificationRepository {

    fun getAllNotifications(): Flow<Result<List<TwitchNotification>>>
    suspend fun saveNotification(notification: TwitchNotification)
    fun getNotificationsEvent(): SharedFlow<TwitchNotification>
}