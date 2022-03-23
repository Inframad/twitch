package com.example.twitchapp.ui.streams

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.twitchapp.R
import com.example.twitchapp.common.BaseFragment
import com.example.twitchapp.common.extensions.bindAction
import com.example.twitchapp.common.extensions.bindCommandAction
import com.example.twitchapp.databinding.FragmentGameStreamsBinding
import com.example.twitchapp.navigation.Navigator
import com.example.twitchapp.ui.game.GameFragmentArgs
import com.example.twitchapp.ui.streams.adapter.GameStreamsLoadStateAdapter
import com.example.twitchapp.ui.streams.adapter.GameStreamsPagingAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameStreamsFragment : BaseFragment<GameStreamViewModel>(R.layout.fragment_game_streams) {

    private val viewBinding: FragmentGameStreamsBinding by viewBinding()
    override val viewModel: GameStreamViewModel by viewModels()

    private var pagingDataAdapter: GameStreamsPagingAdapter? = null

    override fun initViews() {
        pagingDataAdapter = GameStreamsPagingAdapter(GameStreamComparator()) { gameStream ->
            Navigator.goToGameScreen(this, GameFragmentArgs(stream = gameStream))
        }

        viewBinding.apply {
            gameStreamsRecyclerView.adapter =
                pagingDataAdapter?.withLoadStateFooter(
                    footer = GameStreamsLoadStateAdapter(
                        retry = { viewModel.onScrollEndError() },
                        onLoadStateChanged = {
                            viewModel.onFooterLoadStateChanged(it)
                        })
                )

            swipeRefreshLayout.setOnRefreshListener { viewModel.onSwipeToRefresh() }
        }

        pagingDataAdapter?.apply {
            addOnPagesUpdatedListener { viewModel.onPagesUpdated() }

            addLoadStateListener { viewModel.onPagesLoadStateChanged(it) }
        }
    }

    override fun bindViewModel() {
        super.bindViewModel()
        viewModel.apply {
            bindAction(gameStreamsFlow) {
                pagingDataAdapter?.submitData(it)
            }
            bindCommandAction(refreshCommand) { pagingDataAdapter?.refresh() }
            bindCommandAction(retryCommand) { pagingDataAdapter?.retry() }
            bindCommandAction(isNoDataPlaceholderVisible) {
                viewBinding.noDataTextView.isVisible = it
                viewBinding.mainProgressBar.isVisible = false
            }
            bindCommandAction(isRefreshing) {
                viewBinding.swipeRefreshLayout.isRefreshing = it
            }
        }
    }
}