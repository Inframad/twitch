package com.example.twitchapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.twitchapp.data.model.GameStream
import com.example.twitchapp.databinding.ItemGameStreamBinding

class GameStreamsPagingAdapter(
    diffCallback: DiffUtil.ItemCallback<GameStream>
) : PagingDataAdapter<GameStream, GameStreamViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: GameStreamViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameStreamViewHolder {
        val binding = ItemGameStreamBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GameStreamViewHolder(binding)
    }
}

