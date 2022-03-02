package com.example.twitchapp.data.network

import com.example.twitchapp.data.model.StreamsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TwitchApi {

    @GET("streams/")
    suspend fun getGameStreams(@Query("after") cursor: String): StreamsResponse
}