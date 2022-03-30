package com.example.notification

import com.example.twitchapp.model.notifications.TwitchNotification
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class TwitchNotificationPresentation(
    val messageId: String?,
    val title: String?,
    val description: String?,
    val dateTime: String?
) {
    companion object {
        fun fromModel(model: TwitchNotification, dateTimePattern: String) =
            TwitchNotificationPresentation(
                messageId = model.messageId,
                title = model.title,
                description = model.description,
                dateTime = DateTimeFormatter.ofPattern(dateTimePattern).format(
                    model.date.toInstant().atOffset(
                        ZoneOffset.UTC
                    ).toLocalDateTime()
                ).toString()
            )
    }
}