package com.example.twitchapp.ui.favourite

import android.content.Context
import com.example.twitchapp.common.BaseViewModelRx
import com.example.twitchapp.model.Result
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
) : BaseViewModelRx(context) {

    val uiState = liveData<UiState<List<Game>>>()

    init {
        repository.getFavouriteGames()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                uiState.setValue(handleResult(result))
            }
    }

    private fun handleResult(result: Result<List<Game>>): UiState<List<Game>> =
        when (result) {
            is Result.Success -> {
                if (result.data.isEmpty()) UiState.Empty else UiState.Loaded(result.data)
            }
            is Result.Error -> UiState.Error(handleBaseError(result.e))
            Result.Empty -> UiState.Empty
            Result.Loading -> UiState.Loading
        }
}