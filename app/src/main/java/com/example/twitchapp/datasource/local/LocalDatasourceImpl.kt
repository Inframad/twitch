package com.example.twitchapp.datasource.local

import com.example.twitchapp.common.dispatchers.IoDispatcher
import com.example.twitchapp.database.game.GameDao
import com.example.twitchapp.database.game.GameEntity
import com.example.twitchapp.database.streams.GameStreamDao
import com.example.twitchapp.database.streams.GameStreamEntity
import com.example.twitchapp.model.Result
import com.example.twitchapp.model.game.Game
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDatasourceImpl @Inject constructor(
    private val gameStreamDao: GameStreamDao,
    private val gameDao: GameDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : LocalDatasource {

    override suspend fun getGameStreamsPage(startId: Int, endId: Int): List<GameStreamEntity> {
        return withContext(ioDispatcher) {
            gameStreamDao.getPage(startId, endId)
        }
    }

    override suspend fun getGameStreamByAccessKey(accessKey: String): GameStreamEntity {
        return withContext(ioDispatcher) {
            gameStreamDao.getGameStreamByAccessKey(accessKey)
        }
    }

    override suspend fun saveGameStreams(gameStreams: List<GameStreamEntity>) {
        withContext(ioDispatcher) {
            gameStreamDao.replace(gameStreams)
        }
    }

    override suspend fun deleteAllGameStreams() {
        withContext(ioDispatcher) {
            gameStreamDao.clearAll()
        }
    }

    override suspend fun getGameStreamsFirstPage() =
        withContext(ioDispatcher) {
            gameStreamDao.getFirstPage()
        }

    override suspend fun getGame(name: String): Game =
        withContext(ioDispatcher) {
            gameDao.getGame(name).toModel()
        }


    override suspend fun isGameExist(name: String): Boolean =
        withContext(ioDispatcher) {
            gameDao.isGameExist(name)
        }

    override fun getFavouriteGames() =
        gameDao.getFavoriteGames().map { list ->
            Result.Success(
                list.map { gameEntity ->
                    gameEntity.toModel()
                }
            )
        }.flowOn(ioDispatcher)

    override suspend fun updateGame(game: Game) {
        withContext(ioDispatcher) {
            gameDao.updateGame(GameEntity.fromModel(game))
        }
    }

    override suspend fun saveAndGetGame(game: Game): Game =
        gameDao.saveAndGetGame(GameEntity.fromModel(game)).toModel()
}