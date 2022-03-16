package com.example.twitchapp.database.game

import androidx.room.*
import com.example.twitchapp.database.BaseDao
import com.example.twitchapp.database.DbConstants
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao: BaseDao<GameEntity> {

    @Query("SELECT * FROM ${DbConstants.GAMES_TABLE_NAME} WHERE name=:name")
    suspend fun getGame(name: String): GameEntity

    @Query("SELECT * FROM ${DbConstants.GAMES_TABLE_NAME} WHERE id=:id")
    suspend fun getGameById(id: Long): GameEntity

    @Query("SELECT EXISTS(SELECT * FROM ${DbConstants.GAMES_TABLE_NAME} WHERE name = :name)")
    suspend fun isGameExist(name: String): Boolean

    @Query("SELECT * FROM ${DbConstants.GAMES_TABLE_NAME} WHERE isFavourite=1")
    fun getFavoriteGames(): Flow<List<GameEntity>>

    @Update
    suspend fun updateGame(game: GameEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(gameEntity: GameEntity)

    @Transaction
    suspend fun saveAndGetGame(gameEntity: GameEntity): GameEntity {
        insert(gameEntity)
        return getGameById(gameEntity.id)
    }
}