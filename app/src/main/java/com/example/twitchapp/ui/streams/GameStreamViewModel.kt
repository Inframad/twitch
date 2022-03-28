package com.example.twitchapp.ui.streams

import android.content.Context
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.example.twitchapp.R
import com.example.twitchapp.common.livedata.BaseViewModelLiveData
import com.example.twitchapp.model.NetworkState
import com.example.twitchapp.model.exception.DatabaseException
import com.example.twitchapp.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class GameStreamViewModel @Inject constructor(
    @ApplicationContext context: Context,
    repository: Repository
) : BaseViewModelLiveData(context) {

    val isRefreshing = TCommand<Boolean>()
    val refreshCommand = Command()
    val retryCommand = Command()
    val isNoDataPlaceholderVisible = TCommand<Boolean>()
    val gameStreamsLiveData = repository.getGameStreamsLiveData()
        .liveData.cachedIn(this)

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

}