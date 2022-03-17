package com.example.twitchapp.model.notifications

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class TwitchNotification {
    abstract val title: String?
    abstract val description: String?

    @Parcelize
    data class GameNotification(
        override val title: String?,
        override val description: String?,
        val gameName: String?,
        val streamerName: String?,
        val viewersCount: Long?,
        val date: Long?
    ): TwitchNotification(), Parcelable

    @Parcelize
    data class StreamNotification(
        override val title: String?,
        override val description: String?,
        val date: Long?
    ): TwitchNotification(), Parcelable
}