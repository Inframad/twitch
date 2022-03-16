package com.example.twitchapp.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replace(item: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replace(item: List<T>)
}