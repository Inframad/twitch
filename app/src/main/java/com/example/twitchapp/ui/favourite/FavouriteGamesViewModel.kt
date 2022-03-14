package com.example.twitchapp.ui.favourite

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.twitchapp.R
import com.example.twitchapp.common.BaseViewModel
import com.example.twitchapp.model.Result
import com.example.twitchapp.model.game.Game
import com.example.twitchapp.repository.Repository
import com.example.twitchapp.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteGamesViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val repository: Repository
) : BaseViewModel(context) {

    val uiState = mutableStateFlow(UiState.Loading as UiState<List<Game>>)

    init {
        viewModelScope.launch {
            repository.getFavouriteGames().collect { result ->
                uiState.setValue(handleResult(result))
            }
        }
    }

    private fun handleResult(result: Result<List<Game>>): UiState<List<Game>> =
        when (result) {
            is Result.Success -> UiState.Loaded(result.data)
            is Result.Error -> UiState.Error(handleError(result.e))
            Result.Empty -> UiState.Empty
        }

    private fun handleError(e: Throwable): String =
        e.localizedMessage ?: getString(R.string.unknown_error_msg)
}