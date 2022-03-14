package com.example.twitchapp.api.game

import android.net.Uri
import com.example.twitchapp.model.game.Game
import com.squareup.moshi.Json

class GameDTO(
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "box_art_url") val imageUrl: Uri?
) {
    fun toModel() =
        Game(
            id = 0,
            name = name ?: "Unknown",
            imageUrl = imageUrl
        )
}