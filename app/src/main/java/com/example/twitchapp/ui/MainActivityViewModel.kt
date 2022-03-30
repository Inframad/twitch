package com.example.twitchapp.ui

import android.content.Context
import com.example.repository.notification.NotificationRepository
import com.example.twitchapp.BuildConfig
import com.example.common.livedata.BaseViewModelLiveData
import com.example.twitchapp.model.notifications.GameNotification
import com.example.twitchapp.model.notifications.TwitchNotification
import com.example.streams.game.GameFragmentArgs
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @ApplicationContext context: Context,
    notificationRepository: NotificationRepository
) : BaseViewModelLiveData(context) {

    private var _currentScreen = AppScreen.STREAMS
    val toggleBottomNavigationViewVisibility = TCommand<Boolean>()
    val navigateToGameScreenCommand = TCommand<GameFragmentArgs>()

    init {
        notificationRepository.getNotificationsEvent()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::messageReceived){}
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
                        ?: twitchNotification.title.toString()
                )
            }
        }
    }
}