package com.example.twitchapp.database.notification

import androidx.room.Dao
import androidx.room.Query
import com.example.twitchapp.database.BaseDao
import com.example.twitchapp.database.DbConstants
import com.example.twitchapp.database.game.GameEntity

@Dao
interface GameNotificationDao:BaseDao<GameNotificationEntity> {

    @Query("SELECT * FROM ${DbConstants.GAME_NOTIFICATIONS_TABLE_NAME}")
    suspend fun getAllGameNotifications(): List<GameEntity>
}