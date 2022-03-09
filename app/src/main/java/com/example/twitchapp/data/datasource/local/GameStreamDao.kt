package com.example.twitchapp.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.twitchapp.data.GAME_STREAMS_PAGE_SIZE
import com.example.twitchapp.data.model.streams.GameStreamEntity

@Dao
interface GameStreamDao {

    @Query("SELECT * FROM gamestreamentity")
    suspend fun getAll(): List<GameStreamEntity>

    @Query("SELECT * FROM gamestreamentity WHERE accessKey=:accessKey")
    suspend fun getGameStreamByAccessKey(accessKey: String): GameStreamEntity

    @Query("SELECT * FROM gamestreamentity WHERE id BETWEEN :startId AND :endId")
    suspend fun getPage(startId: Int, endId: Int): List<GameStreamEntity>

    @Query("SELECT * FROM gamestreamentity LIMIT :pageSize")
    suspend fun getFirstPage(pageSize: Int = GAME_STREAMS_PAGE_SIZE): List<GameStreamEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gameStreamsEntity: GameStreamEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gameStreamsEntities: List<GameStreamEntity>)

    @Query("DELETE FROM gamestreamentity")
    suspend fun clearAll()
}