package com.example.twitchapp.data.datasource.local

import com.example.twitchapp.data.converter.toGame
import com.example.twitchapp.data.converter.toGameEntity
import com.example.twitchapp.data.model.Result
import com.example.twitchapp.data.model.game.Game
import com.example.twitchapp.data.model.streams.GameStreamEntity
import com.example.twitchapp.di.data.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalDatasource @Inject constructor(
    private val gameStreamDao: GameStreamDao,
    private val gameDao: GameDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getGameStreamsPage(startId: Int, endId: Int): List<GameStreamEntity> {
        return withContext(ioDispatcher) {
            gameStreamDao.getPage(startId, endId)
        }
    }

    suspend fun getGameStreamByAccessKey(accessKey: String): GameStreamEntity {
        return withContext(ioDispatcher) {
            gameStreamDao.getGameStreamByAccessKey(accessKey)
        }
    }

    suspend fun saveGameStreams(gameStreams: List<GameStreamEntity>) {
        withContext(ioDispatcher) {
            gameStreamDao.insert(gameStreams)
        }
    }

    suspend fun deleteAllGameStreams(){
        withContext(ioDispatcher) {
            gameStreamDao.clearAll()
        }
    }

    suspend fun getGameStreamsFirstPage() =
        withContext(ioDispatcher) {
            gameStreamDao.getFirstPage()
        }

    suspend fun getGame(name: String): Game =
        withContext(ioDispatcher) {
            gameDao.getGame(name).toGame()
        }

    suspend fun saveGame(game: Game) =
        withContext(ioDispatcher) {
            gameDao.saveGame(game.toGameEntity())
        }

    suspend fun isGameExist(name: String): Boolean =
        withContext(ioDispatcher) {
            gameDao.isGameExist(name)
        }

    suspend fun getFavouriteGames() =
        withContext(ioDispatcher) {
            Result.Success(gameDao.getFavoriteGames().map { it.toGame() })
        }

    suspend fun updateGame(game: Game) {
        withContext(ioDispatcher) {
            gameDao.updateGame(game)
        }
    }
}