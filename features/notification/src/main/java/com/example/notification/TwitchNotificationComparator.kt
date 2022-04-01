package com.example.notification

import androidx.recyclerview.widget.DiffUtil

class TwitchNotificationComparator : DiffUtil.ItemCallback<TwitchNotificationPresentation>() {

    override fun areItemsTheSame(
        oldItem: TwitchNotificationPresentation,
        newItem: TwitchNotificationPresentation
    ): Boolean {
        return oldItem.messageId == newItem.messageId
    }

    override fun areContentsTheSame(
        oldItem: TwitchNotificationPresentation,
        newItem: TwitchNotificationPresentation
    ): Boolean {
        return oldItem == newItem
    }
}