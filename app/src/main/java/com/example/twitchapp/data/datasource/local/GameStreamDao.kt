package com.example.twitchapp.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.twitchapp.data.model.GameStream

@Dao
interface GameStreamDao {

    @Query("SELECT * FROM gamestream")
    fun getAll(): List<GameStream>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(gameStreams: List<GameStream>)

    @Query("DELETE FROM gamestream")
    suspend fun clearAll()
}