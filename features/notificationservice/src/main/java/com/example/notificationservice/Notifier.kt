package com.example.notificationservice

import com.example.twitchapp.model.notifications.TwitchNotification

interface Notifier {
    fun showNotification(notification: TwitchNotification)
}