package com.example.twitchapp.database.notification

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.twitchapp.database.DbConstants
import java.util.*

@Entity(tableName = DbConstants.TWITCH_NOTIFICATIONS_TABLE_NAME)
class TwitchNotificationPivotEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: Date,
    val childType: TwitchNotificationType,
    val childId: Long
)