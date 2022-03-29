package com.example.twitchapp.model.game

import android.net.Uri

data class Game(
    val id: Long,
    val name: String?,
    val imageUrl: Uri?,
    val isFavourite: Boolean = false
)
