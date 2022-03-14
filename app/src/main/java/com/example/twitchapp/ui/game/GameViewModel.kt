package com.example.twitchapp.ui.game

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.twitchapp.R
import com.example.twitchapp.common.BaseViewModel
import com.example.twitchapp.model.Result
import com.example.twitchapp.model.game.Game
import com.example.twitchapp.model.streams.GameStream
import com.example.twitchapp.repository.Repository
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
    private var game: Game? = null

    val toggleFavouriteCommand = TCommand<Int>()

    fun init(stream: GameStream) {
        fetchGameModel(stream)
    }

    fun favouriteGameImageButtonClicked() {
        var savedGame = game ?: return
        savedGame = savedGame.copy(isFavourite = !savedGame.isFavourite)
        viewModelScope.launch {
            repository.updateGame(savedGame)
        }
        game = savedGame
        toggleFavourite()
    }

    private fun fetchGameModel(gameStream: GameStream) {
        viewModelScope.launch {
            uiState.setValue(
                when (val result = repository.getGame(gameStream.gameName)) {
                    is Result.Success -> {
                        result.data.apply {
                            game = this
                            toggleFavourite()
                        }
                        UiState.Loaded(
                            GameScreenModel(
                                name = result.data.name,
                                streamerName = gameStream.userName,
                                viewersCount = gameStream.viewerCount.toString(),
                                imageUrl = result.data.imageUrl
                            )
                        )
                    }
                    Result.Empty -> UiState.Empty
                    is Result.Error -> UiState.Error(handleError(result.e))
                }
            )
        }
    }

    private fun toggleFavourite() {
        toggleFavouriteCommand.setValue(
            if (game?.isFavourite == true) R.color.red_400
            else R.color.grey_400
        )
    }


    private fun handleError(e: Throwable): String =
        when (e) {
            is SocketTimeoutException -> getString(R.string.check_internet_connection_msg)
            is UnknownHostException -> getString(R.string.check_internet_connection_msg)
            else -> getString(R.string.unknown_error_msg)
        }
}