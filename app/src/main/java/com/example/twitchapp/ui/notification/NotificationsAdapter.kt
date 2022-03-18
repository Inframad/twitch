package com.example.twitchapp.ui.notification

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.twitchapp.model.notifications.TwitchNotification

class NotificationsAdapter: ListAdapter<TwitchNotification, TwitchNotificationViewHolder>(TwitchNotificationComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TwitchNotificationViewHolder =
        TwitchNotificationViewHolder.from(parent)

    override fun onBindViewHolder(holder: TwitchNotificationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

