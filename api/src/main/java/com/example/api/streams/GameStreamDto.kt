package com.example.api.streams

import android.net.Uri
import com.example.api.ApiConst
import com.example.api.ImageUri
import com.example.twitchapp.model.streams.GameStream
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class GameStreamDto(
    @field:Json(name = "user_name") val userName: String?,
    @field:Json(name = "game_name") val gameName: String?,
    @field:Json(name = "viewer_count") val viewerCount: Long?,
    @field:Json(name = "thumbnail_url")
    @ImageUri(ApiConst.STREAM_IMAGE_HEIGHT, ApiConst.STREAM_IMAGE_WIDTH)
    val imageUrl: Uri?
) {
    fun toModel() =
        GameStream(
            userName = userName,
            gameName = gameName,
            viewerCount = viewerCount,
            imageUrl = imageUrl
        )
}