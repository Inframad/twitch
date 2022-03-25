package com.example.twitchapp.ui.streams

import android.content.Context
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.cachedIn
import com.example.twitchapp.R
import com.example.twitchapp.common.livedata.BaseViewModelLiveData
import com.example.twitchapp.model.NetworkState
import com.example.twitchapp.model.exception.DatabaseException
import com.example.twitchapp.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException
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
    val gameStreamsLiveData = repository.getGameStreamsLiveData().cachedIn(this)

    init {
        if (repository.getCurrentNetworkState() == NetworkState.NOT_AVAILABLE) {
            showToast(getString(R.string.src_streams_lbl_no_saved_data_and_internet))
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
        if (e is NoSuchElementException) {
            isNoDataPlaceholderVisible.setValue(true)
            isRefreshing.setValue(false)
            return
        }

        showToast(
            when (e) {
                is com.example.twitchapp.model.exception.HttpException -> when (e.code) {
                    429 -> getString(R.string.scr_any_lbl_too_many_request)
                    404 -> getString(R.string.scr_any_lbl_not_found)
                    400 -> getString(R.string.scr_any_lbl_check_internet_connection)
                    else -> getString(R.string.scr_any_lbl_unknown_error)
                }
                is SocketTimeoutException -> getString(R.string.scr_any_lbl_check_internet_connection)
                is UnknownHostException -> getString(R.string.scr_any_lbl_check_internet_connection)
                else -> getString(R.string.scr_any_lbl_unknown_error)
            }
        )
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