package com.example.twitchapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.twitchapp.data.GAME_STREAMS_PAGE_SIZE
import com.example.twitchapp.data.datasource.GameStreamsPagingSource
import com.example.twitchapp.data.datasource.RemoteDataSource
import com.example.twitchapp.data.datasource.local.LocalDatasource
import com.example.twitchapp.data.model.NetworkState
import com.example.twitchapp.data.model.Result
import com.example.twitchapp.data.model.game.Game
import com.example.twitchapp.data.network.NetworkConnectionChecker
import com.example.twitchapp.data.network.TwitchGameStreamsApi
import javax.inject.Inject

class Repository @Inject constructor(
    private val localDatasource: LocalDatasource,
    private val remoteDataSource: RemoteDataSource,
    private val networkConnectionChecker: NetworkConnectionChecker,
    private val api: TwitchGameStreamsApi
) {

    val gameStreamsFlow = Pager(
        PagingConfig(pageSize = GAME_STREAMS_PAGE_SIZE)
    ) {
        GameStreamsPagingSource(api, localDatasource, networkConnectionChecker)
    }.flow

    fun getCurrentNetworkState(): NetworkState =
        networkConnectionChecker.getNetworkState()

    suspend fun getGame(name: String): Result<Game> {
        return when (getCurrentNetworkState()) {
            NetworkState.AVAILABLE -> {
                when (val result = remoteDataSource.getGame(name)) {
                    is Result.Success -> localDatasource.saveGame(result.data)
                    else -> return result
                }
                Result.Success(localDatasource.getGame(name))
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

    fun getFavouriteGames() =
        localDatasource.getFavouriteGames()

    suspend fun updateGame(game: Game) =
        localDatasource.updateGame(game)
}

