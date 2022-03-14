package com.example.twitchapp.datasource

import com.example.twitchapp.database.game.GameDao
import com.example.twitchapp.database.game.GameEntity
import com.example.twitchapp.database.streams.GameStreamDao
import com.example.twitchapp.database.streams.GameStreamEntity
import com.example.twitchapp.datasource.di.IoDispatcher
import com.example.twitchapp.model.Result
import com.example.twitchapp.model.game.Game
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
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

    suspend fun deleteAllGameStreams() {
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
            gameDao.getGame(name).toModel()
        }

    suspend fun saveGame(game: Game) =
        withContext(ioDispatcher) {
            gameDao.saveGame(GameEntity.fromModel(game))
        }

    suspend fun isGameExist(name: String): Boolean =
        withContext(ioDispatcher) {
            gameDao.isGameExist(name)
        }

    fun getFavouriteGames() =
        gameDao.getFavoriteGames().map { list ->
            Result.Success(
                list.map { gameEntity ->
                    gameEntity.toModel()
                }
            )
        }.flowOn(ioDispatcher)

    suspend fun updateGame(game: Game) {
        withContext(ioDispatcher) {
            gameDao.updateGame(GameEntity.fromModel(game))
        }
    }
}