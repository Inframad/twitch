package com.example.twitchapp.data.network

import com.example.twitchapp.data.model.StreamsResponse
import retrofit2.http.GET

interface TwitchApi {

    @GET("streams/")
    suspend fun getGameStreams(): StreamsResponse
}