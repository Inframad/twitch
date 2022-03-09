package com.example.twitchapp.data.network

import com.example.twitchapp.data.model.game.GamesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TwitchGamesApi {

    @GET("games/")
    suspend fun getGames(@Query("name") name: String): GamesResponse
}