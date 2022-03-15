package com.example.twitchapp.database.game

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
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

    @Transaction
    suspend fun saveAndGetGame(gameEntity: GameEntity): GameEntity {
        saveGame(gameEntity)
        return getGame(gameEntity.name)
    }
}