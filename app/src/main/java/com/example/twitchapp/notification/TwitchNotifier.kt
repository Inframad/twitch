package com.example.twitchapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.twitchapp.R
import com.example.twitchapp.model.notifications.GameNotification
import com.example.twitchapp.model.notifications.TwitchNotification
import com.example.twitchapp.ui.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TwitchNotifier @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = NotificationConst.CHANNEL_NAME
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                NotificationConst.CHANNEL_ID,
                name,
                importance
            )
            val notificationManager: NotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(notification: TwitchNotification) {
        with(NotificationManagerCompat.from(applicationContext)) {
            val notificationBuilder =
                NotificationCompat.Builder(applicationContext, NotificationConst.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_twitch)
                    .setContentTitle(notification.title)
                    .setContentText(notification.description)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)

            val clickIntent = Intent(applicationContext, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            if (notification is GameNotification) {
                clickIntent.putExtra(NotificationConst.TWITCH_NOTIFICATION_KEY, notification)
            }

            val flags =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }

            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                NotificationConst.NOTIFICATION_REQUEST_CODE,
                clickIntent,
                flags
            )
            notificationBuilder.setContentIntent(pendingIntent)

            notify(
                NotificationConst.NOTIFICATION_ID,
                notificationBuilder.build()
            )
        }
    }
}