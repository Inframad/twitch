package com.example.twitchapp.database.notification

import androidx.room.Dao
import androidx.room.Query
import com.example.twitchapp.database.BaseDao
import com.example.twitchapp.database.DbConstants

@Dao
interface GameNotificationDao:BaseDao<GameNotificationEntity> {

    @Query("SELECT * FROM ${DbConstants.GAME_NOTIFICATIONS_TABLE_NAME} WHERE id=:id")
    suspend fun getGameNotification(id: Long): GameNotificationEntity
}