package com.example.twitchapp.notification

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.example.twitchapp.App
import com.example.twitchapp.AppState
import com.example.twitchapp.R
import com.example.twitchapp.model.notifications.GameNotification
import com.example.twitchapp.model.notifications.StreamNotification
import com.example.twitchapp.model.notifications.TwitchNotification
import com.example.twitchapp.notification.NotificationConst.MessageKeys
import com.example.twitchapp.notification.NotificationConst.NotificationType
import com.example.twitchapp.repository.notification.NotificationRepositoryImpl
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
@AndroidEntryPoint
class FirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationRepository: NotificationRepositoryImpl
    @Inject
    lateinit var notifier: TwitchNotifier

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        CoroutineScope(Dispatchers.IO).launch {

            val notificationModel = message.toIntent().extras?.let {
                createNotificationModel(it)
            }

            val intent = Intent()
            intent.action = NotificationConst.INTENT_FILTER_FIREBASE

            notificationModel?.let {
                notificationRepository.saveNotification(it)

                intent.putExtra(
                    NotificationConst.TWITCH_NOTIFICATION_KEY,
                    notificationModel
                )

                when ((application as App).currentAppState) {
                    AppState.OnActivityCreated,
                    AppState.OnActivityStarted,
                    AppState.OnActivityResumed -> {
                        applicationContext.sendBroadcast(intent)
                    }
                    else -> {
                        notifier.showNotification(it)
                    }
                }
            }
        }
    }

    private fun createNotificationModel(bundle: Bundle): TwitchNotification {
        bundle.apply {
            return when (val type = getString(MessageKeys.NOTIFICATION_TYPE)) {
                NotificationType.STREAMS -> StreamNotification(
                    messageId = getString(MessageKeys.MESSAGE_ID),
                    title = applicationContext.getString(R.string.scr_any_lbl_new_streams_available),
                    date = Date()
                )
                NotificationType.GAME -> GameNotification(
                    messageId = getString(MessageKeys.MESSAGE_ID),
                    title = getString(MessageKeys.GAME_NAME),
                    description = getString(R.string.scr_any_lbl_game_info_updated),
                    gameName = getString(MessageKeys.GAME_NAME),
                    streamerName = getString(MessageKeys.STREAMER_NAME),
                    viewersCount = getString(MessageKeys.VIEWERS_COUNT)?.toLong(),
                    date = Date()
                )
                else -> throw IllegalArgumentException("Unknown notification type: $type")
            }
        }
    }
}