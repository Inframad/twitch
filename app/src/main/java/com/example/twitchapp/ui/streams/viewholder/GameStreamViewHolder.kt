package com.example.twitchapp.ui.streams.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.twitchapp.data.model.streams.GameStream
import com.example.twitchapp.databinding.ItemGameStreamBinding
import com.example.twitchapp.ui.util.glideImage

class GameStreamViewHolder(private val binding: ItemGameStreamBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(gameStream: GameStream, onClick: (GameStream) -> Unit) {
        binding.apply {

            gameStreamIv.glideImage(gameStream.imageUrl)

            gameNameTv.text = gameStream.gameName
            usernameTv.text = gameStream.userName
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
