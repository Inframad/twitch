package com.example.streams

import android.content.Context
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.example.api.streams.TwitchGameStreamsApi
import com.example.api.util.NetworkConnectionChecker
import com.example.twitchapp.database.streams.GameStreamEntity
import com.example.twitchapp.datasource.GAME_STREAMS_PAGE_SIZE
import com.example.twitchapp.datasource.R
import com.example.twitchapp.datasource.local.LocalDatasource
import com.example.twitchapp.model.AppNetworkMode
import com.example.twitchapp.model.NetworkState
import com.example.twitchapp.model.exception.DatabaseException
import com.example.twitchapp.model.exception.DatabaseState
import com.example.twitchapp.model.streams.GameStream
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GameStreamsPagingSource @Inject constructor(
    private val twitchGameStreamsApi: TwitchGameStreamsApi,
    private val localDatasource: LocalDatasource,
    @ApplicationContext private val applicationContext: Context,
    networkConnectionChecker: NetworkConnectionChecker
) : RxPagingSource<String, GameStream>() {

    private var isDatabaseShouldBeCleared: Boolean = true
    private var appNetworkMode: AppNetworkMode = when(networkConnectionChecker.getNetworkState()) {
        NetworkState.AVAILABLE -> AppNetworkMode.ONLINE
        NetworkState.NOT_AVAILABLE -> AppNetworkMode.OFFLINE
    }

    override fun loadSingle(params: LoadParams<String>): Single<LoadResult<String, GameStream>> {
        val loadResult = when (appNetworkMode) {
            AppNetworkMode.ONLINE -> twitchGameStreamsApi.getGameStreams(params.key ?: "")
                .flatMap {
                    if (isDatabaseShouldBeCleared && !it.data.isNullOrEmpty()) {
                        isDatabaseShouldBeCleared = false
                        localDatasource.deleteAllGameStreams().andThen(Single.just(it))
                    } else Single.just(it)
                }.map {
                    it.data.map { it.toModel() }
                        .map { GameStreamEntity.fromModel(it) } to it.pagination.cursor
                }
                .flatMap { (entities, cursor) ->
                    localDatasource.saveGameStreams(entities).andThen(makePage(entities, cursor))
                }
            AppNetworkMode.OFFLINE -> if (params.key != null) {
                localDatasource.getGameStreamByAccessKey(params.key!!)
                    .flatMap {
                        localDatasource.getGameStreamsPage(
                            startId = it.id,
                            endId = it.id + GAME_STREAMS_PAGE_SIZE
                        )
                    }.flatMap {
                        if (it.last().accessKey != params.key) {
                            makePage(it)
                        } else {
                            @Suppress("Unchecked_Cast")
                            Single.just(
                                LoadResult.Page(
                                    data = emptyList<GameStream>(),
                                    prevKey = null,
                                    nextKey = null
                                ) as LoadResult<String, GameStream>
                            )
                        }
                    }
            } else {
                localDatasource.getGameStreamsFirstPage()
                    .flatMap {
                        if(it.isEmpty()) throw DatabaseException(DatabaseState.EMPTY)
                        else makePage(it)
                    }
            }
        }
        return loadResult.onErrorResumeNext {
           Single.just(LoadResult.Error(it))
        }
    }

    private fun makePage(list: List<GameStreamEntity>, nextKey: String? = list.last().accessKey): Single<LoadResult<String, GameStream>> =
        Single.just(LoadResult.Page(
            data = list.map { it.toModel() }.map { gameStream ->
                gameStream.copy(
                    userName = gameStream.userName
                        ?: applicationContext.getString(R.string.scr_any_lbl_unknown),
                    gameName = gameStream.gameName
                        ?: applicationContext.getString(R.string.scr_any_lbl_unknown),
                    viewerCount = gameStream.viewerCount ?: 0L
                )
            },
            prevKey = null,
            nextKey = nextKey
        )
        )

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