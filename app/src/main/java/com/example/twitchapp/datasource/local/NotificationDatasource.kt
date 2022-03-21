package com.example.twitchapp.datasource.local

import com.example.twitchapp.common.dispatchers.IoDispatcher
import com.example.twitchapp.database.notification.*
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

    fun getAllNotifications(): Flow<List<TwitchNotification>> =
        twitchNotificationDao.getAllNotifications().map {
            it.map { twitchNotificationEntity ->
                when (twitchNotificationEntity.childType) {
                    TwitchNotificationType.GAME -> gameNotificationDao.getGameNotification(
                        twitchNotificationEntity.childId
                    ).toModel()
                    TwitchNotificationType.STREAMS -> streamNotificationDao.getStreamNotification(
                        twitchNotificationEntity.childId
                    ).toModel()
                }
            }
        }.flowOn(ioDispatcher)


    suspend fun saveNotification(
        notification: TwitchNotification
    ) {
        withContext(ioDispatcher) {
            when (notification) {
                is TwitchNotification.GameNotification -> {
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
                is TwitchNotification.StreamNotification ->
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