package com.example.twitchapp.api.streams

import retrofit2.http.GET
import retrofit2.http.Query

interface TwitchGameStreamsApi {

    @GET("streams/")
    suspend fun getGameStreams(@Query("after") cursor: String): StreamsResponse
}