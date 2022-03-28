package com.example.twitchapp.api.streams

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface TwitchGameStreamsApi {

    @GET("streams/")
    fun getGameStreams(@Query("after") cursor: String): Single<StreamsResponseDto>
}