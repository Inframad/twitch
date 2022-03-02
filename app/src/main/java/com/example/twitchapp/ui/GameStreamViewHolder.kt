package com.example.twitchapp.ui

import androidx.recyclerview.widget.RecyclerView
import com.example.twitchapp.data.model.GameStream
import com.example.twitchapp.databinding.ItemGameStreamBinding

class GameStreamViewHolder(private val binding: ItemGameStreamBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(gameStream: GameStream) {
        binding.apply {
            gameNameTv.text = gameStream.gameName
            usernameTv.text = gameStream.username
            amountOfViewsTv.text = gameStream.viewerCount.toString()
        }
    }
}
