package com.example.twitchapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.twitchapp.data.repository.Repository
import javax.inject.Inject

class GameStreamViewModel @Inject constructor(
    repository: Repository
) : ViewModel() {

    val gameStreamsFlow = repository.gameStreamsFlow.cachedIn(viewModelScope)

    val initNetworkState =
        repository.getCurrentNetworkState()

}