package com.example.favourite

import androidx.recyclerview.widget.DiffUtil
import com.example.twitchapp.model.game.Game

class FavouriteGameComparator : DiffUtil.ItemCallback<Game>() {
    override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem == newItem
    }
}