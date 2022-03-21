package com.example.twitchapp.repository.notification

import com.example.twitchapp.datasource.local.NotificationDatasource
import com.example.twitchapp.model.Result
import com.example.twitchapp.model.notifications.TwitchNotification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val notificationDatasource: NotificationDatasource
): NotificationRepository {

    override fun getAllNotifications(): Flow<Result<List<TwitchNotification>>> =
        notificationDatasource.getAllNotifications().onStart {
            emit(Result.Loading)
        }

    override suspend fun saveNotification(notification: TwitchNotification) {
        notificationDatasource.saveNotification(notification)
    }
}