package com.example.twitchapp.data.datasource.local

import com.example.twitchapp.data.model.GameStreamEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalDatasource @Inject constructor(
    private val gameStreamDao: GameStreamDao
) {

    fun getGameStreams() =
        gameStreamDao.getAll()

    suspend fun saveGameStream(gameStream: GameStreamEntity) {
        gameStreamDao.insert(gameStream)
    }

    suspend fun getGameStreamsPage(id: Int): List<GameStreamEntity> {
        return withContext(Dispatchers.IO) {
            gameStreamDao.getPage(id)
        }
    }


    suspend fun getGameStreamByGUID(guid: String): GameStreamEntity {
        return withContext(Dispatchers.IO) {
            gameStreamDao.getGameStreamByGUID(guid)
        }
    }


    suspend fun saveGameStreams(gameStreams: List<GameStreamEntity>) {
        gameStreamDao.insert(gameStreams)
    }

}