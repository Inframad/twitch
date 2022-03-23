package com.example.twitchapp.ui

import android.content.Context
import com.example.twitchapp.BuildConfig
import com.example.twitchapp.common.BaseViewModel
import com.example.twitchapp.model.notifications.GameNotification
import com.example.twitchapp.model.notifications.TwitchNotification
import com.example.twitchapp.ui.game.GameFragmentArgs
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @ApplicationContext context: Context
) : BaseViewModel(context) {

    private var _currentScreen = AppScreen.STREAMS
    val toggleBottomNavigationViewVisibility = TCommand<Boolean>()
    val sendIntentToGameScreenCommand = TCommand<GameNotification>()
    val sendIntentToNotificationsScreenCommand = Command()

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

    fun messageReceived(twitchNotification: TwitchNotification?) {
        twitchNotification?.let {
            when {
                _currentScreen == AppScreen.GAME && it is GameNotification ->
                    sendIntentToGameScreenCommand.setValue(twitchNotification as GameNotification)
                _currentScreen == AppScreen.NOTIFICATIONS ->
                    sendIntentToNotificationsScreenCommand.setValue(Unit)
                else -> {
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

}