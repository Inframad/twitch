package com.example.twitchapp.ui.notification

import androidx.recyclerview.widget.DiffUtil
import com.example.twitchapp.model.notifications.TwitchNotification

class TwitchNotificationComparator : DiffUtil.ItemCallback<TwitchNotification>() {

    override fun areItemsTheSame(oldItem: TwitchNotification, newItem: TwitchNotification): Boolean {
        return oldItem.date == newItem.date //TODO unique id
    }

    override fun areContentsTheSame(oldItem: TwitchNotification, newItem: TwitchNotification): Boolean {
        return oldItem == newItem
    }
}