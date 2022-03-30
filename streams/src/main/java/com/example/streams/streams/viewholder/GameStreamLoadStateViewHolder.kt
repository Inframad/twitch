package com.example.streams.streams.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.streams.streams.FooterItem
import com.example.streams.R
import com.example.streams.databinding.GameStreamsLoadStateFooterBinding

class GameStreamLoadStateViewHolder (
    private val binding: GameStreamsLoadStateFooterBinding,
    private val retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(footerItem: FooterItem) {
        binding.apply {
            errorMsg.text = footerItem.errorMsg
            progressBar.isVisible = footerItem.progressBarIsVisible
            retryButton.isVisible = footerItem.retryButtonIsVisible
            errorMsg.isVisible = footerItem.errorMsgIsVisible
            retryButton.setOnClickListener { retry.invoke() }
        }
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): GameStreamLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.game_streams_load_state_footer, parent, false)
            val binding = GameStreamsLoadStateFooterBinding.bind(view)
            return GameStreamLoadStateViewHolder(binding, retry)
        }
    }
}