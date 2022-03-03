package com.example.twitchapp.data.model

import java.util.*

data class GameStream(
    val GUID: String = UUID.randomUUID().toString(),
    val username: String,
    val gameName: String,
    val viewerCount: Long,
    val imageUrl: String?
)