package com.example.twitchapp.data.model.game

import com.squareup.moshi.Json

data class GamesResponse (
    @field:Json(name = "data") val games: List<GameDTO>
)