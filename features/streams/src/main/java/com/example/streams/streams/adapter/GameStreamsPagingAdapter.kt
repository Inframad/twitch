package com.example.streams.streams.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.streams.streams.viewholder.GameStreamViewHolder
import com.example.twitchapp.model.streams.GameStream

class GameStreamsPagingAdapter(
    diffCallback: DiffUtil.ItemCallback<GameStream>,
    private val onClick: (GameStream) -> Unit
) : PagingDataAdapter<GameStream, GameStreamViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: GameStreamViewHolder, position: Int) {
        getItem(position)?.let { item -> holder.bind(item, onClick) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameStreamViewHolder =
        GameStreamViewHolder.from(parent)
}

