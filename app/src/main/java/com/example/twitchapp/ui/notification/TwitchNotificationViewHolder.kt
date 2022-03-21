package com.example.twitchapp.ui.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.twitchapp.databinding.ItemTwitchNotificationBinding
import com.example.twitchapp.model.notifications.TwitchNotification
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class TwitchNotificationViewHolder(
    private val viewBinding: ItemTwitchNotificationBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(notification: TwitchNotification) {
        viewBinding.apply {
            notificationTitleTextView.text = notification.title
            notificationDescriptionTextView.text = notification.description
            notificationDateTextView.text =
                DateTimeFormatter.ofPattern("MM.dd hh:mm").format(notification.date.toInstant().atOffset(
                    ZoneOffset.UTC).toLocalDateTime()).toString() //TODO
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