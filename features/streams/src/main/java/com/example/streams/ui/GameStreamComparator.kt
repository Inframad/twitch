package com.example.streams.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.twitchapp.model.streams.GameStream

class GameStreamComparator : DiffUtil.ItemCallback<GameStream>() {

    override fun areItemsTheSame(oldItem: GameStream, newItem: GameStream): Boolean {
        return oldItem.accessKey == newItem.accessKey
    }

    override fun areContentsTheSame(oldItem: GameStream, newItem: GameStream): Boolean {
        return oldItem == newItem
    }
}