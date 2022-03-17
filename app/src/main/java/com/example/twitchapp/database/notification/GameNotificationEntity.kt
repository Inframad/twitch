package com.example.twitchapp.database.notification

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.twitchapp.model.notifications.TwitchNotification

@Entity
class GameNotificationEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val gameName: String,
    val streamerName: String,
    val viewersCount: Long,
    val date: Long
) {
    fun toModel() =
        TwitchNotification.GameNotification(
            title = title,
            description = description,
            gameName = gameName,
            streamerName = streamerName,
            viewersCount = viewersCount,
            date = date
        )
    companion object {
        fun fromModel(gameNotification: TwitchNotification.GameNotification) =
            GameNotificationEntity(
                title = gameNotification.title,
                description = gameNotification.description,
                gameName = gameNotification.gameName,
                streamerName = gameNotification.streamerName,
                viewersCount = gameNotification.viewersCount,
                date = gameNotification.date
            )
    }
}