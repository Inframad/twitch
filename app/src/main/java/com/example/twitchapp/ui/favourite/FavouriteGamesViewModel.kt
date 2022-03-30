package com.example.twitchapp.ui.favourite

import android.content.Context
import com.example.repository.Repository
import com.example.twitchapp.model.game.Game
import com.example.twitchapp.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class FavouriteGamesViewModel @Inject constructor(
    @ApplicationContext context: Context,
    repository: Repository
) : com.example.common.livedata.BaseViewModelLiveData(context) {

    val uiState = Data<UiState<List<Game>>>()

    init {
        repository.getFavouriteGames()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { uiState.setValue(UiState.Loading) }
            .subscribe({ list ->
                uiState.setValue(
                    if (list.isEmpty()) UiState.Empty
                    else UiState.Loaded(list)
                )
            },
                {
                    showToast(handleBaseError(it))
                })
            .addToCompositeDisposable()
    }
}