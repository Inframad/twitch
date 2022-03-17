package com.example.twitchapp.database.notification

import androidx.room.Entity
import com.example.twitchapp.model.notifications.GameNotification

@Entity
class GameNotificationEntity (
    val gameName: String,
    val streamerName: String,
    val viewersCount: Long,
    val date: Long
) {
    fun toModel() =
        GameNotification(
            gameName,
            streamerName,
            viewersCount,
            date
        )
    companion object {
        fun fromModel(gameNotification: GameNotification) =
            GameNotificationEntity(
                gameNotification.gameName,
                gameNotification.streamerName,
                gameNotification.viewersCount,
                gameNotification.date
            )
    }
}