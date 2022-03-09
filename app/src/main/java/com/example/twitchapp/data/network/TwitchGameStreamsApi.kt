package com.example.twitchapp.data.network

import com.example.twitchapp.data.model.response.StreamsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TwitchGameStreamsApi {

    @GET("streams/")
    suspend fun getGameStreams(@Query("after") cursor: String): StreamsResponse
}