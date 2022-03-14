package com.example.twitchapp.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.twitchapp.api.streams.TwitchGameStreamsApi
import com.example.twitchapp.api.util.NetworkConnectionChecker
import com.example.twitchapp.database.GAME_STREAMS_PAGE_SIZE
import com.example.twitchapp.database.streams.GameStreamEntity
import com.example.twitchapp.datasource.local.LocalDatasource
import com.example.twitchapp.model.AppNetworkMode
import com.example.twitchapp.model.DatabaseException
import com.example.twitchapp.model.DatabaseState
import com.example.twitchapp.model.NetworkState
import com.example.twitchapp.model.streams.GameStream
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameStreamsPagingSource @Inject constructor(
    private val twitchGameStreamsApi: TwitchGameStreamsApi,
    private val localDatasource: LocalDatasource,
    networkConnectionChecker: NetworkConnectionChecker
) : PagingSource<String, GameStream>() {

    private var appNetworkMode: AppNetworkMode = when (networkConnectionChecker.getNetworkState()) {
        NetworkState.AVAILABLE -> AppNetworkMode.ONLINE
        NetworkState.NOT_AVAILABLE -> AppNetworkMode.OFFLINE
    }

    private var isDatabaseShouldBeCleared: Boolean = true

    override suspend fun load(params: LoadParams<String>): LoadResult<String, GameStream> {
        try {
            var data = emptyList<GameStream>()
            var nextKey: String? = null

            when (appNetworkMode) {
                AppNetworkMode.ONLINE -> {
                    val nextPage = params.key ?: ""
                    val response = twitchGameStreamsApi.getGameStreams(nextPage)

                    if (isDatabaseShouldBeCleared && !response.data.isNullOrEmpty()) {
                        localDatasource.deleteAllGameStreams()
                        isDatabaseShouldBeCleared = false
                    }

                    data = response.data.map { it.toModel() }
                    localDatasource.saveGameStreams(data.map { GameStreamEntity.fromModel(it) })

                    nextKey = response.pagination.cursor
                }
                AppNetworkMode.OFFLINE -> {
                    if (params.key != null) {
                        val nextPage = params.key
                        val startId = localDatasource.getGameStreamByAccessKey(nextPage!!).id
                        val gameStreamEntities = localDatasource.getGameStreamsPage(
                            startId = startId,
                            endId = startId + GAME_STREAMS_PAGE_SIZE
                        )

                        if (gameStreamEntities.last().accessKey != nextPage) {
                            data = gameStreamEntities.map { it.toModel() }
                            nextKey = data.last().accessKey
                        }

                    } else {
                        val gameStreamEntities = localDatasource.getGameStreamsFirstPage()

                        if (gameStreamEntities.isEmpty()) throw DatabaseException(DatabaseState.EMPTY)
                        else {
                            data = gameStreamEntities.map { it.toModel() }
                            nextKey = data.last().accessKey
                        }
                    }
                }
            }
            return LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: UnknownHostException) {
            appNetworkMode = AppNetworkMode.OFFLINE
            return LoadResult.Error(e)
        } catch (e: DatabaseException) {
            return LoadResult.Error(e)
        } catch (e: Throwable) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, GameStream>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            val pageIndex = state.pages.indexOf(anchorPage)
            if (pageIndex == 0) {
                isDatabaseShouldBeCleared = true
                appNetworkMode = AppNetworkMode.ONLINE
                return null
            }
            anchorPage?.nextKey
        }
    }

}