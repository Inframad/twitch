package com.example.twitchapp.data.converter

import com.example.twitchapp.data.model.game.Game
import com.example.twitchapp.data.model.game.GameDTO
import com.example.twitchapp.data.model.game.GameEntity
import com.example.twitchapp.data.model.streams.GameStream
import com.example.twitchapp.data.model.streams.GameStreamDTO
import com.example.twitchapp.data.model.streams.GameStreamEntity

fun GameStreamDTO.toGameStream(imageWidth: Int = 1000, imageHeight: Int = 500) =
    GameStream(
        userName = userName ?: "Unknown",
        gameName = gameName ?: "Unknown",
        viewerCount = viewerCount ?: 0,
        imageUrl = imageUrl?.replace("{width}x{height}", "${imageWidth}x${imageHeight}")
    )

fun GameStreamEntity.toGameStream() =
    GameStream(
        accessKey = accessKey,
        userName = userName,
        gameName = gameName,
        viewerCount = viewerCount,
        imageUrl = imageUrl
    )

fun GameStream.toGameStreamEntity() =
    GameStreamEntity(
        accessKey = accessKey,
        userName = userName,
        gameName = gameName,
        viewerCount = viewerCount,
        imageUrl = imageUrl
    )

fun GameDTO.toGame(imageWidth: Int = 1000, imageHeight: Int = 1000) =
    Game(
        id = 0,
        name = name ?: "Unknown",
        imageUrl = imageUrl?.replace("{width}x{height}", "${imageWidth}x${imageHeight}")
    )

fun Game.toGameEntity() =
    GameEntity(
        id = id,
        name = name,
        isFavourite = isFavourite,
        imageUrl = imageUrl
    )

fun GameEntity.toGame() =
    Game(
        id = id,
        name = name,
        isFavourite = isFavourite,
        imageUrl = imageUrl
    )
