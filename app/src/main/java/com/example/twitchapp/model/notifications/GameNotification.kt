package com.example.twitchapp.model.notifications

data class GameNotification(
    val gameName: String,
    val streamerName: String,
    val viewersCount: Long,
    val date: Long
)