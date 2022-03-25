package com.example.twitchapp.datasource.local

import com.example.twitchapp.database.notification.*
import com.example.twitchapp.model.notifications.GameNotification
import com.example.twitchapp.model.notifications.StreamNotification
import com.example.twitchapp.model.notifications.TwitchNotification
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class NotificationDatasource @Inject constructor(
    private val twitchNotificationDao: TwitchNotificationPivotDao,
    private val gameNotificationDao: GameNotificationDao,
    private val streamNotificationDao: StreamNotificationDao
) {

    fun getAllNotifications(): Observable<List<TwitchNotification>> {
        return twitchNotificationDao.getAllNotifications()
            .flatMap { items ->
                Observable.fromIterable(items)
                    .flatMapSingle { twitchNotificationEntity ->
                        when (twitchNotificationEntity.childType) {
                            TwitchNotificationType.GAME -> gameNotificationDao.getGameNotification(
                                twitchNotificationEntity.childId
                            ).map { it.toModel() }
                            TwitchNotificationType.STREAMS -> streamNotificationDao.getStreamNotification(
                                twitchNotificationEntity.childId
                            ).map { it.toModel() }
                        }
                    }
                    .toList()
                    .toObservable()
            }
            .subscribeOn(Schedulers.io())
    }

    fun saveNotification(twitchNotification: TwitchNotification): Completable =
        Single.just(twitchNotification)
            .flatMap { notification ->
                when (notification) {
                    is GameNotification -> {
                        gameNotificationDao.insert(
                            GameNotificationEntity.fromModel(
                                notification
                            )
                        ).flatMap {
                            twitchNotificationDao.insert(
                                TwitchNotificationPivotEntity(
                                    date = notification.date,
                                    childType = TwitchNotificationType.GAME,
                                    childId = it
                                )
                            )
                        }
                    }
                    is StreamNotification ->
                        streamNotificationDao.insert(
                            StreamNotificationEntity.fromModel(
                                notification
                            )
                        ).flatMap {
                            twitchNotificationDao.insert(
                                TwitchNotificationPivotEntity(
                                    date = notification.date,
                                    childType = TwitchNotificationType.STREAMS,
                                    childId = it
                                )
                            )
                        }
                }
            }.subscribeOn(Schedulers.io()).ignoreElement()
}