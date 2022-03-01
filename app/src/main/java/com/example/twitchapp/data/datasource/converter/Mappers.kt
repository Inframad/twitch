package com.example.twitchapp.data.datasource.converter

import com.example.twitchapp.data.model.GameStreamDTO
import com.example.twitchapp.data.model.GameStreamDb

fun GameStreamDTO.toGameStreamDb() =
    GameStreamDb(
        username = username ?: "Unknown",
        gameName = gameName ?: "Unknown",
        viewerCount = viewerCount ?: 0,
        imageUrl = imageUrl
    )