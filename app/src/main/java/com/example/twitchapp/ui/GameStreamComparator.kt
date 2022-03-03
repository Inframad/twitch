package com.example.twitchapp.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.twitchapp.data.model.GameStream

class GameStreamComparator : DiffUtil.ItemCallback<GameStream>() {
    override fun areItemsTheSame(oldItem: GameStream, newItem: GameStream): Boolean {
        return oldItem.GUID == newItem.GUID
    }

    override fun areContentsTheSame(oldItem: GameStream, newItem: GameStream): Boolean {
        return oldItem == newItem
    }

}