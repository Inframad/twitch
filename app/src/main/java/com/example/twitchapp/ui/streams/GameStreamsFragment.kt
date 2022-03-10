package com.example.twitchapp.ui.streams

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.twitchapp.R
import com.example.twitchapp.data.model.DatabaseException
import com.example.twitchapp.databinding.FragmentGameStreamsBinding
import com.example.twitchapp.ui.BaseFragment
import com.example.twitchapp.ui.GAME_NAME
import com.example.twitchapp.ui.STREAMER_NAME
import com.example.twitchapp.ui.VIEWERS_COUNT
import com.example.twitchapp.ui.streams.adapter.GameStreamsLoadStateAdapter
import com.example.twitchapp.ui.streams.adapter.GameStreamsPagingAdapter
import com.example.twitchapp.ui.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@AndroidEntryPoint
class GameStreamsFragment : BaseFragment<GameStreamViewModel>(R.layout.fragment_game_streams) {

    private val binding: FragmentGameStreamsBinding by viewBinding()
    override val viewModel: GameStreamViewModel by viewModels()

    private var pagingDataAdapter: GameStreamsPagingAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        bindViewModel()
    }

    override fun bindViewModel() {
        super.bindViewModel()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.gameStreamsFlow.collectLatest {
                    pagingDataAdapter?.submitData(it)
                }
            }
        }
    }

    private fun initViews() {
        pagingDataAdapter = GameStreamsPagingAdapter(GameStreamComparator()) { gameStream ->
            val bundle = bundleOf(
                GAME_NAME to gameStream.gameName,
                STREAMER_NAME to gameStream.userName,
                VIEWERS_COUNT to gameStream.viewerCount
            )
            findNavController().navigate(R.id.action_game_streams_page_to_gameFragment, bundle)
        }

        binding.apply {
            gameStreamsRecyclerView.adapter =
                pagingDataAdapter?.withLoadStateFooter(footer = GameStreamsLoadStateAdapter {
                    pagingDataAdapter?.retry()
                })

            swipeRefreshLayout.setOnRefreshListener {
                pagingDataAdapter?.refresh()
            }
        }

        pagingDataAdapter?.apply {
            addOnPagesUpdatedListener {
                binding.noDataTextView.visibility = View.GONE
                binding.swipeRefreshLayout.isRefreshing = false
            }

            addLoadStateListener {
                if (it.refresh is LoadState.Error) {
                    handleError(it.refresh as LoadState.Error)
                }
            }
        }
    }

    private fun handleError(state: LoadState.Error) {
        context?.showToast(
            getString(
                when (state.error) {
                    is DatabaseException -> R.string.offline_mode_msg
                    is UnknownHostException -> R.string.check_internet_connection_msg
                    is SocketTimeoutException -> R.string.check_internet_connection_msg
                    else -> R.string.unknown_error_msg
                }
            )
        )
        if (state.error is DatabaseException) binding.noDataTextView.isVisible = true
        binding.swipeRefreshLayout.isRefreshing = false
    }
}