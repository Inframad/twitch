package com.example.twitchapp.ui.streams

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.twitchapp.data.model.NetworkState
import com.example.twitchapp.data.repository.Repository
import com.example.twitchapp.ui.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameStreamViewModel @Inject constructor(
    repository: Repository
) : ViewModel() {

    val gameStreamsFlow = repository.gameStreamsFlow.cachedIn(viewModelScope)

    private val _offlineMode: SingleLiveEvent<Unit> = SingleLiveEvent()
    val offlineMode: LiveData<Unit> = _offlineMode

    init {
        if (repository.getCurrentNetworkState() == NetworkState.NOT_AVAILABLE)
            _offlineMode.value = Unit
    }
}