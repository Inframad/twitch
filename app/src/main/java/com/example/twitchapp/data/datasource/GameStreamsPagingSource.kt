package com.example.twitchapp.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.twitchapp.data.model.GameStreamDTO
import com.example.twitchapp.data.network.TwitchApi
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class GameStreamsPagingSource @Inject constructor(
    private val twitchApi: TwitchApi
) : PagingSource<String, GameStreamDTO>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, GameStreamDTO> {
        return try {
            val nextPage = params.key ?: ""
            val response = twitchApi.getGameStreams(nextPage)
            LoadResult.Page(
                data = response.data,
                prevKey = null,
                nextKey = response.pagination.cursor
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, GameStreamDTO>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            val pageIndex = state.pages.indexOf(anchorPage)
            if (pageIndex == 0) return null
            anchorPage?.nextKey
        }
    }

}