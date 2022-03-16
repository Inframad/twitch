package com.example.twitchapp.repository

import androidx.paging.PagingData
import com.example.twitchapp.model.NetworkState
import com.example.twitchapp.model.Result
import com.example.twitchapp.model.game.Game
import com.example.twitchapp.model.streams.GameStream
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getGameStreamsFlow(): Flow<PagingData<GameStream>>
    fun getCurrentNetworkState(): NetworkState
    suspend fun getGame(name: String?): Result<Game>
    fun getFavouriteGames(): Flow<Result.Success<List<Game>>>
    suspend fun updateGame(game: Game)
}