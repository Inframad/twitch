package com.example.twitchapp.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.twitchapp.App
import com.example.twitchapp.AppState
import com.example.twitchapp.R
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
        intent.action = NotificationConst.INTENT_FILTER_FIREBASE
        val notificationModel = intent.extras?.let {
            createNotificationModel(it)
        }

        notificationModel?.let {
            CoroutineScope(Dispatchers.IO).launch {
                notificationRepository.save(it)
            }

            when ((application as App).currentAppState) {
                AppState.OnActivityCreated,
                AppState.OnActivityStarted,
                AppState.OnActivityResumed -> {
                    applicationContext.sendBroadcast(intent)
                }
                else -> {
                    createNotificationChannel()
                    showNotification(it)
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = NotificationConst.CHANNEL_NAME
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                NotificationConst.CHANNEL_ID,
                name,
                importance
            )
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(notification: TwitchNotification) {
        with(NotificationManagerCompat.from(this)) {
            notify(
                123,
                NotificationCompat.Builder(applicationContext, NotificationConst.CHANNEL_ID)  //TODO
                    .setSmallIcon(R.drawable.ic_twitch)
                    .setContentTitle(notification.title)
                    .setContentText(notification.description)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT).build()
            )
        }
    }

    private fun createNotificationModel(bundle: Bundle): TwitchNotification {
        bundle.apply {
            return when (val type = bundle.getString("notification_type")) {
                "STREAMS" -> TwitchNotification.StreamNotification(
                    bundle.getString("title")
                        ?: throw NullPointerException("Notification title from FCM is null"),
                    bundle.getString("description")
                        ?: throw NullPointerException("Notification desc from FCM is null"),
                    bundle.getInt("streams_amount"),
                    123 //TODO date
                )
                "GAME" -> TwitchNotification.GameNotification(
                    bundle.getString("title")
                        ?: throw NullPointerException("Notification title from FCM is null"),
                    bundle.getString("description")
                        ?: throw NullPointerException("Notification desc from FCM is null"),
                    gameName = getString("game_name")
                        ?: throw NullPointerException("gameName from FCM is null"),
                    streamerName = getString("streamer_name")
                        ?: throw java.lang.NullPointerException("streamerName from FCM is null"),
                    viewersCount = getString("viewers_count")!!.toLong(),
                    date = 12345678L
                )
                else -> throw IllegalArgumentException("Unknown notification type: $type")
            }
        }
    }
}