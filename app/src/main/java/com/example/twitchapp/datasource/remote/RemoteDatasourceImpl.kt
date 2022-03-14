package com.example.twitchapp.datasource.remote

import com.example.twitchapp.api.game.TwitchGamesApi
import com.example.twitchapp.datasource.di.IoDispatcher
import com.example.twitchapp.model.Result
import com.example.twitchapp.model.game.Game
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDatasourceImpl @Inject constructor(
    private val gamesApi: TwitchGamesApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): RemoteDatasource {

    override suspend fun getGame(name: String): Result<Game> =
        withContext(ioDispatcher) {
            try {
                Result.Success(
                    gamesApi.getGames(name).games.first().toModel()
                )
            } catch (e: Throwable) {
                Result.Error(e)
            }
        }
}