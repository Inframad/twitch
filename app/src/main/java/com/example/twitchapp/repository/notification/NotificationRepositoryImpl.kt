package com.example.twitchapp.repository.notification

import com.example.twitchapp.datasource.local.NotificationDatasource
import com.example.twitchapp.model.Result
import com.example.twitchapp.model.notifications.TwitchNotification
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val notificationDatasource: NotificationDatasource
): NotificationRepository {

    private val _notificationsEventFlow = MutableSharedFlow<TwitchNotification>()

    override fun getAllNotifications(): Flow<Result<List<TwitchNotification>>> =
        notificationDatasource.getAllNotifications().onStart {
            emit(Result.Loading)
        }

    override suspend fun saveNotification(notification: TwitchNotification) {
        _notificationsEventFlow.emit(notification)
        notificationDatasource.saveNotification(notification)
    }

    override fun getNotificationsEvent(): SharedFlow<TwitchNotification> =
        _notificationsEventFlow.asSharedFlow()
}