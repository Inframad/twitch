package com.example.twitchapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.twitchapp.api.streams.TwitchGameStreamsApi
import com.example.twitchapp.api.util.NetworkConnectionChecker
import com.example.twitchapp.database.GAME_STREAMS_PAGE_SIZE
import com.example.twitchapp.datasource.GameStreamsPagingSource
import com.example.twitchapp.datasource.local.LocalDatasource
import com.example.twitchapp.datasource.remote.RemoteDatasource
import com.example.twitchapp.model.NetworkState
import com.example.twitchapp.model.Result
import com.example.twitchapp.model.game.Game
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    private val localDatasource: LocalDatasource,
    private val remoteDatasource: RemoteDatasource,
    private val networkConnectionChecker: NetworkConnectionChecker,
    private val api: TwitchGameStreamsApi
) : Repository {

    override fun getGameStreamsFlow() = Pager(
        PagingConfig(pageSize = GAME_STREAMS_PAGE_SIZE)
    ) {
        GameStreamsPagingSource(api, localDatasource, networkConnectionChecker)
    }.flow

    override fun getCurrentNetworkState(): NetworkState =
        networkConnectionChecker.getNetworkState()

    override suspend fun getGame(name: String): Result<Game> {
        return when (getCurrentNetworkState()) {
            NetworkState.AVAILABLE -> {
                return when (val result = remoteDatasource.getGame(name)) {
                    is Result.Success ->
                        Result.Success(localDatasource.saveAndGetGame(result.data))
                    else -> result
                }
            }
            NetworkState.NOT_AVAILABLE -> {
                if (localDatasource.isGameExist(name)) {
                    Result.Success(localDatasource.getGame(name))
                } else {
                    Result.Empty
                }
            }
        }
    }

    override fun getFavouriteGames() =
        localDatasource.getFavouriteGames()

    override suspend fun updateGame(game: Game) =
        localDatasource.updateGame(game)
}

