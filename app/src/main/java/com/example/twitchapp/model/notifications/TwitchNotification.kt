package com.example.twitchapp.model.notifications

sealed class TwitchNotification {
    data class GameNotification(
        val gameName: String,
        val streamerName: String,
        val viewersCount: Long,
        val date: Long
    ): TwitchNotification()
    data class StreamNotification(
        val amount: Int,
        val date: Long
    ): TwitchNotification()
}