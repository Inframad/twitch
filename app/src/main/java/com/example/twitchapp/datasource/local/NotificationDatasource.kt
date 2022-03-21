package com.example.twitchapp.datasource.local

import com.example.twitchapp.common.dispatchers.IoDispatcher
import com.example.twitchapp.database.notification.*
import com.example.twitchapp.model.Result
import com.example.twitchapp.model.notifications.GameNotification
import com.example.twitchapp.model.notifications.StreamNotification
import com.example.twitchapp.model.notifications.TwitchNotification
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotificationDatasource @Inject constructor(
    private val twitchNotificationDao: TwitchNotificationDao,
    private val gameNotificationDao: GameNotificationDao,
    private val streamNotificationDao: StreamNotificationDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    fun getAllNotifications(): Flow<Result<List<TwitchNotification>>> {
        return twitchNotificationDao.getAllNotifications().map {
            try {
                Result.Success(it.map { twitchNotificationEntity ->
                    when (twitchNotificationEntity.childType) {
                        TwitchNotificationType.GAME -> gameNotificationDao.getGameNotification(
                            twitchNotificationEntity.childId
                        ).toModel()
                        TwitchNotificationType.STREAMS -> streamNotificationDao.getStreamNotification(
                            twitchNotificationEntity.childId
                        ).toModel()
                    }
                }
                )
            } catch (e: Exception) {
                Result.Error(e)
            }
        }.flowOn(ioDispatcher)
    }

    suspend fun saveNotification(
        notification: TwitchNotification
    ) {
        withContext(ioDispatcher) {
            when (notification) {
                is GameNotification -> {
                    twitchNotificationDao.insert(
                        TwitchNotificationEntity(
                            date = notification.date,
                            childType = TwitchNotificationType.GAME,
                            childId = gameNotificationDao.insert(
                                GameNotificationEntity.fromModel(
                                    notification
                                )
                            )
                        )
                    )
                }
                is StreamNotification ->
                    twitchNotificationDao.insert(
                        TwitchNotificationEntity(
                            date = notification.date,
                            childType = TwitchNotificationType.STREAMS,
                            childId = streamNotificationDao.insert(
                                StreamNotificationEntity.fromModel(notification)
                            )
                        )
                    )
            }
        }
    }
}