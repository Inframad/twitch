package com.example.twitchapp.data.model.game

data class Game(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    var isFavourite: Boolean = false
)
