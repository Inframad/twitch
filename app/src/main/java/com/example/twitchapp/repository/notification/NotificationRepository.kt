package com.example.twitchapp.repository.notification

import com.example.twitchapp.model.Result
import com.example.twitchapp.model.notifications.TwitchNotification
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.SharedFlow

interface NotificationRepository {

    fun getAllNotifications(): Observable<Result<List<TwitchNotification>>>
    suspend fun saveNotification(notification: TwitchNotification)
    fun getNotificationsEvent(): SharedFlow<TwitchNotification>
}