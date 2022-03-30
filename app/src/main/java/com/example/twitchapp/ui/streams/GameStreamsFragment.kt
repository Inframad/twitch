package com.example.twitchapp.ui.streams

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.twitchapp.R
import com.example.common.extensions.bindActionLiveData
import com.example.common.livedata.BaseFragmentLiveData
import com.example.twitchapp.databinding.FragmentGameStreamsBinding
import com.example.twitchapp.navigation.Navigator
import com.example.twitchapp.ui.game.GameFragmentArgs
import com.example.twitchapp.ui.streams.adapter.GameStreamsLoadStateAdapter
import com.example.twitchapp.ui.streams.adapter.GameStreamsPagingAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameStreamsFragment : com.example.common.livedata.BaseFragmentLiveData<GameStreamViewModel>(R.layout.fragment_game_streams) {

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
           bindActionLiveData(gameStreamsLiveData) {
                pagingDataAdapter?.submitData(lifecycle, it)
            }
            bindActionLiveData(refreshCommand) { pagingDataAdapter?.refresh() }
            bindActionLiveData(retryCommand) { pagingDataAdapter?.retry() }
            bindActionLiveData(isNoDataPlaceholderVisible) {
                viewBinding.noDataTextView.isVisible = it
                viewBinding.mainProgressBar.isVisible = false
            }
            bindActionLiveData(isRefreshing) {
                viewBinding.swipeRefreshLayout.isRefreshing = it
            }
        }
    }
}