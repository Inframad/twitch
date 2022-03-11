package com.example.twitchapp.ui.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.twitchapp.R
import com.example.twitchapp.data.model.game.Game
import com.example.twitchapp.databinding.ItemFavouriteGameBinding

class FavouriteGameViewHolder(
    private val viewBinding: ItemFavouriteGameBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    private val circularProgressDrawable = CircularProgressDrawable(viewBinding.root.context)

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

    init {
        circularProgressDrawable.apply {
            strokeWidth = 5f
            centerRadius = 30f
        }.start()
    }

    fun bind(game: Game) {
        viewBinding.apply {
            Glide.with(root)
                .load(game.imageUrl)
                .placeholder(circularProgressDrawable)
                .error(R.drawable.image_error_placeholder)
                .into(favouriteGameImageView)
            gameNameTextView.text = game.name
        }
    }
}