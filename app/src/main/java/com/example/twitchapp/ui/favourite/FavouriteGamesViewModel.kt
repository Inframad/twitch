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
            .subscribe({ list ->
                uiState.setValue(
                    if (list.isEmpty()) UiState.Empty
                    else UiState.Loaded(list)
                )
            },
                {
                    if(it is DatabaseException) uiState.setValue(UiState.Empty)
                    else showToast(handleBaseError(it))
                })
            .addToCompositeDisposable()
    }
}