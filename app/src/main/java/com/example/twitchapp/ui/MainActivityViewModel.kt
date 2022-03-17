package com.example.twitchapp.ui

import android.content.Context
import android.os.Bundle
import com.example.twitchapp.common.BaseViewModel
import com.example.twitchapp.notification.NotificationConst.MessageKeys
import com.example.twitchapp.notification.NotificationConst.NotificationType
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @ApplicationContext context: Context
) : BaseViewModel(context) {

    val toggleBottomNavigationViewVisibility = TCommand<Boolean>()
    val sendIntentToGameScreenCommand = TCommand<Bundle>()

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

    fun messageReceived(bundle: Bundle?, currentScreen: AppScreen) {
        bundle?.let {
            when (bundle.getString("notification_type")) {
                NotificationType.STREAMS ->
                    showToastCommand.setValue(bundle.getString(MessageKeys.DESCRIPTION))
                NotificationType.GAME -> {
                    when (currentScreen) {
                        AppScreen.GAME ->
                            sendIntentToGameScreenCommand.setValue(bundle)
                        else ->
                            showToastCommand.setValue(bundle.getString(MessageKeys.DESCRIPTION))
                    }
                }
            }
        }
    }
}