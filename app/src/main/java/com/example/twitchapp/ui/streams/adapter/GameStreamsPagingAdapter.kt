package com.example.twitchapp.ui.streams.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.twitchapp.data.model.streams.GameStream
import com.example.twitchapp.ui.streams.viewholder.GameStreamViewHolder

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

