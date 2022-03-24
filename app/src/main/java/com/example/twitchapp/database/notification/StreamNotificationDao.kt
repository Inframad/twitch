package com.example.twitchapp.database.notification

import androidx.room.Dao
import androidx.room.Query
import com.example.twitchapp.database.BaseDao
import com.example.twitchapp.database.DbConstants

@Dao
interface StreamNotificationDao : BaseDao<StreamNotificationEntity> {

    @Query("SELECT * FROM ${DbConstants.STREAM_NOTIFICATIONS_TABLE_NAME} WHERE id=:id")
    fun getStreamNotification(id: Long): StreamNotificationEntity
}