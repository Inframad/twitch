package com.example.twitchapp.ui.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.twitchapp.data.model.game.Game
import com.example.twitchapp.databinding.ItemFavouriteGameBinding
import com.example.twitchapp.ui.util.glideImage

class FavouriteGameViewHolder(
    private val viewBinding: ItemFavouriteGameBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    companion object {
        fun from(parent: ViewGroup): FavouriteGameViewHolder {
            return FavouriteGameViewHolder(
                ItemFavouriteGameBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    fun bind(game: Game) {
        viewBinding.apply {
            favouriteGameImageView.glideImage(game.imageUrl)
            gameNameTextView.text = game.name
        }
    }
}