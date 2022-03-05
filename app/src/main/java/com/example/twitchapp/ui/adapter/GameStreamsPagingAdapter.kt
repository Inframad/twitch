package com.example.twitchapp.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.twitchapp.data.model.GameStream
import com.example.twitchapp.ui.viewholder.GameStreamViewHolder

class GameStreamsPagingAdapter(
    diffCallback: DiffUtil.ItemCallback<GameStream>
) : PagingDataAdapter<GameStream, GameStreamViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: GameStreamViewHolder, position: Int) {
        getItem(position)?.let { item -> holder.bind(item) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameStreamViewHolder =
        GameStreamViewHolder.from(parent)
}

