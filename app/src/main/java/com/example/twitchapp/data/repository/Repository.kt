package com.example.twitchapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.example.twitchapp.data.converter.toGameStream
import com.example.twitchapp.data.datasource.GameStreamsPagingSource
import com.example.twitchapp.data.datasource.local.LocalDatasource
import com.example.twitchapp.data.network.TwitchApi
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class Repository @Inject constructor(
    private val localDatasource: LocalDatasource,
    private val api: TwitchApi
) {

    val gameStreamsFlow = Pager(
        PagingConfig(pageSize = 20)
    ) {
        GameStreamsPagingSource(api)
    }.flow.map {
        it.map { gameStreamDTO ->
            val gameStream = gameStreamDTO.toGameStream()
            localDatasource.saveGameStream(gameStream)
            gameStream
        }
    }

}