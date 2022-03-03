package com.example.twitchapp.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.twitchapp.data.converter.toGameStream
import com.example.twitchapp.data.converter.toGameStreamEntity
import com.example.twitchapp.data.datasource.local.LocalDatasource
import com.example.twitchapp.data.model.*
import com.example.twitchapp.data.network.NetworkConnectionChecker
import com.example.twitchapp.data.network.TwitchApi
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameStreamsPagingSource @Inject constructor(
    private val twitchApi: TwitchApi,
    private val localDatasource: LocalDatasource,
    networkConnectionChecker: NetworkConnectionChecker
) : PagingSource<String, GameStream>() {

    private var mode: Mode = when (networkConnectionChecker.getNetworkState()) {
        NetworkState.AVAILABLE -> Mode.ONLINE
        NetworkState.NOT_AVAILABLE -> Mode.OFFLINE
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, GameStream> {
        try {
            var data = emptyList<GameStream>()
            var nextKey: String? = null

            when (mode) {
                Mode.ONLINE -> {
                    val nextPage = params.key ?: ""
                    val response = twitchApi.getGameStreams(nextPage)

                    data = response.data.map { it.toGameStream() }
                    localDatasource.saveGameStreams(data.map { it.toGameStreamEntity() })

                    nextKey = response.pagination.cursor
                }
                Mode.OFFLINE -> {
                    if (params.key != null) {
                        val nextPage = params.key
                        val gameStreamEntities = localDatasource.getGameStreamsPage(
                            localDatasource.getGameStreamByGUID(nextPage!!).id
                        )

                        if (gameStreamEntities.last().GUID != nextPage) {
                            data = gameStreamEntities.map { it.toGameStream() }
                            nextKey = data.last().GUID
                        }

                    } else {
                        val gameStreamEntities = localDatasource.getGameStreamsPage(1)

                        if (gameStreamEntities.isEmpty()) throw DatabaseException(DatabaseState.EMPTY)
                        else {
                            data = gameStreamEntities.map { it.toGameStream() }
                            nextKey = data.last().GUID
                        }
                    }
                }
            }
            return LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        } catch (e: DatabaseException) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, GameStream>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            val pageIndex = state.pages.indexOf(anchorPage)
            if (pageIndex == 0) return null
            anchorPage?.nextKey
        }
    }

}