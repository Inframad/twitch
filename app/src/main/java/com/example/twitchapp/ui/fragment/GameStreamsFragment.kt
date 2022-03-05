package com.example.twitchapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.twitchapp.R
import com.example.twitchapp.data.model.DatabaseException
import com.example.twitchapp.databinding.FragmentGameStreamsBinding
import com.example.twitchapp.presentation.GameStreamViewModel
import com.example.twitchapp.ui.adapter.GameStreamsLoadStateAdapter
import com.example.twitchapp.ui.adapter.GameStreamsPagingAdapter
import com.example.twitchapp.ui.util.GameStreamComparator
import com.example.twitchapp.ui.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@AndroidEntryPoint
class GameStreamsFragment : Fragment(R.layout.fragment_game_streams) {

    private val binding: FragmentGameStreamsBinding by viewBinding()

    private val viewModel: GameStreamViewModel by viewModels()


    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagingDataAdapter = GameStreamsPagingAdapter(GameStreamComparator())

        binding.apply {
            rv.adapter =
                pagingDataAdapter.withLoadStateFooter(footer = GameStreamsLoadStateAdapter {
                    pagingDataAdapter.retry()
                })

            swipeRefreshLayout.setOnRefreshListener {
                pagingDataAdapter.refresh()
            }
        }

        pagingDataAdapter.apply {
            addOnPagesUpdatedListener {
                binding.noDataMsgTv.visibility = View.GONE
                binding.swipeRefreshLayout.isRefreshing = false
            }

            addLoadStateListener {
                if (it.refresh is LoadState.Error) {
                    handleError(it.refresh as LoadState.Error)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.gameStreamsFlow.collectLatest {
                    pagingDataAdapter.submitData(it)
                }
            }
        }

        viewModel.offlineMode.observe(viewLifecycleOwner) {
            context?.showToast(getString(R.string.offline_mode_msg))
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
        if (state.error is DatabaseException) binding.noDataMsgTv.visibility = View.VISIBLE
        binding.swipeRefreshLayout.isRefreshing = false
    }
}