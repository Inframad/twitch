package com.example.twitchapp.database.notification

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.twitchapp.model.notifications.TwitchNotification
import java.util.*

@Entity
class StreamNotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String?,
    val description: String?,
    val date: Date
) {
    fun toModel() =
        TwitchNotification.StreamNotification(
            title = title,
            description = description,
            date = date
        )

    companion object {
        fun fromModel(streamNotification: TwitchNotification.StreamNotification) =
            StreamNotificationEntity(
                title = streamNotification.title,
                description = streamNotification.description,
                date = streamNotification.date
            )
    }
}