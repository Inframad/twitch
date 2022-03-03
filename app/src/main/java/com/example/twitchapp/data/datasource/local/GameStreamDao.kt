package com.example.twitchapp.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.twitchapp.data.model.GameStreamEntity

@Dao
interface GameStreamDao {

    @Query("SELECT * FROM gamestreamentity")
    fun getAll(): List<GameStreamEntity>

    @Query("SELECT * FROM gamestreamentity WHERE GUID=:guid")
    fun getGameStreamByGUID(guid: String): GameStreamEntity

    @Query("SELECT * FROM gamestreamentity WHERE id BETWEEN :id AND :id+20")
    fun getPage(id: Int): List<GameStreamEntity>

    @Query("SELECT * FROM gamestreamentity LIMIT 20")
    fun getFirstPage(): List<GameStreamEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gameStreamsEntity: GameStreamEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gameStreamsEntities: List<GameStreamEntity>)

    @Query("DELETE FROM gamestreamentity")
    suspend fun clearAll()
}