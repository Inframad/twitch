package com.example.api.game

import com.example.api.game.GamesResponseDto
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface TwitchGamesApi {

    @GET("games/")
    fun getGames(@Query("name") name: String): Single<GamesResponseDto>
}