package com.example.twitchapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.twitchapp.data.datasource.GameStreamsPagingSource
import com.example.twitchapp.data.datasource.local.LocalDatasource
import com.example.twitchapp.data.model.NetworkState
import com.example.twitchapp.data.network.NetworkConnectionChecker
import com.example.twitchapp.data.network.TwitchApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val localDatasource: LocalDatasource,
    private val networkConnectionChecker: NetworkConnectionChecker,
    private val api: TwitchApi
) {

    val gameStreamsFlow = Pager(
        PagingConfig(pageSize = 20)
    ) {
        GameStreamsPagingSource(api, localDatasource, networkConnectionChecker)
    }.flow

    fun getCurrentNetworkState(): NetworkState =
        networkConnectionChecker.getNetworkState()
}