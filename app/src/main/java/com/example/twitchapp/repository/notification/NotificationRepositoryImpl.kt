package com.example.twitchapp.repository.notification

import com.example.twitchapp.datasource.local.NotificationDatasource
import com.example.twitchapp.model.Result
import com.example.twitchapp.model.notifications.TwitchNotification
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val notificationDatasource: NotificationDatasource
): NotificationRepository {

    private val _notificationsEventFlow = MutableSharedFlow<TwitchNotification>()

    override fun getAllNotifications(): Observable<Result<List<TwitchNotification>>> =
        notificationDatasource.getAllNotifications()

    override suspend fun saveNotification(notification: TwitchNotification) {
        _notificationsEventFlow.emit(notification)
        notificationDatasource.saveNotification(notification)
    }

    override fun getNotificationsEvent(): SharedFlow<TwitchNotification> =
        _notificationsEventFlow.asSharedFlow()
}