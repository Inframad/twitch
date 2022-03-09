package com.example.twitchapp.data.datasource.local

import com.example.twitchapp.data.converter.toGame
import com.example.twitchapp.data.converter.toGameEntity
import com.example.twitchapp.data.model.game.Game
import com.example.twitchapp.data.model.streams.GameStreamEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalDatasource @Inject constructor(
    private val gameStreamDao: GameStreamDao,
    private val gameDao: GameDao
) {

    suspend fun getGameStreamsPage(startId: Int, endId: Int): List<GameStreamEntity> {
        return withContext(Dispatchers.IO) { //TODO Inject dispatchers to constructor
            gameStreamDao.getPage(startId, endId)
        }
    }

    suspend fun getGameStreamByGUID(guid: String): GameStreamEntity {
        return withContext(Dispatchers.IO) {
            gameStreamDao.getGameStreamByGUID(guid)
        }
    }

    suspend fun saveGameStreams(gameStreams: List<GameStreamEntity>) {
        withContext(Dispatchers.IO) {
            gameStreamDao.insert(gameStreams)
        }
    }

    suspend fun deleteAllGameStreams(){
        withContext(Dispatchers.IO) {
            gameStreamDao.clearAll()
        }
    }

    suspend fun getGameStreamsFirstPage() =
        withContext(Dispatchers.IO) {
            gameStreamDao.getFirstPage()
        }

    suspend fun getGame(name: String): Game =
        withContext(Dispatchers.IO) {
            gameDao.getGame(name).toGame()
        }

    suspend fun saveGame(game: Game) =
        withContext(Dispatchers.IO) {
            gameDao.saveGame(game.toGameEntity())
        }

    suspend fun isGameExist(name: String): Boolean =
        withContext(Dispatchers.IO) {
            gameDao.isGameExist(name)
        }

}