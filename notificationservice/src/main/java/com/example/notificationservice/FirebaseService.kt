package com.example.notificationservice

import android.annotation.SuppressLint
import android.os.Bundle
import com.example.common.AppState
import com.example.common.ApplicationWithState
import com.example.notificationservice.NotificationConst.MessageKeys
import com.example.notificationservice.NotificationConst.NotificationType
import com.example.repository.notification.NotificationRepositoryImpl
import com.example.twitchapp.model.notifications.GameNotification
import com.example.twitchapp.model.notifications.StreamNotification
import com.example.twitchapp.model.notifications.TwitchNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
@AndroidEntryPoint
class FirebaseService: FirebaseMessagingService() {

    @Inject
    lateinit var notificationRepository: NotificationRepositoryImpl

    @Inject
    lateinit var notifier: Notifier

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val notificationModel = message.toIntent().extras?.let {
            createNotificationModel(it)
        }
        notificationModel?.let {
            notificationRepository.saveNotification(it).subscribe()

            when ((application as ApplicationWithState).getCurrentState()) {
                AppState.OnActivityCreated,
                AppState.OnActivityStarted,
                AppState.OnActivityResumed -> {
                }
                else -> {
                    notifier.showNotification(it)
                }
            }
        }
    }

    private fun createNotificationModel(bundle: Bundle): TwitchNotification {
        bundle.apply {
            return when (val type = getString(MessageKeys.NOTIFICATION_TYPE)) {
                NotificationType.STREAMS -> StreamNotification(
                    messageId = getString(MessageKeys.MESSAGE_ID),
                    title = applicationContext.getString(com.example.common.R.string.scr_any_lbl_new_streams_available),
                    date = Date()
                )
                NotificationType.GAME -> GameNotification(
                    messageId = getString(MessageKeys.MESSAGE_ID),
                    title = getString(MessageKeys.GAME_NAME),
                    description = getString(com.example.common.R.string.scr_any_lbl_game_info_updated),
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