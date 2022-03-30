package com.example.streams.streams.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.example.streams.streams.FooterItem
import com.example.streams.streams.viewholder.GameStreamLoadStateViewHolder

class GameStreamsLoadStateAdapter(
    private val onLoadStateChanged: (LoadState) -> FooterItem,
    private val retry: () -> Unit
) : LoadStateAdapter<GameStreamLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: GameStreamLoadStateViewHolder, loadState: LoadState) {
        holder.bind(onLoadStateChanged(loadState))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): GameStreamLoadStateViewHolder {
        return GameStreamLoadStateViewHolder.create(parent, retry)
    }
}