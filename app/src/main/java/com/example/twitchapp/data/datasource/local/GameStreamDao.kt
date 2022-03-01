package com.example.twitchapp.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.twitchapp.data.model.GameStreamDb
import kotlinx.coroutines.flow.Flow

@Dao
interface GameStreamDao {

    @Query("SELECT * FROM gamestreamdb")
    fun getAll(): Flow<List<GameStreamDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(gameStreamsDb: List<GameStreamDb>)
}