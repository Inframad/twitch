package com.example.twitchapp.datasource

import android.content.Context
import com.example.twitchapp.api.streams.TwitchGameStreamsApi
import com.example.twitchapp.api.util.NetworkConnectionChecker
import com.example.twitchapp.datasource.local.LocalDatasource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameStreamsPagingSourceFactory @Inject constructor(
    private val api: TwitchGameStreamsApi,
    private val localDatasource: LocalDatasource,
    @ApplicationContext private val applicationContext: Context,
    private val networkConnectionChecker: NetworkConnectionChecker) {

    fun create() =
        GameStreamsPagingSource(api, localDatasource, applicationContext, networkConnectionChecker)
}