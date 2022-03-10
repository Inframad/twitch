package com.example.twitchapp.ui.streams

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.twitchapp.R
import com.example.twitchapp.databinding.FragmentGameStreamsBinding
import com.example.twitchapp.ui.BaseFragment
import com.example.twitchapp.ui.bindAction
import com.example.twitchapp.ui.bindCommandAction
import com.example.twitchapp.ui.streams.adapter.GameStreamsLoadStateAdapter
import com.example.twitchapp.ui.streams.adapter.GameStreamsPagingAdapter
import dagger.hilt.android.AndroidEntryPoint

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
        viewModel.apply {
            bindAction(gameStreamsFlow) {
                pagingDataAdapter?.submitData(it)
            }
            bindCommandAction(refreshCommand) { pagingDataAdapter?.refresh() }
            bindCommandAction(retryCommand) { pagingDataAdapter?.retry() }
            bindCommandAction(showNoDataPlaceholder) {
                viewBinding.noDataTextView.isVisible = true
            }
            bindCommandAction(hideNoDataPlaceholder) {
                viewBinding.noDataTextView.isVisible = false
            }
            bindCommandAction(stopShowRefreshing) {
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