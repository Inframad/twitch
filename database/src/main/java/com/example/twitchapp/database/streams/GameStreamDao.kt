package com.example.twitchapp.database.streams

import androidx.room.Dao
import androidx.room.Query
import com.example.twitchapp.database.BaseDao
import com.example.twitchapp.database.DbConstants
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface GameStreamDao: BaseDao<GameStreamEntity> {

    @Query("SELECT * FROM ${DbConstants.STREAMS_TABLE_NAME} WHERE accessKey=:accessKey")
    fun getGameStreamByAccessKey(accessKey: String): Single<GameStreamEntity>

    @Query("SELECT * FROM ${DbConstants.STREAMS_TABLE_NAME} WHERE id BETWEEN :startId AND :endId")
    fun getPage(startId: Int, endId: Int): Single<List<GameStreamEntity>>

    @Query("SELECT * FROM ${DbConstants.STREAMS_TABLE_NAME} LIMIT :pageSize")
    fun getFirstPage(pageSize: Int): Single<List<GameStreamEntity>>

    @Query("DELETE FROM ${DbConstants.STREAMS_TABLE_NAME}")
    fun clearAll(): Completable
}