package com.example.twitchapp.data.datasource

import com.example.twitchapp.data.network.TwitchApi
import javax.inject.Inject

class RemoteDatasource @Inject constructor(
    private val twitchApi: TwitchApi
) {

    suspend fun getGameStreams() =
        twitchApi.getGameStreams().data

}