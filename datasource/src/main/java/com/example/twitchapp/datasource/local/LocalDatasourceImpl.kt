package com.example.twitchapp.datasource.local

import androidx.room.rxjava3.EmptyResultSetException
import com.example.twitchapp.database.game.GameDao
import com.example.twitchapp.database.game.GameEntity
import com.example.twitchapp.database.streams.GameStreamDao
import com.example.twitchapp.database.streams.GameStreamEntity
import com.example.twitchapp.datasource.GAME_STREAMS_PAGE_SIZE
import com.example.twitchapp.model.exception.DatabaseException
import com.example.twitchapp.model.exception.DatabaseState
import com.example.twitchapp.model.game.Game
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDatasourceImpl @Inject constructor(
    private val gameStreamDao: GameStreamDao,
    private val gameDao: GameDao
) : LocalDatasource {

    override fun getGameStreamsPage(startId: Int, endId: Int): Single<List<GameStreamEntity>> =
        gameStreamDao.getPage(startId, endId)
            .subscribeOn(Schedulers.io())

    override fun getGameStreamByAccessKey(accessKey: String): Single<GameStreamEntity> =
        gameStreamDao.getGameStreamByAccessKey(accessKey)
            .subscribeOn(Schedulers.io())

    override fun saveGameStreams(gameStreams: List<GameStreamEntity>): Completable =
        gameStreamDao.replace(gameStreams)
            .subscribeOn(Schedulers.io())

    override fun deleteAllGameStreams(): Completable =
        gameStreamDao.clearAll()
            .subscribeOn(Schedulers.io())

    override fun getGameStreamsFirstPage(): Single<List<GameStreamEntity>> =
        gameStreamDao.getFirstPage(GAME_STREAMS_PAGE_SIZE)
            .subscribeOn(Schedulers.io())

    override fun getGame(name: String): Single<Game> =
        gameDao.getGame(name)
            .onErrorResumeNext {
                Single.error(
                    when (it) {
                        is EmptyResultSetException -> DatabaseException(DatabaseState.EMPTY)
                        else -> it
                    }
                )
            }
            .map { it.toModel() }

            .subscribeOn(Schedulers.io())

    override fun getFavouriteGames(): Observable<List<Game>> =
        gameDao.getFavoriteGames()
            .onErrorResumeNext {
                Observable.error(
                    when (it) {
                        is EmptyResultSetException -> DatabaseException(DatabaseState.EMPTY)
                        else -> it
                    }
                )
            }.map { list ->
                list.map { gameEntity ->
                    gameEntity.toModel()
                }
            }.subscribeOn(Schedulers.io())

    override fun updateGame(game: Game): Completable =
        gameDao.updateGame(GameEntity.fromModel(game))
            .subscribeOn(Schedulers.io())

    override fun saveAndGetGame(game: Game): Single<Game> {
        val gameEntity = GameEntity.fromModel(game)
        return gameDao.insertWithIgnore(gameEntity)
            .andThen(gameDao.getGameById(gameEntity.id))
            .map { it.toModel() }
            .subscribeOn(Schedulers.io())
    }
}
