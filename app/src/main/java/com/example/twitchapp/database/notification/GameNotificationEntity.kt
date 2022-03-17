package com.example.twitchapp.database.notification

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.twitchapp.model.notifications.TwitchNotification

@Entity
class GameNotificationEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val gameName: String,
    val streamerName: String,
    val viewersCount: Long,
    val date: Long
) {
    fun toModel() =
        TwitchNotification.GameNotification(
            gameName,
            streamerName,
            viewersCount,
            date
        )
    companion object {
        fun fromModel(gameNotification: TwitchNotification.GameNotification) =
            GameNotificationEntity(
                gameName = gameNotification.gameName,
                streamerName = gameNotification.streamerName,
                viewersCount = gameNotification.viewersCount,
                date = gameNotification.date
            )
    }
}