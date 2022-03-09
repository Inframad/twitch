package com.example.twitchapp.ui.streams

import androidx.recyclerview.widget.DiffUtil
import com.example.twitchapp.data.model.streams.GameStream

class GameStreamComparator : DiffUtil.ItemCallback<GameStream>() {
    override fun areItemsTheSame(oldItem: GameStream, newItem: GameStream): Boolean {
        return oldItem.GUID == newItem.GUID
    }

    override fun areContentsTheSame(oldItem: GameStream, newItem: GameStream): Boolean {
        return oldItem == newItem
    }

}