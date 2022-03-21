package com.example.twitchapp.ui.notification

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

class NotificationsAdapter :
    ListAdapter<TwitchNotificationPresentation, TwitchNotificationViewHolder>(
        TwitchNotificationComparator()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TwitchNotificationViewHolder =
        TwitchNotificationViewHolder.from(parent)

    override fun onBindViewHolder(
        holder: TwitchNotificationViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }
}

