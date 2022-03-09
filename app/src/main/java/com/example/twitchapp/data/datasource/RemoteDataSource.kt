package com.example.twitchapp.data.datasource

import com.example.twitchapp.data.converter.toGame
import com.example.twitchapp.data.model.Result
import com.example.twitchapp.data.model.game.Game
import com.example.twitchapp.data.network.TwitchGamesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val gamesApi: TwitchGamesApi
) {

    suspend fun getGame(name: String): Result<Game> =
        withContext(Dispatchers.IO) {
            try {
                Result.Success(
                    gamesApi.getGames(name).games.first().toGame()
                )
            } catch (e: Throwable) {
                Result.Error(e)
            }
        }
}