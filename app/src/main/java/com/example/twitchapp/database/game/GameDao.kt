package com.example.twitchapp.database.game

import androidx.room.*
import com.example.twitchapp.database.BaseDao
import com.example.twitchapp.database.DbConstants
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@Dao
interface GameDao: BaseDao<GameEntity> {

    @Query("SELECT * FROM ${DbConstants.GAMES_TABLE_NAME} WHERE name=:name")
    fun getGame(name: String): Single<GameEntity>

    @Query("SELECT * FROM ${DbConstants.GAMES_TABLE_NAME} WHERE id=:id")
    fun getGameById(id: Long): GameEntity

    @Query("SELECT EXISTS(SELECT * FROM ${DbConstants.GAMES_TABLE_NAME} WHERE name = :name)")
    fun isGameExist(name: String): Boolean

    @Query("SELECT * FROM ${DbConstants.GAMES_TABLE_NAME} WHERE isFavourite=1")
    fun getFavoriteGames(): Observable<List<GameEntity>>

    @Update
    fun updateGame(game: GameEntity): Completable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWithIgnore(gameEntity: GameEntity)

    @Transaction
    fun saveAndGetGame(gameEntity: GameEntity): GameEntity {
        insertWithIgnore(gameEntity)
        return getGameById(gameEntity.id)
    }
}