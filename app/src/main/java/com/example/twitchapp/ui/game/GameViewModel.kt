package com.example.twitchapp.ui.game

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.twitchapp.R
import com.example.twitchapp.data.model.Result
import com.example.twitchapp.data.model.streams.GameStream
import com.example.twitchapp.data.repository.Repository
import com.example.twitchapp.ui.BaseViewModel
import com.example.twitchapp.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val repository: Repository
) : BaseViewModel(context) {

    val uiState = mutableStateFlow(UiState.Loading as UiState<GameScreenModel>)

    fun init(stream: GameStream) {
        viewModelScope.launch {
            getGame(stream)
        }
    }

    private fun getGame(gameStream: GameStream) {
        viewModelScope.launch {
            uiState.setValue(
                when (val result = repository.getGame(gameStream.gameName)) {
                    is Result.Success -> UiState.Loaded(
                        GameScreenModel(
                            name = result.data.name,
                            streamerName = gameStream.userName,
                            viewersCount = gameStream.viewerCount.toString(),
                            imageUrl = result.data.imageUrl
                        )
                    )
                    Result.Empty -> UiState.Empty
                    is Result.Error -> UiState.Error(handleError(result.e))
                }
            )
        }
    }


    private fun handleError(e: Throwable): String =
        when (e) {
            is SocketTimeoutException -> getString(R.string.check_internet_connection_msg)
            is UnknownHostException -> getString(R.string.check_internet_connection_msg)
            else -> getString(R.string.unknown_error_msg)
        }
}