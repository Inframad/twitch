package com.example.twitchapp.ui

import android.content.Context
import com.example.common.livedata.BaseViewModelLiveData
import com.example.navigation.Navigator
import com.example.repository.notification.NotificationRepository
import com.example.twitchapp.AppScreen
import com.example.twitchapp.model.notifications.GameNotification
import com.example.twitchapp.model.notifications.TwitchNotification
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @ApplicationContext context: Context,
    notificationRepository: NotificationRepository,
    private val navigator: Navigator
) : BaseViewModelLiveData(context) {

    private var _currentScreen = AppScreen.STREAMS
    val toggleBottomNavigationViewVisibility = TCommand<Boolean>()

    init {
        notificationRepository.getNotificationsEvent()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::messageReceived){}

        navigator.onDestinationChanged {
            _currentScreen = AppScreen.fromResId(it)
        }
    }

    fun initialized() {
        FirebaseMessaging.getInstance().subscribeToTopic(com.example.twitchapp.BuildConfig.FCM_TOPIC_NAME)
    }

    fun onIntent(notification: GameNotification) {
       navigator.openGame(notification)
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