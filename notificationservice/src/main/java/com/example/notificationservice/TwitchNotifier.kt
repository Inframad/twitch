package com.example.notificationservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notificationservice.NotificationConst.CHANNEL_ID
import com.example.notificationservice.NotificationConst.CHANNEL_NAME
import com.example.notificationservice.NotificationConst.NOTIFICATION_ID
import com.example.notificationservice.NotificationConst.NOTIFICATION_REQUEST_CODE
import com.example.notificationservice.NotificationConst.TWITCH_NOTIFICATION_KEY
import com.example.twitchapp.model.notifications.GameNotification
import com.example.twitchapp.model.notifications.TwitchNotification
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
class TwitchNotifier @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    @DestinationActivity private val activityClass: KClass<out AppCompatActivity>
): Notifier {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_NAME
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                CHANNEL_ID,
                name,
                importance
            )
            val notificationManager: NotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun showNotification(notification: TwitchNotification) {
        with(NotificationManagerCompat.from(applicationContext)) {
            val notificationBuilder =
                NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                    .setSmallIcon(com.example.common.R.drawable.ic_twitch)
                    .setContentTitle(notification.title)
                    .setContentText(notification.description)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)

            val clickIntent = Intent(applicationContext, activityClass.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            if (notification is GameNotification) {
                clickIntent.putExtra(TWITCH_NOTIFICATION_KEY, notification)
            }

            val flags =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }

            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                NOTIFICATION_REQUEST_CODE,
                clickIntent,
                flags
            )
            notificationBuilder.setContentIntent(pendingIntent)

            notify(
                NOTIFICATION_ID,
                notificationBuilder.build()
            )
        }
    }
}