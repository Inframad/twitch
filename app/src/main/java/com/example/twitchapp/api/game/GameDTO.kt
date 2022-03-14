package com.example.twitchapp.api.game

import com.example.twitchapp.model.game.Game
import com.squareup.moshi.Json

class GameDTO(
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "box_art_url") val imageUrl: String?
) {
    fun toModel(imageWidth: Int = 1000, imageHeight: Int = 1000) =
        Game(
            id = 0,
            name = name ?: "Unknown",
            imageUrl = imageUrl?.replace("{width}x{height}", "${imageWidth}x${imageHeight}")
        )
}