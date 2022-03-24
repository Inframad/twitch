package com.example.twitchapp.database.notification

import androidx.room.Dao
import androidx.room.Query
import com.example.twitchapp.database.BaseDao
import com.example.twitchapp.database.DbConstants
import io.reactivex.rxjava3.core.Observable

@Dao
interface TwitchNotificationPivotDao: BaseDao<TwitchNotificationPivotEntity> {

    @Query("SELECT * FROM ${DbConstants.TWITCH_NOTIFICATIONS_TABLE_NAME} ORDER BY date DESC")
    fun getAllNotifications(): Observable<List<TwitchNotificationPivotEntity>>
}