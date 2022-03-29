package com.example.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.api.NetworkException
import com.example.api.util.NetworkConnectionChecker
import com.example.twitchapp.datasource.GAME_STREAMS_PAGE_SIZE
import com.example.twitchapp.datasource.GameStreamsPagingSourceFactory
import com.example.twitchapp.datasource.local.LocalDatasource
import com.example.twitchapp.datasource.remote.RemoteDatasource
import com.example.twitchapp.model.NetworkState
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

    override fun getGameStreamsLiveData() = Pager(
        PagingConfig(pageSize = GAME_STREAMS_PAGE_SIZE)
    ) {
        pagingSourceFactory.create()
    }

    override fun getCurrentNetworkState(): NetworkState =
        networkConnectionChecker.getNetworkState()

    override fun getGame(name: String): Single<Game> =
        remoteDatasource.getGame(name)
            .flatMap {
                localDatasource.saveAndGetGame(it)
            }
            .onErrorResumeNext {
                when (it) {
                    is NetworkException.NetworkIsNotAvailable ->
                        localDatasource.getGame(name)
                    else -> throw it
                }
            }

    override fun getFavouriteGames() =
        localDatasource.getFavouriteGames()

    override fun updateGame(game: Game) =
        localDatasource.updateGame(game)
}

