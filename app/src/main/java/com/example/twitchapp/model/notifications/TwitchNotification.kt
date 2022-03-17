package com.example.twitchapp.model.notifications

sealed class TwitchNotification {
    abstract val title: String
    abstract val description: String

    data class GameNotification(
        override val title: String,
        override val description: String,
        val gameName: String,
        val streamerName: String,
        val viewersCount: Long,
        val date: Long
    ): TwitchNotification()

    data class StreamNotification(
        override val title: String,
        override val description: String,
        val amount: Int,
        val date: Long
    ): TwitchNotification()
}