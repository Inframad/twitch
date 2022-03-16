package com.example.twitchapp.database.streams

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.twitchapp.model.streams.GameStream

@Entity
class GameStreamEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val accessKey: String,
    val userName: String?,
    val gameName: String?,
    val viewerCount: Long?,
    val imageUrl: Uri?
) {
    fun toModel() =
        GameStream(
            accessKey = accessKey,
            userName = userName,
            gameName = gameName,
            viewerCount = viewerCount,
            imageUrl = imageUrl
        )

    companion object {
        fun fromModel(gameStream: GameStream) =
            GameStreamEntity(
                accessKey = gameStream.accessKey,
                userName = gameStream.userName,
                gameName = gameStream.gameName,
                viewerCount = gameStream.viewerCount,
                imageUrl = gameStream.imageUrl
            )
    }
}