package com.example.game

import android.net.Uri

data class GameScreenModel(
    val name: String,
    val streamerName: String,
    val viewersCount: String,
    val imageUrl: Uri?,
)
