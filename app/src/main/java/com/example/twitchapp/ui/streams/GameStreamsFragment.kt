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
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.twitchapp.R
import com.example.twitchapp.databinding.FragmentGameStreamsBinding
import com.example.twitchapp.ui.*
import com.example.twitchapp.ui.streams.adapter.GameStreamsLoadStateAdapter
import com.example.twitchapp.ui.streams.adapter.GameStreamsPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameStreamsFragment : BaseFragment<GameStreamViewModel>(R.layout.fragment_game_streams) {

    private val viewBinding: FragmentGameStreamsBinding by viewBinding()
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

        viewModel.apply {
            bindAction(refreshCommand) { pagingDataAdapter?.refresh() }
            bindAction(retryCommand) { pagingDataAdapter?.retry() }
            bindAction(showNoDataPlaceholder) {
                viewBinding.noDataTextView.isVisible = true
            }
            bindAction(hideNoDataPlaceholder) {
                viewBinding.noDataTextView.isVisible = false
            }
            bindAction(stopShowRefreshing) {
                viewBinding.swipeRefreshLayout.isRefreshing = false
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

        viewBinding.apply {
            gameStreamsRecyclerView.adapter =
                pagingDataAdapter?.withLoadStateFooter(footer = GameStreamsLoadStateAdapter {
                    viewModel.onScrollEndError()
                })

            swipeRefreshLayout.setOnRefreshListener { viewModel.onSwipeToRefresh() }
        }

        pagingDataAdapter?.apply {
            addOnPagesUpdatedListener { viewModel.onPagesUpdated()}

            addLoadStateListener { viewModel.onPagesLoadStateChanged(it) }
        }
    }
}