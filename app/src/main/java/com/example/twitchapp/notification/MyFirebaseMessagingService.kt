package com.example.twitchapp.notification

import android.annotation.SuppressLint
import com.example.twitchapp.model.notifications.TwitchNotification
import com.example.twitchapp.repository.notification.NotificationRepositoryImpl
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationRepository: NotificationRepositoryImpl

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val intent = message.toIntent()
        intent.action = "com.example.twitchapp.message"
        intent.extras?.let {
            CoroutineScope(Dispatchers.IO).launch {
                notificationRepository.save(
                    TwitchNotification.GameNotification(
                        gameName = it.getString("game_name")
                            ?: throw NullPointerException("gameName from FCM is null"),
                        streamerName = it.getString("streamer_name")
                            ?: throw java.lang.NullPointerException("streamerName from FCM is null"),
                        viewersCount = it.getLong("viewers_count"),
                        date = 12345678L
                    )
                )
            }
        }
        applicationContext.sendBroadcast(intent)
    }
}