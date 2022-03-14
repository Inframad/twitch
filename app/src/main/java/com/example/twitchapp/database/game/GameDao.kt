package com.example.twitchapp.database.game

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.twitchapp.database.DbConstants
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Query("SELECT * FROM ${DbConstants.GAMES_TABLE_NAME} WHERE name=:name")
    suspend fun getGame(name: String): GameEntity

    @Insert(onConflict = REPLACE)
    suspend fun saveGame(game: GameEntity)

    @Query("SELECT EXISTS(SELECT * FROM ${DbConstants.GAMES_TABLE_NAME} WHERE name = :name)")
    suspend fun isGameExist(name: String): Boolean

    @Query("SELECT * FROM ${DbConstants.GAMES_TABLE_NAME} WHERE isFavourite=1")
    fun getFavoriteGames(): Flow<List<GameEntity>>

    @Update
    suspend fun updateGame(game: GameEntity)
}