package com.example.twitchapp.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.twitchapp.App
import com.example.twitchapp.AppState
import com.example.twitchapp.R
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
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationRepository: NotificationRepositoryImpl

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        CoroutineScope(Dispatchers.IO).launch {

            val notificationModel = message.toIntent().extras?.let {
                createNotificationModel(it)
            }

            val intent = Intent()
            intent.action = NotificationConst.INTENT_FILTER_FIREBASE

            notificationModel?.let {

                intent.putExtra(
                    NotificationConst.TWITCH_NOTIFICATION_KEY,
                    notificationModel
                )

                notificationRepository.save(it)

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
                123, //TODO
                NotificationCompat.Builder(applicationContext, NotificationConst.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_twitch)
                    .setContentTitle(notification.title)
                    .setContentText(notification.description)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT).build()
            )
        }
    }

    private fun createNotificationModel(bundle: Bundle): TwitchNotification {
        bundle.apply {
            return when (val type = getString(MessageKeys.NOTIFICATION_TYPE)) {
                NotificationType.STREAMS -> TwitchNotification.StreamNotification(
                    title = getString(MessageKeys.TITLE),
                    description = getString(MessageKeys.DESCRIPTION),
                    date = Date()
                )
                NotificationType.GAME -> TwitchNotification.GameNotification(
                    title = getString(MessageKeys.TITLE),
                    description = getString(MessageKeys.DESCRIPTION),
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