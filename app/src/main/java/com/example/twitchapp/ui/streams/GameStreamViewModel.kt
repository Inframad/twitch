package com.example.twitchapp.ui.streams

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.cachedIn
import com.example.twitchapp.R
import com.example.twitchapp.model.DatabaseException
import com.example.twitchapp.model.NetworkState
import com.example.twitchapp.repository.Repository
import com.example.twitchapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
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
    val gameStreamsFlow = repository.gameStreamsFlow.cachedIn(viewModelScope)

    init {
        if (repository.getCurrentNetworkState() == NetworkState.NOT_AVAILABLE) {
            showToast(getString(R.string.no_saved_data_msg))
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
            showToast(
                getString(
                    when (error) {
                        is UnknownHostException -> R.string.check_internet_connection_msg
                        is SocketTimeoutException -> R.string.check_internet_connection_msg
                        else -> R.string.unknown_error_msg
                    }
                )
            )
            if (error is DatabaseException) isNoDataPlaceholderVisible.setValue(true)
            isRefreshing.setValue(false)
        }
    }

    fun onFooterLoadStateChanged(loadState: LoadState) =
        FooterItem(
            errorMsg = if (loadState is LoadState.Error) handleError(loadState)
            else "",
            progressBarIsVisible = loadState is LoadState.Loading,
            retryButtonIsVisible = loadState is LoadState.Error,
            errorMsgIsVisible = loadState is LoadState.Error
        )

    private fun handleError(loadState: LoadState.Error): String {
        return when (loadState.error) {
            is HttpException ->
                when ((loadState.error as HttpException).code()) {
                    429 -> getString(R.string.too_many_request_error)
                    404 -> getString(R.string.not_found_error_msg)
                    else -> loadState.error.localizedMessage
                }
            is IOException -> getString(R.string.check_internet_connection_msg)
            else -> loadState.error.localizedMessage
                ?: getString(R.string.unknown_error_msg)
        }
    }

    fun onPagesUpdated() {
        isNoDataPlaceholderVisible.setValue(false)
        isRefreshing.setValue(false)
    }

}