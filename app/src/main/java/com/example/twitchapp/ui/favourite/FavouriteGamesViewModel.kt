package com.example.twitchapp.ui.favourite

import android.content.Context
import com.example.twitchapp.common.livedata.BaseViewModelLiveData
import com.example.twitchapp.model.exception.DatabaseException
import com.example.twitchapp.model.game.Game
import com.example.twitchapp.repository.Repository
import com.example.twitchapp.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class FavouriteGamesViewModel @Inject constructor(
    @ApplicationContext context: Context,
    repository: Repository
) : BaseViewModelLiveData(context) {

    val uiState = Data<UiState<List<Game>>>()

    init {
        repository.getFavouriteGames()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { uiState.setValue(UiState.Loading) }
            .subscribe(::handleSuccess, ::handleError)
            .addToCompositeDisposable()
    }

    private fun handleSuccess(data: List<Game>) {
        uiState.setValue(UiState.Loaded(data))
    }

    private fun handleError(t: Throwable) {
        when (t) {
            is DatabaseException -> uiState.setValue(UiState.Empty)
        }
    }
}