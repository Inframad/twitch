package com.example.repository.notification

import com.example.twitchapp.model.notifications.TwitchNotification
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface NotificationRepository {

    fun getAllNotifications(): Observable<List<TwitchNotification>>
    fun saveNotification(notification: TwitchNotification): Completable
    fun getNotificationsEvent(): Observable<TwitchNotification>
}