package com.example.twitchapp.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.twitchapp.R
import com.example.twitchapp.data.model.GameStream
import com.example.twitchapp.databinding.ItemGameStreamBinding

class GameStreamViewHolder(private val binding: ItemGameStreamBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val circularProgressDrawable = CircularProgressDrawable(binding.root.context)

    init {
        circularProgressDrawable.apply {
            strokeWidth = 5f
            centerRadius = 30f
        }.start()
    }

    fun bind(gameStream: GameStream) {
        binding.apply {

            Glide.with(root)
                .load(gameStream.imageUrl)
                .placeholder(circularProgressDrawable)
                .error(R.drawable.image_error_placeholder)
                .into(gameStreamIv)

            gameNameTv.text = gameStream.gameName
            usernameTv.text = gameStream.username
            amountOfViewsTv.text = gameStream.viewerCount.toString()
        }
    }

    companion object {
        fun from(parent: ViewGroup): GameStreamViewHolder {
            return GameStreamViewHolder(
                ItemGameStreamBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}
