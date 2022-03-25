package com.example.twitchapp.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import io.reactivex.rxjava3.core.Single

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replace(item: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replace(item: List<T>)

    @Insert
    fun insert(item: T): Single<Long>
}