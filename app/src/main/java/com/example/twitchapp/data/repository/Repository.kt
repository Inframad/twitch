package com.example.twitchapp.data.repository

import com.example.twitchapp.data.datasource.RemoteDatasource
import com.example.twitchapp.data.datasource.converter.toGameStreamDb
import com.example.twitchapp.data.datasource.local.LocalDatasource
import javax.inject.Inject

class Repository @Inject constructor(
    private val remoteDatasource: RemoteDatasource,
    private val localDatasource: LocalDatasource
) {

    fun getGameStreams() =
        localDatasource.getGameStreams()

    suspend fun updateGameStreams() {
        val gameStreamsDb = remoteDatasource.getGameStreams().map { it.toGameStreamDb() }
        localDatasource.saveGameStreams(gameStreamsDb)
    }

}