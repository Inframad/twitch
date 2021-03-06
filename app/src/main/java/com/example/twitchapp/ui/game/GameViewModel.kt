package com.example.twitchapp.ui.game

import android.content.Context
import com.example.twitchapp.R
import com.example.twitchapp.common.livedata.BaseViewModelLiveData
import com.example.twitchapp.model.SnackbarData
import com.example.twitchapp.model.exception.DatabaseException
import com.example.twitchapp.model.game.Game
import com.example.twitchapp.model.notifications.GameNotification
import com.example.twitchapp.model.streams.GameStream
import com.example.twitchapp.repository.Repository
import com.example.twitchapp.repository.notification.NotificationRepository
import com.example.twitchapp.ui.UiState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val repository: Repository,
    notificationRepository: NotificationRepository
) : BaseViewModelLiveData(context) {

    val uiState = Data<UiState<GameScreenModel>>()
    private var game: Game? = null
    private var isGameModelFetched = false
    val goBackCommand = Command()

    val toggleFavourite = Data<Int>()

    fun init(stream: GameStream?, notification: GameNotification?) {
        if (!isGameModelFetched) {
            stream?.let {
                fetchGameModel(stream.gameName, stream.userName, stream.viewerCount)
                return
            }
            notification?.let { fetchGameModel(it.gameName, it.streamerName, it.viewersCount) }
        }
    }

    init {
        notificationRepository.getNotificationsEvent()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it is GameNotification) onMessageReceived(it)
            }.addToCompositeDisposable()
    }

    fun favouriteGameImageButtonClicked() {
        var savedGame = game ?: return
        savedGame = savedGame.copy(isFavourite = !savedGame.isFavourite)
        repository.updateGame(savedGame)
            .subscribe()
            .addToCompositeDisposable()
        game = savedGame
        toggleFavourite()
    }

    private fun fetchGameModel(gameName: String?, streamerName: String?, viewersCount: Long?) {
        if (gameName != null) {
            repository.getGame(gameName)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { uiState.setValue(UiState.Loading) }
                .map {
                    it to GameScreenModel(
                        name = it.name
                            ?: getString(R.string.scr_any_lbl_unknown),
                        streamerName = streamerName
                            ?: getString(R.string.scr_any_lbl_unknown),
                        viewersCount = viewersCount.toString(),
                        imageUrl = it.imageUrl
                    )
                }.subscribe({ (g, gameScreenModel) ->
                    isGameModelFetched = true
                    game = g
                    uiState.setValue(UiState.Loaded(gameScreenModel))
                }, {
                    if(it is DatabaseException) uiState.setValue(UiState.Empty)
                    else showToast(handleBaseError(it))
                })
                .addToCompositeDisposable()
        } else {
            showSnackbarCommand.setValue(SnackbarData(
                getString(R.string.scr_game_lbl_game_is_not_found),
                getString(R.string.scr_any_lbl_go_back),
                Snackbar.LENGTH_INDEFINITE
            ) {
                goBackCommand.setValue(Unit)
            })
        }
    }

    private fun toggleFavourite() {
        toggleFavourite.setValue(
            if (game?.isFavourite == true) R.color.red_400
            else R.color.grey_400
        )
    }

    private fun onMessageReceived(twitchNotification: GameNotification?) {
        game?.let { game ->
            twitchNotification?.let {
                if (twitchNotification.gameName == game.name) {
                    showToastCommand.setValue(getString(R.string.scr_game_lbl_game_data_updated))
                    uiState.setValue(
                        UiState.Loaded(
                            GameScreenModel(
                                name = it.gameName
                                    ?: getString(R.string.scr_any_lbl_unknown),
                                streamerName = it.streamerName
                                    ?: getString(R.string.scr_any_lbl_unknown),
                                viewersCount = it.viewersCount?.toString()
                                    ?: getString(R.string.scr_any_lbl_null_viewers_count),
                                imageUrl = game.imageUrl
                            )
                        )
                    )
                } else {
                    showSnackbarCommand.setValue(SnackbarData(
                        message = "${twitchNotification.title}\n${twitchNotification.description}",
                        actionName = getString(R.string.scr_any_lbl_go_to)
                    ) {
                        navigateToGameScreenCommand.setValue(
                            GameFragmentArgs(notification = twitchNotification)
                        )
                    })
                }
            }
        }
    }
}