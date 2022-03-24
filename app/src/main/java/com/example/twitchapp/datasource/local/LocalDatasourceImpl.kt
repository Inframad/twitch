package com.example.twitchapp.datasource.local

import com.example.twitchapp.common.dispatchers.IoDispatcher
import com.example.twitchapp.database.game.GameDao
import com.example.twitchapp.database.game.GameEntity
import com.example.twitchapp.database.streams.GameStreamDao
import com.example.twitchapp.database.streams.GameStreamEntity
import com.example.twitchapp.model.Result
import com.example.twitchapp.model.game.Game
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineDispatcher
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

    override fun getGame(name: String): Single<Game> =
        gameDao.getGame(name)
            .map { it.toModel() }
            .subscribeOn(Schedulers.io())

    override fun isGameExist(name: String): Boolean =
            gameDao.isGameExist(name)

    override fun getFavouriteGames(): Observable<Result.Success<List<Game>>> =
        gameDao.getFavoriteGames()
            .map { list ->
                Result.Success(
                    list.map { gameEntity ->
                        gameEntity.toModel()
                    }
                )
            }.subscribeOn(Schedulers.io())

    override fun updateGame(game: Game): Completable =
        gameDao.updateGame(GameEntity.fromModel(game))
            .subscribeOn(Schedulers.io())

    override fun saveAndGetGame(game: Game): Single<Game> =
        Single.just(gameDao.saveAndGetGame(GameEntity.fromModel(game)))
            .map {
                it.toModel()
            }.subscribeOn(Schedulers.io())
}
