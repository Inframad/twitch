package com.example.twitchapp.ui.streams

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.cachedIn
import com.example.twitchapp.R
import com.example.twitchapp.common.BaseViewModel
import com.example.twitchapp.model.DatabaseException
import com.example.twitchapp.model.NetworkState
import com.example.twitchapp.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class GameStreamViewModel @Inject constructor(
    @ApplicationContext context: Context,
    repository: Repository
) : BaseViewModel(context) {

    val isRefreshing = TCommand<Boolean>()
    val refreshCommand = Command()
    val retryCommand = Command()
    val isNoDataPlaceholderVisible = TCommand<Boolean>()
    val gameStreamsFlow = repository.getGameStreamsFlow().cachedIn(viewModelScope)

    init {
        if (repository.getCurrentNetworkState() == NetworkState.NOT_AVAILABLE) {
            showToast(getString(R.string.no_saved_data_and_internet_msg))
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
            showToast(handleBaseError(error))
            if (error is DatabaseException) isNoDataPlaceholderVisible.setValue(true)
            isRefreshing.setValue(false)
        }
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