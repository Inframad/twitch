package com.example.twitchapp.ui.notification

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.twitchapp.R
import com.example.twitchapp.common.BaseViewModel
import com.example.twitchapp.model.Result
import com.example.twitchapp.model.notifications.TwitchNotification
import com.example.twitchapp.repository.notification.NotificationRepository
import com.example.twitchapp.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsListViewModel
@Inject constructor(
    @ApplicationContext context: Context,
    private val repository: NotificationRepository
) : BaseViewModel(context) {

    val uiState = mutableStateFlow(UiState.Loading as UiState<List<TwitchNotificationPresentation>>)
    val toggleFabVisibilityCommand = mutableStateFlow(false)
    val scrollUpCommand = Command()

    init {
        viewModelScope.launch {
            repository.getAllNotifications().collect { result ->
                uiState.setValue(handleResult(result))
            }
        }
    }

    fun onMessageReceived(scrollPosition: Int, canScrollDown: Boolean) {
        if(scrollPosition != 0 || canScrollDown) toggleFabVisibilityCommand.setValue(true)
    }

    private fun handleResult(
        result: Result<List<TwitchNotification>>
    ): UiState<List<TwitchNotificationPresentation>> =
        when (result) {
            is Result.Success -> {
                if (result.data.isEmpty()) UiState.Empty
                else UiState.Loaded(result.data.map {
                    TwitchNotificationPresentation.fromModel(
                        it,
                        getString(R.string.scr_any_date_time_pattern)
                    )
                })
            }
            is Result.Error -> UiState.Error(handleBaseError(result.e))
            Result.Empty -> UiState.Empty
            Result.Loading -> UiState.Loading
        }

    fun onFloatingActionButtonClicked() {
        toggleFabVisibilityCommand.setValue(false)
        scrollUpCommand.setValue(Unit)
    }

    fun onRecyclerViewScrollStateChanged(canScrollUp: Boolean) {
        if(!canScrollUp) toggleFabVisibilityCommand.setValue(false)
    }
}