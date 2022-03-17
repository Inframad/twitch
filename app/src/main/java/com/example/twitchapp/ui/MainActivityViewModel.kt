package com.example.twitchapp.ui

import android.content.Context
import com.example.twitchapp.common.BaseViewModel
import com.example.twitchapp.model.notifications.TwitchNotification
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @ApplicationContext context: Context
) : BaseViewModel(context) {

    val toggleBottomNavigationViewVisibility = TCommand<Boolean>()
    val sendIntentToGameScreenCommand = TCommand<TwitchNotification.GameNotification>()

    fun onDestinationChanged(screen: AppScreen) {
        toggleBottomNavigationViewVisibility.setValue(
            when (screen) {
                AppScreen.APP_REVIEW -> false
                else -> true
            }
        )
    }

    fun initialized() {
        FirebaseMessaging.getInstance().subscribeToTopic("main")
    }

    fun messageReceived(twitchNotification: TwitchNotification?, currentScreen: AppScreen) {
        twitchNotification?.let {
            if(it is TwitchNotification.GameNotification) {
                if(currentScreen == AppScreen.GAME)
                    sendIntentToGameScreenCommand.setValue(twitchNotification as TwitchNotification.GameNotification)
            } else {
                showToastCommand.setValue(twitchNotification.description)
            }
        }
    }
}