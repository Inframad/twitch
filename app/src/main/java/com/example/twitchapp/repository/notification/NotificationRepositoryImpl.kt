package com.example.twitchapp.repository.notification

import com.example.twitchapp.datasource.local.NotificationDatasource
import com.example.twitchapp.model.notifications.TwitchNotification
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val notificationDatasource: NotificationDatasource
): NotificationRepository {

    private val _notificationsEventSubject = PublishSubject.create<TwitchNotification>()

    override fun getAllNotifications(): Observable<List<TwitchNotification>> =
        notificationDatasource.getAllNotifications()

    fun getAllNotifications1(): Single<List<TwitchNotification>> =
        notificationDatasource.getAllNotifications1()

    override fun saveNotification(notification: TwitchNotification): Completable {
        _notificationsEventSubject.onNext(notification)
        return notificationDatasource.saveNotification(notification)
    }

    override fun getNotificationsEvent(): Observable<TwitchNotification> =
        _notificationsEventSubject
}