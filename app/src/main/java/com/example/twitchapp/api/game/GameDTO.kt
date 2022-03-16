package com.example.twitchapp.api.game

import android.net.Uri
import com.example.twitchapp.api.ApiConst
import com.example.twitchapp.api.ImageUri
import com.example.twitchapp.model.game.Game
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class GameDTO(
    val name: String?,
    @field:Json(name = "box_art_url") @ImageUri(ApiConst.GAME_IMAGE_HEIGHT,ApiConst.GAME_IMAGE_WIDTH) val imageUrl: Uri?
) {
    fun toModel() =
        Game(
            id = 0,
            name = name,
            imageUrl = imageUrl
        )
}