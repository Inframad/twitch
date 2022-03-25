package com.example.twitchapp.datasource

import android.content.Context
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.example.twitchapp.R
import com.example.twitchapp.api.streams.TwitchGameStreamsApi
import com.example.twitchapp.api.util.NetworkConnectionChecker
import com.example.twitchapp.database.GAME_STREAMS_PAGE_SIZE
import com.example.twitchapp.database.streams.GameStreamEntity
import com.example.twitchapp.datasource.local.LocalDatasource
import com.example.twitchapp.model.NetworkState
import com.example.twitchapp.model.exception.HttpException
import com.example.twitchapp.model.streams.GameStream
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.Single
import java.net.UnknownHostException
import javax.inject.Inject

class GameStreamsPagingSource @Inject constructor(
    private val twitchGameStreamsApi: TwitchGameStreamsApi,
    private val localDatasource: LocalDatasource,
    @ApplicationContext private val applicationContext: Context,
    private val networkConnectionChecker: NetworkConnectionChecker
) : RxPagingSource<String, GameStream>() {

    private var isDatabaseShouldBeCleared: Boolean = true

    /*override suspend fun load(params: LoadParams<String>): LoadResult<String, GameStream> {
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
                data = data.map { gameStream ->
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
        } catch (e: UnknownHostException) {
            appNetworkMode = AppNetworkMode.OFFLINE
            return LoadResult.Error(e)
        } catch (e: Throwable) {
            return LoadResult.Error(e)
        }
    }*/

    override fun loadSingle(params: LoadParams<String>): Single<LoadResult<String, GameStream>> {
        val nextPage = params.key ?: ""


        val loadResult = when (networkConnectionChecker.getNetworkState()) {
            NetworkState.AVAILABLE -> twitchGameStreamsApi.getGameStreams(nextPage)
                .flatMap {
                    val entities =
                        it.data.map { it.toModel() }.map { GameStreamEntity.fromModel(it) }
                    localDatasource.saveGameStreams(entities)
                    makePage(entities)
                }
            NetworkState.NOT_AVAILABLE  -> if (params.key != null) {
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
                    .flatMap { makePage(it) }
            }
        }
        return loadResult.onErrorResumeNext {
            Single.just(
                when (it) {
                    is UnknownHostException -> {
                        LoadResult.Error(it)
                    }
                    is retrofit2.adapter.rxjava3.HttpException -> {
                        LoadResult.Error(HttpException(it.code()))
                    }
                    else -> LoadResult.Error(it)
                }
            )
        }
        /*return twitchGameStreamsApi.getGameStreams(nextPage)
            .flatMap {
                val entities = it.data.map { it.toModel() }.map { GameStreamEntity.fromModel(it) }
                localDatasource.saveGameStreams(entities)
                makePage(entities)
            }.onErrorResumeNext {
                if (params.key != null) {
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
                                Single.just(
                                    LoadResult.Page(
                                        data = emptyList(),
                                        prevKey = null,
                                        nextKey = null
                                    )
                                )
                            }

                        }
                } else {
                    localDatasource.getGameStreamsFirstPage()
                        .flatMap { makePage(it) }
                }
            }.onErrorReturn {
                LoadResult.Error(it)
            }*/
    }

    private fun makePage(list: List<GameStreamEntity>): Single<LoadResult<String, GameStream>> =
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
            nextKey = list.last().accessKey
        )
        )

    override fun getRefreshKey(state: PagingState<String, GameStream>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            val pageIndex = state.pages.indexOf(anchorPage)
            if (pageIndex == 0) {
                isDatabaseShouldBeCleared = true
                return null
            }
            anchorPage?.nextKey
        }
    }
}