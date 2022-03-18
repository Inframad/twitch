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
                    "GameNotification" -> gameNotificationDao.getGameNotification(
                        twitchNotificationEntity.childId
                    )
                        .toModel()
                    "StreamNotification" -> streamNotificationDao.getStreamNotification(
                        twitchNotificationEntity.childId
                    )
                        .toModel()
                    else -> throw IllegalArgumentException() //TODO
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
                            title = notification.title,
                            description = notification.description,
                            date = notification.date,
                            childType = "GameNotification", //TODO
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
                            title = notification.title,
                            description = notification.description,
                            date = notification.date,
                            childType = "StreamNotification", //TODO
                            childId = streamNotificationDao.insert(
                                StreamNotificationEntity.fromModel(
                                    notification
                                )
                            )
                        )
                    )
            }
        }
    }
}