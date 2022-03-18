package com.example.twitchapp.database.notification

import androidx.room.Dao
import androidx.room.Query
import com.example.twitchapp.database.BaseDao
import com.example.twitchapp.database.DbConstants

@Dao
interface TwitchNotificationDao: BaseDao<TwitchNotificationEntity> {

    @Query("SELECT * FROM ${DbConstants.TWITCH_NOTIFICATIONS_TABLE_NAME} ORDER BY date DESC")
    fun getAllNotifications(): List<TwitchNotificationEntity>
}