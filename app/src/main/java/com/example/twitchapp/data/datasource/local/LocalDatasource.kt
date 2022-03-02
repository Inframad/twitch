package com.example.twitchapp.data.datasource.local

import com.example.twitchapp.data.model.GameStream
import javax.inject.Inject

class LocalDatasource @Inject constructor(
    private val gameStreamDao: GameStreamDao
) {

    fun getGameStreams() =
        gameStreamDao.getAll()

    suspend fun saveGameStream(gameStream: GameStream) {
        gameStreamDao.insert(gameStream)
    }

}