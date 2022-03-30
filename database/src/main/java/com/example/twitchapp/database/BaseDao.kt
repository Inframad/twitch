package com.example.twitchapp.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replace(item: T): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replace(item: List<T>): Completable

    @Insert
    fun insert(item: T): Single<Long>
}