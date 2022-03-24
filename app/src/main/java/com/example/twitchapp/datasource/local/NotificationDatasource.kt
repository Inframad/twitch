package com.example.twitchapp.datasource.local

import com.example.twitchapp.database.notification.*
import com.example.twitchapp.model.Result
import com.example.twitchapp.model.notifications.GameNotification
import com.example.twitchapp.model.notifications.StreamNotification
import com.example.twitchapp.model.notifications.TwitchNotification
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class NotificationDatasource @Inject constructor(
    private val twitchNotificationDao: TwitchNotificationPivotDao,
    private val gameNotificationDao: GameNotificationDao,
    private val streamNotificationDao: StreamNotificationDao
) {

    fun getAllNotifications(): Observable<Result<List<TwitchNotification>>> {
        return twitchNotificationDao.getAllNotifications()
            .map {
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
            }.subscribeOn(Schedulers.io())
    }

    fun saveNotification(notification: TwitchNotification): Completable =
        Completable.create {
            when (notification) {
                is GameNotification -> {
                    twitchNotificationDao.insert(
                        TwitchNotificationPivotEntity(
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
                        TwitchNotificationPivotEntity(
                            date = notification.date,
                            childType = TwitchNotificationType.STREAMS,
                            childId = streamNotificationDao.insert(
                                StreamNotificationEntity.fromModel(notification)
                            )
                        )
                    )
            }
            it.onComplete()
        }.subscribeOn(Schedulers.io())
}