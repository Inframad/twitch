package com.example.twitchapp.data.model.streams

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class GameStream(
    val accessKey: String = UUID.randomUUID().toString(),
    val userName: String,
    val gameName: String,
    val viewerCount: Long,
    val imageUrl: String?
) : Parcelable