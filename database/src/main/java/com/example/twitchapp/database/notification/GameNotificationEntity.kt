package com.example.twitchapp.database.notification

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.twitchapp.database.DbConstants
import com.example.twitchapp.model.notifications.GameNotification
import java.util.*

@Entity(
    tableName = DbConstants.GAME_NOTIFICATIONS_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = TwitchNotificationPivotEntity::class,
            parentColumns = ["childId"],
            childColumns = ["messageId"],
            onDelete = CASCADE
        )
    ],
    indices = [
        Index(
            value = ["messageId"]
        )
    ]
)
class GameNotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val messageId: String,
    val title: String?,
    val description: String?,
    val gameName: String?,
    val streamerName: String?,
    val viewersCount: Long?,
    val date: Date
) {
    fun toModel() =
        GameNotification(
            messageId = messageId,
            title = title,
            description = description,
            gameName = gameName,
            streamerName = streamerName,
            viewersCount = viewersCount,
            date = date
        )

    companion object {
        fun fromModel(gameNotification: GameNotification) =
            GameNotificationEntity(
                messageId = gameNotification.messageId,
                title = gameNotification.title,
                description = gameNotification.description,
                gameName = gameNotification.gameName,
                streamerName = gameNotification.streamerName,
                viewersCount = gameNotification.viewersCount,
                date = gameNotification.date
            )
    }
}