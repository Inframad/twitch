package com.example.twitchapp.ui.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.twitchapp.databinding.ItemTwitchNotificationBinding

class TwitchNotificationViewHolder(
    private val viewBinding: ItemTwitchNotificationBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(notification: TwitchNotificationPresentation) {
        viewBinding.apply {
            notificationTitleTextView.text = notification.title
            notificationDescriptionTextView.text = notification.description
            notificationDateTextView.text = notification.dateTime
        }
    }

    companion object {
        fun from(parent: ViewGroup): TwitchNotificationViewHolder {
            return TwitchNotificationViewHolder(
                ItemTwitchNotificationBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}