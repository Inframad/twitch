package com.example.twitchapp.ui.streams

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.twitchapp.R
import com.example.twitchapp.databinding.FragmentGameStreamsBinding
import com.example.twitchapp.ui.BaseFragment
import com.example.twitchapp.ui.bindAction
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
            findNavController().navigate(
                GameStreamsFragmentDirections
                    .actionGameStreamsPageToGameFragment(gameStream)
            )
        }

        viewBinding.apply {
            gameStreamsRecyclerView.adapter =
                pagingDataAdapter?.withLoadStateFooter(footer = GameStreamsLoadStateAdapter {
                    viewModel.onScrollEndError()
                })

            swipeRefreshLayout.setOnRefreshListener { viewModel.onSwipeToRefresh() }
        }

        pagingDataAdapter?.apply {
            addOnPagesUpdatedListener { viewModel.onPagesUpdated() }

            addLoadStateListener { viewModel.onPagesLoadStateChanged(it) }
        }
    }
}