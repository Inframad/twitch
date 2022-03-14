package com.example.twitchapp.api.game

import com.squareup.moshi.Json

data class GamesResponse (
    @field:Json(name = "data") val games: List<GameDTO>
)