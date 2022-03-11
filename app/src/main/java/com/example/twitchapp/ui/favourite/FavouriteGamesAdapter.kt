package com.example.twitchapp.ui.favourite

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.twitchapp.data.model.game.Game

class FavouriteGamesAdapter(): ListAdapter<Game, FavouriteGameViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteGameViewHolder =
        FavouriteGameViewHolder.from(parent)

    override fun onBindViewHolder(holder: FavouriteGameViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class DiffCallback : DiffUtil.ItemCallback<Game>() {
    override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem == newItem
    }
}