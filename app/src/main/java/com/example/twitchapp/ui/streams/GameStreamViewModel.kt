package com.example.twitchapp.ui.streams

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.cachedIn
import com.example.twitchapp.R
import com.example.twitchapp.data.model.DatabaseException
import com.example.twitchapp.data.model.NetworkState
import com.example.twitchapp.data.repository.Repository
import com.example.twitchapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class GameStreamViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val repository: Repository
) : BaseViewModel(context) {

    val isRefreshing = TCommand<Boolean>()
    val refreshCommand = Command()
    val retryCommand = Command()
    val isNoDataPlaceholderVisible = TCommand<Boolean>()
    val gameStreamsFlow = repository.gameStreamsFlow.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            if (repository.getCurrentNetworkState() == NetworkState.NOT_AVAILABLE) {
                showToast(getString(R.string.no_saved_data_msg))
            }
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

    fun onPagesUpdated() {
        isNoDataPlaceholderVisible.setValue(false)
        isRefreshing.setValue(false)
    }

}