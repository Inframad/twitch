package com.example.twitchapp.ui.streams

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.twitchapp.R
import com.example.twitchapp.data.model.NetworkState
import com.example.twitchapp.data.repository.Repository
import com.example.twitchapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameStreamViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val repository: Repository
) : BaseViewModel(context) {

    val gameStreamsFlow = repository.gameStreamsFlow.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            if (repository.getCurrentNetworkState() == NetworkState.NOT_AVAILABLE) {
                showToast(getString(R.string.no_saved_data_msg))
            }
        }
    }
}