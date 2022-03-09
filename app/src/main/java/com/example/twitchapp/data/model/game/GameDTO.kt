package com.example.twitchapp.data.model.game

import com.squareup.moshi.Json

data class GameDTO(
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "box_art_url") val imageUrl: String?
)