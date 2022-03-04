package com.example.twitchapp.data.converter

import com.example.twitchapp.data.model.GameStream
import com.example.twitchapp.data.model.GameStreamDTO
import com.example.twitchapp.data.model.GameStreamEntity

fun GameStreamDTO.toGameStream(imageWidth: Int = 1000, imageHeight: Int = 500) =
    GameStream(
        username = username ?: "Unknown",
        gameName = gameName ?: "Unknown",
        viewerCount = viewerCount ?: 0,
        imageUrl = imageUrl?.replace("{width}x{height}", "${imageWidth}x${imageHeight}")
    )

fun GameStreamEntity.toGameStream() =
    GameStream(
        GUID = GUID,
        username = username,
        gameName = gameName,
        viewerCount = viewerCount,
        imageUrl = imageUrl
    )

fun GameStream.toGameStreamEntity() =
    GameStreamEntity(
        GUID = GUID,
        username = username,
        gameName = gameName,
        viewerCount = viewerCount,
        imageUrl = imageUrl
    )
