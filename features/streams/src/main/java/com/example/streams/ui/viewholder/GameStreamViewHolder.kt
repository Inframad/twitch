package com.example.streams.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.common.extensions.glideImage
import com.example.streams.databinding.ItemGameStreamBinding
import com.example.twitchapp.model.streams.GameStream

class GameStreamViewHolder(private val binding: ItemGameStreamBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(gameStream: GameStream, onClick: (GameStream) -> Unit) {
        binding.apply {

            gameStreamImageView.glideImage(gameStream.imageUrl)

            gameNameTextView.text = gameStream.gameName
            streamerNameTextView.text = gameStream.userName
            amountOfViewsTv.text = gameStream.viewerCount.toString()

            root.setOnClickListener { onClick(gameStream) }
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
