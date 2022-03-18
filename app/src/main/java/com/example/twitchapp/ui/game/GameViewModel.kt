package com.example.twitchapp.ui.game

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.twitchapp.R
import com.example.twitchapp.common.BaseViewModel
import com.example.twitchapp.model.Result
import com.example.twitchapp.model.game.Game
import com.example.twitchapp.model.notifications.TwitchNotification
import com.example.twitchapp.model.streams.GameStream
import com.example.twitchapp.notification.TwitchNotifier
import com.example.twitchapp.repository.Repository
import com.example.twitchapp.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val repository: Repository,
    private val notifier: TwitchNotifier
) : BaseViewModel(context) {

    val uiState = mutableStateFlow(UiState.Loading as UiState<GameScreenModel>)
    private var game: Game? = null

    val toggleFavouriteCommand = TCommand<Int>()

    fun init(stream: GameStream?, notification: TwitchNotification.GameNotification?) {
        stream?.let {
            fetchGameModel(stream.gameName, stream.userName, stream.viewerCount)
            return
        }
        notification?.let { fetchGameModel(it.gameName, it.streamerName, it.viewersCount)}
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

    private fun fetchGameModel(gameName: String?, streamerName: String?, viewersCount: Long?) {
        viewModelScope.launch {
            uiState.setValue(
                when (val result = repository.getGame(gameName)) {
                    is Result.Success -> {
                        result.data.apply {
                            game = this
                            toggleFavourite()
                        }
                        UiState.Loaded(
                            GameScreenModel(
                                name = result.data.name ?: getString(R.string.scr_any_lbl_unknown),
                                streamerName = streamerName
                                    ?: getString(R.string.scr_any_lbl_unknown),
                                viewersCount = viewersCount.toString(),
                                imageUrl = result.data.imageUrl
                            )
                        )
                    }
                    Result.Empty -> UiState.Empty
                    is Result.Error -> UiState.Error(handleBaseError(result.e))
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

    fun onMessageReceived(twitchNotification: TwitchNotification.GameNotification?) {
        game?.let { game ->
            twitchNotification?.let {
                if (twitchNotification.gameName == game.name) {
                    showToastCommand.setValue(getString(R.string.scr_game_lbl_game_data_updated))
                    uiState.setValue( //TODO Сохранить при повороте
                        UiState.Loaded(
                            GameScreenModel(
                                name = it.gameName!!,
                                streamerName = it.streamerName!!,
                                viewersCount = it.viewersCount!!.toString(),
                                imageUrl = game.imageUrl
                            )
                        )
                    )
                } else {
                    notifier.showNotification(twitchNotification)
                }
            }
        }
    }
}