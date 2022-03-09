package com.example.twitchapp.ui.streams.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.twitchapp.R
import com.example.twitchapp.databinding.GameStreamsLoadStateFooterBinding
import retrofit2.HttpException
import java.io.IOException

class GameStreamLoadStateViewHolder (
    private val binding: GameStreamsLoadStateFooterBinding,
    private val retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = showError(loadState)
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorMsg.isVisible = loadState is LoadState.Error
    }

    private fun showError(loadState: LoadState.Error): String {
        binding.root.context.apply {
            return when (loadState.error) {
                is HttpException ->
                    when ((loadState.error as HttpException).code()) {
                        429 -> getString(R.string.too_many_request_error)
                        404 -> getString(R.string.not_found_error_msg)
                        else -> loadState.error.localizedMessage
                    }
                is IOException -> getString(R.string.check_internet_connection_msg)
                else -> loadState.error.localizedMessage
                    ?: getString(R.string.unknown_error_msg)
            }
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