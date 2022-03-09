package com.example.twitchapp.data.model.streams

import java.util.*

data class GameStream(
    val GUID: String = UUID.randomUUID().toString(),
    val userName: String,
    val gameName: String,
    val viewerCount: Long,
    val imageUrl: String?
)