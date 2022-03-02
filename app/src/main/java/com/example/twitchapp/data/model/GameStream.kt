package com.example.twitchapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameStream(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val gameName: String,
    val viewerCount: Long,
    val imageUrl: String?
)