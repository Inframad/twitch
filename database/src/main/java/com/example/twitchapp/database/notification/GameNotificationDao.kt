package com.example.twitchapp.database.notification

import androidx.room.Dao
import androidx.room.Query
import com.example.twitchapp.database.BaseDao
import com.example.twitchapp.database.DbConstants
import io.reactivex.rxjava3.core.Single

@Dao
interface GameNotificationDao: BaseDao<GameNotificationEntity> {

    @Query("SELECT * FROM ${DbConstants.GAME_NOTIFICATIONS_TABLE_NAME} WHERE id=:id")
    fun getGameNotification(id: Long): Single<GameNotificationEntity>
}