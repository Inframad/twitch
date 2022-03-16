package com.example.twitchapp.database.game

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.twitchapp.model.game.Game

@Entity
class GameEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String?,
    val imageUrl: Uri?,
    val isFavourite: Boolean = false
) {
    fun toModel() =
        Game(
            id = id,
            name = name,
            isFavourite = isFavourite,
            imageUrl = imageUrl
        )

    companion object {
        fun fromModel(game: Game) =
            GameEntity(
                id = game.id,
                name = game.name,
                isFavourite = game.isFavourite,
                imageUrl = game.imageUrl
            )
    }

}
