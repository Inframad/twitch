package com.example.twitchapp.ui

import android.content.Context
import com.example.twitchapp.common.BaseViewModel
import com.example.twitchapp.model.notifications.GameNotification
import com.example.twitchapp.model.notifications.TwitchNotification
import com.example.twitchapp.notification.NotificationConst
import com.example.twitchapp.notification.TwitchNotifier
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val notifier: TwitchNotifier
) : BaseViewModel(context) {

    private var _currentScreen = AppScreen.STREAMS
    val toggleBottomNavigationViewVisibility = TCommand<Boolean>()
    val sendIntentToGameScreenCommand = TCommand<GameNotification>()
    val navigateToGameScreenCommand = TCommand<GameNotification>()

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
        FirebaseMessaging.getInstance().subscribeToTopic(NotificationConst.FCM_TOPIC_NAME)
    }

    fun onIntent(notification: GameNotification) {
        navigateToGameScreenCommand.setValue(notification)
    }

    fun messageReceived(twitchNotification: TwitchNotification?) {
        twitchNotification?.let {
            when {
                _currentScreen == AppScreen.GAME && it is GameNotification ->
                    sendIntentToGameScreenCommand.setValue(twitchNotification as GameNotification)
                _currentScreen == AppScreen.GAME && it !is GameNotification ->
                    notifier.showNotification(it)
                else -> showToastCommand.setValue("${twitchNotification.title}\n${twitchNotification.description}")
            }
        }
    }

}