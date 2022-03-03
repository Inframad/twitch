package com.example.twitchapp.ui

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.twitchapp.data.model.GameStream

class GameStreamsPagingAdapter(
    diffCallback: DiffUtil.ItemCallback<GameStream>
) : PagingDataAdapter<GameStream, GameStreamViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: GameStreamViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(item) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameStreamViewHolder =
        GameStreamViewHolder.from(parent)
}

