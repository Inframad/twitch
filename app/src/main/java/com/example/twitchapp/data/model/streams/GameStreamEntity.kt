package com.example.twitchapp.data.model.streams

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameStreamEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val accessKey: String,
    val userName: String,
    val gameName: String,
    val viewerCount: Long,
    val imageUrl: String?
)