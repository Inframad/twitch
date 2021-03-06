package com.example.twitchapp.model.notifications

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

sealed class TwitchNotification: Parcelable {
    abstract val messageId: String?
    abstract val title: String?
    abstract val description: String?
    abstract val date: Date
}

@Parcelize
data class GameNotification(
    override val messageId: String?,
    override val title: String?,
    override val description: String?,
    val gameName: String?,
    val streamerName: String?,
    val viewersCount: Long?,
    override val date: Date
): TwitchNotification()

@Parcelize
data class StreamNotification(
    override val messageId: String?,
    override val title: String?,
    override val description: String? = null,
    override val date: Date
): TwitchNotification()