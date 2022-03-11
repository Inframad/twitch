package com.example.twitchapp.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.twitchapp.data.model.game.Game
import com.example.twitchapp.data.model.game.GameEntity

@Dao
interface GameDao {

    @Query("SELECT * FROM gameentity WHERE name=:name")
    suspend fun getGame(name: String): GameEntity

    @Insert(onConflict = REPLACE)
    suspend fun saveGame(game: GameEntity)

    @Query("SELECT EXISTS(SELECT * FROM gameentity WHERE name = :name)")
    suspend fun isGameExist(name: String): Boolean

    @Query("SELECT * FROM gameentity WHERE isFavourite=1")
    suspend fun getFavoriteGames(): List<GameEntity>

    @Update
    suspend fun updateGame(game: Game)
}