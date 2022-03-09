package com.example.twitchapp.data.model.streams

import com.squareup.moshi.Json

data class GameStreamDTO(
    @field:Json(name = "user_name") val userName: String?,
    @field:Json(name = "game_name") val gameName: String?,
    @field:Json(name = "viewer_count") val viewerCount: Long?,
    @field:Json(name = "thumbnail_url") val imageUrl: String?
)