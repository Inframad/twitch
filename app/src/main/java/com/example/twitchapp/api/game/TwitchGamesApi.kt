package com.example.twitchapp.api.game

import retrofit2.http.GET
import retrofit2.http.Query

interface TwitchGamesApi {

    @GET("games/")
    suspend fun getGames(@Query("name") name: String): GamesResponseDto
}