package com.example.twitchapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.rxjava3.EmptyResultSetException
import com.example.twitchapp.api.util.NetworkConnectionChecker
import com.example.twitchapp.database.GAME_STREAMS_PAGE_SIZE
import com.example.twitchapp.datasource.GameStreamsPagingSourceFactory
import com.example.twitchapp.datasource.local.LocalDatasource
import com.example.twitchapp.datasource.remote.RemoteDatasource
import com.example.twitchapp.model.DatabaseException
import com.example.twitchapp.model.DatabaseState
import com.example.twitchapp.model.NetworkState
import com.example.twitchapp.model.exception.NoInternetConnectionException
import com.example.twitchapp.model.game.Game
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    private val localDatasource: LocalDatasource,
    private val remoteDatasource: RemoteDatasource,
    private val networkConnectionChecker: NetworkConnectionChecker,
    private val pagingSourceFactory: GameStreamsPagingSourceFactory
) : Repository {

    override fun getGameStreamsFlow() = Pager(
        PagingConfig(pageSize = GAME_STREAMS_PAGE_SIZE)
    ) {
        pagingSourceFactory.create()
    }.flow

    override fun getCurrentNetworkState(): NetworkState =
        networkConnectionChecker.getNetworkState()

    override fun getGame(name: String): Single<Game> =
        remoteDatasource.getGame(name)
            .onErrorResumeNext {
                when(it) {
                    is NoInternetConnectionException ->
                        return@onErrorResumeNext localDatasource.getGame(name)
                    else -> throw it
                }
            }.onErrorResumeNext {
                Single.error(
                    when(it) {
                        is EmptyResultSetException -> DatabaseException(DatabaseState.EMPTY)
                        else -> it
                    }
                )
            }
            .doOnSuccess {
                localDatasource.saveAndGetGame(it)
            }

    override fun getFavouriteGames() =
        localDatasource.getFavouriteGames()

    override fun updateGame(game: Game) =
        localDatasource.updateGame(game)
}

