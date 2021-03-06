package com.example.twitchapp.database.notification

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.twitchapp.database.DbConstants
import com.example.twitchapp.model.notifications.StreamNotification
import java.util.*

@Entity(tableName = DbConstants.STREAM_NOTIFICATIONS_TABLE_NAME)
class StreamNotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val messageId: String?,
    val title: String?,
    val description: String?,
    val date: Date
) {
    fun toModel() =
        StreamNotification(
            messageId = messageId,
            title = title,
            description = description,
            date = date
        )

    companion object {
        fun fromModel(streamNotification: StreamNotification) =
            StreamNotificationEntity(
                messageId = streamNotification.messageId,
                title = streamNotification.title,
                description = streamNotification.description,
                date = streamNotification.date
            )
    }
}