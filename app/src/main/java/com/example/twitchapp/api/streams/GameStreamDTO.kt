package com.example.twitchapp.api.streams

import android.net.Uri
import com.example.twitchapp.model.streams.GameStream
import com.squareup.moshi.Json


class GameStreamDTO(
    @field:Json(name = "user_name") val userName: String?,
    @field:Json(name = "game_name") val gameName: String?,
    @field:Json(name = "viewer_count") val viewerCount: Long?,
    @field:Json(name = "thumbnail_url")
    val imageUrl: Uri?
) {
    fun toModel() =
        GameStream(
            userName = userName ?: "Unknown",
            gameName = gameName ?: "Unknown",
            viewerCount = viewerCount ?: 0,
            imageUrl = imageUrl
        )
}