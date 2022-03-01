package com.example.twitchapp.data.datasource.local

import com.example.twitchapp.data.model.GameStreamDb
import javax.inject.Inject

class LocalDatasource @Inject constructor(
    private val gameStreamDao: GameStreamDao
) {

    fun getGameStreams() =
        gameStreamDao.getAll()

    suspend fun saveGameStreams(gameStreamsDb: List<GameStreamDb>) {
        gameStreamDao.insertAll(gameStreamsDb)
    }

}