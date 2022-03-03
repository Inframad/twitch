package com.example.twitchapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.twitchapp.App
import com.example.twitchapp.R
import com.example.twitchapp.data.model.DatabaseException
import com.example.twitchapp.databinding.FragmentGameStreamsBinding
import com.example.twitchapp.presentation.GameStreamViewModel
import com.example.twitchapp.ui.adapter.GameStreamsLoadStateAdapter
import com.example.twitchapp.ui.adapter.GameStreamsPagingAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class GameStreamsFragment : Fragment() {

    companion object {
        private const val TAG = "GameStreamsFragment"
    }

    private var _binding: FragmentGameStreamsBinding? = null
    private val binding: FragmentGameStreamsBinding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: GameStreamViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameStreamsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[GameStreamViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagingDataAdapter = GameStreamsPagingAdapter(GameStreamComparator())

        binding.rv.adapter =
            pagingDataAdapter.withLoadStateFooter(footer = GameStreamsLoadStateAdapter {
                pagingDataAdapter.retry()
            })

        binding.swipeRefreshLayout.setOnRefreshListener {
            pagingDataAdapter.refresh()
        }

        pagingDataAdapter.addOnPagesUpdatedListener {
            binding.noDataMsgTv.visibility = View.GONE
            binding.swipeRefreshLayout.isRefreshing = false
        }

        pagingDataAdapter.addLoadStateListener {
            if (it.refresh is LoadState.Error) {
                handleError(it.refresh as LoadState.Error)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.gameStreamsFlow.collectLatest {
                pagingDataAdapter.submitData(it)
            }
        }
    }

    private fun handleError(state: LoadState.Error) {
        when (state.error) {
            is DatabaseException -> {
                Toast.makeText(
                    context,
                    getString(R.string.offline_mode_error_msg),
                    Toast.LENGTH_LONG
                ).show()
                binding.noDataMsgTv.visibility = View.VISIBLE
            }
            is UnknownHostException -> {
                Toast.makeText(
                    context,
                    getString(R.string.check_internet_connection_msg),
                    Toast.LENGTH_LONG
                ).show()
            }
            is SocketTimeoutException -> {
                Toast.makeText(
                    context,
                    getString(R.string.check_internet_connection_msg),
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> {
                Toast.makeText(
                    context,
                    getString(R.string.unknown_error_msg),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        binding.swipeRefreshLayout.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}