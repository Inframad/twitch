package com.example.twitchapp.database.streams

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.twitchapp.database.DbConstants
import com.example.twitchapp.database.GAME_STREAMS_PAGE_SIZE

@Dao
interface GameStreamDao {

    @Query("SELECT * FROM ${DbConstants.STREAMS_TABLE_NAME} WHERE accessKey=:accessKey")
    suspend fun getGameStreamByAccessKey(accessKey: String): GameStreamEntity

    @Query("SELECT * FROM ${DbConstants.STREAMS_TABLE_NAME} WHERE id BETWEEN :startId AND :endId")
    suspend fun getPage(startId: Int, endId: Int): List<GameStreamEntity>

    @Query("SELECT * FROM ${DbConstants.STREAMS_TABLE_NAME} LIMIT :pageSize")
    suspend fun getFirstPage(pageSize: Int = GAME_STREAMS_PAGE_SIZE): List<GameStreamEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gameStreamsEntity: GameStreamEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gameStreamsEntities: List<GameStreamEntity>)

    @Query("DELETE FROM ${DbConstants.STREAMS_TABLE_NAME}")
    suspend fun clearAll()
}