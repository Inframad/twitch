package com.example.twitchapp.database.notification

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.twitchapp.database.DbConstants
import com.example.twitchapp.model.notifications.GameNotification
import com.example.twitchapp.model.notifications.StreamNotification
import com.example.twitchapp.model.notifications.TwitchNotification
import java.util.*

@Entity(tableName = DbConstants.TWITCH_NOTIFICATIONS_TABLE_NAME,
    indices = [
        Index(
            value = ["childId"],
            unique = true
        )
    ])
class TwitchNotificationPivotEntity(
    @ColumnInfo(name = "childId")
    @PrimaryKey
    val childId: String,
    val date: Date,
    val childType: TwitchNotificationType,
) {
    companion object {
        fun fromTwitchNotification(notification: TwitchNotification) =
            TwitchNotificationPivotEntity(
                date = notification.date,
                childType = when (notification) {
                    is GameNotification -> TwitchNotificationType.GAME
                    is StreamNotification -> TwitchNotificationType.STREAMS
                },
                childId = notification.messageId
            )
    }
}