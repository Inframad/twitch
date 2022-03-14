package com.example.twitchapp.model.game

data class Game(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val isFavourite: Boolean = false
)
