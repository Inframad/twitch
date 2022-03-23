package com.example.twitchapp.ui

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewModelScope
import com.example.twitchapp.BuildConfig
import com.example.twitchapp.common.BaseViewModel
import com.example.twitchapp.model.notifications.GameNotification
import com.example.twitchapp.model.notifications.TwitchNotification
import com.example.twitchapp.repository.notification.NotificationRepository
import com.example.twitchapp.ui.game.GameFragmentArgs
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val notificationRepository: NotificationRepository
) : BaseViewModel(context) {

    private var _currentScreen = AppScreen.STREAMS
    val toggleBottomNavigationViewVisibility = TCommand<Boolean>()

    init {
        viewModelScope.launch {
            notificationRepository.getNotificationsEvent()
                .takeWhile { _currentLifecycleOwnerState == Lifecycle.Event.ON_RESUME }
                .collect { messageReceived(it) }
        }
    }

    fun onDestinationChanged(screen: AppScreen) {
        _currentScreen = screen
        toggleBottomNavigationViewVisibility.setValue(
            when (screen) {
                AppScreen.APP_REVIEW -> false
                else -> true
            }
        )
    }

    fun initialized() {
        FirebaseMessaging.getInstance().subscribeToTopic(BuildConfig.FCM_TOPIC_NAME)
    }

    fun onIntent(notification: GameNotification) {
        navigateToGameScreenCommand.setValue(GameFragmentArgs(notification = notification))
    }

    private fun messageReceived(twitchNotification: TwitchNotification?) {
        twitchNotification?.let {
            if(_currentScreen != AppScreen.NOTIFICATIONS
                && _currentScreen != AppScreen.GAME) {
                showToastCommand.setValue(
                    twitchNotification.description?.let { description ->
                        "${twitchNotification.title}\n$description"
                    }
                        ?: twitchNotification.title
                )
            }
        }
    }
}