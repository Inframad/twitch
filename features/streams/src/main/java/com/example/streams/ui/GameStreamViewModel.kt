package com.example.streams.ui

import android.content.Context
import androidx.paging.*
import com.example.common.livedata.BaseViewModelLiveData
import com.example.repository.Repository
import com.example.streams.GameStreamsPagingSourceFactory
import com.example.streams.R
import com.example.streams.StreamsNavigator
import com.example.twitchapp.datasource.GAME_STREAMS_PAGE_SIZE
import com.example.twitchapp.model.NetworkState
import com.example.twitchapp.model.exception.DatabaseException
import com.example.twitchapp.model.streams.GameStream
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class GameStreamViewModel @Inject constructor(
    @ApplicationContext context: Context,
    repository: Repository,
    pagingSourceFactory: GameStreamsPagingSourceFactory,
    private val navigator: StreamsNavigator
) : BaseViewModelLiveData(context) {

    val isRefreshing = TCommand<Boolean>()
    val refreshCommand = Command()
    val retryCommand = Command()
    val isNoDataPlaceholderVisible = TCommand<Boolean>()

    val gameStreamsLiveData = Pager(
        PagingConfig(pageSize = GAME_STREAMS_PAGE_SIZE)
    ) {
        pagingSourceFactory.create()
    }.liveData.cachedIn(this)

    init {
        if (repository.getCurrentNetworkState() == NetworkState.NOT_AVAILABLE) {
            showToast(getString(R.string.src_streams_lbl_saved_data_is_loaded))
        }
    }

    fun onScrollEndError() {
        retryCommand.setValue(Unit)
    }

    fun onSwipeToRefresh() {
        refreshCommand.setValue(Unit)
    }

    fun onPagesLoadStateChanged(loadState: CombinedLoadStates) {
        if (loadState.refresh is LoadState.Error) {
            val error = (loadState.refresh as LoadState.Error).error
            handleError(error)
            if (error is DatabaseException) isNoDataPlaceholderVisible.setValue(true)
            isRefreshing.setValue(false)
        }
    }

    private fun handleError(e: Throwable) {
        if (e is DatabaseException) {
            isNoDataPlaceholderVisible.setValue(true)
            isRefreshing.setValue(false)
            return
        }

        showToast(handleBaseError(e))
    }

    fun onFooterLoadStateChanged(loadState: LoadState) =
        FooterItem(
            errorMsg = if (loadState is LoadState.Error) handleBaseError(loadState.error)
            else "",
            progressBarIsVisible = loadState is LoadState.Loading,
            retryButtonIsVisible = loadState is LoadState.Error,
            errorMsgIsVisible = loadState is LoadState.Error
        )

    fun onPagesUpdated() {
        isNoDataPlaceholderVisible.setValue(false)
        isRefreshing.setValue(false)
    }

    fun onStreamClicked(gameStream: GameStream) {
        navigator.openGame(stream = gameStream)
    }

}