package com.example.twitchapp.ui.game

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.example.twitchapp.R
import com.example.twitchapp.data.model.Result
import com.example.twitchapp.data.model.game.Game
import com.example.twitchapp.data.repository.Repository
import com.example.twitchapp.ui.*
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

    val gameScreenModel = mutableStateFlow(UiState.Loading as UiState<GameScreenModel>)

    fun bindData(data: Bundle) {
        viewModelScope.launch {
            data.apply {
                val gameName = getString(GAME_NAME) ?: getString(R.string.unknown)
                gameScreenModel.setValue(handleResult(repository.getGame(gameName), this))
            }
        }
    }

    private fun handleResult(result: Result<Game>, data: Bundle): UiState<GameScreenModel> =
        when (result) {
            is Result.Success -> UiState.Loaded(
                GameScreenModel(
                    name = result.data.name,
                    streamerName = data.getString(STREAMER_NAME) ?: getString(R.string.unknown),
                    viewersCount = (data.getLong(VIEWERS_COUNT)).toString(),
                    imageUrl = result.data.imageUrl
                )
            )
            Result.Empty -> UiState.Empty
            is Result.Error -> UiState.Error(handleError(result.e))
        }

    private fun handleError(e: Throwable): String =
        when(e) {
            is SocketTimeoutException -> getString(R.string.check_internet_connection_msg)
            is UnknownHostException -> getString(R.string.check_internet_connection_msg)
            else -> getString(R.string.unknown_error_msg)
        }
}