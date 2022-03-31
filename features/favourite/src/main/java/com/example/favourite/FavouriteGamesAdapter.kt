package com.example.favourite

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.twitchapp.model.game.Game

class FavouriteGamesAdapter: ListAdapter<Game, FavouriteGameViewHolder>(FavouriteGameComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteGameViewHolder =
        FavouriteGameViewHolder.from(parent)

    override fun onBindViewHolder(holder: FavouriteGameViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

