package com.example.twitchapp.ui

import android.content.Context
import android.os.Bundle
import android.view.*
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
import com.example.twitchapp.ui.util.showToast
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

        setHasOptionsMenu(true)

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
            viewModel.gameStreamsFlow.collectLatest {
                pagingDataAdapter.submitData(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.app_review_menu_item -> {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, AppReviewFragment())
                    .addToBackStack("APP_REVIEW")
                    .commit()
            }
        }
        return true
    }

    private fun handleError(state: LoadState.Error) {
        context?.showToast(
            getString(
                when (state.error) {
                    is DatabaseException -> R.string.offline_mode_error_msg
                    is UnknownHostException -> R.string.check_internet_connection_msg
                    is SocketTimeoutException -> R.string.check_internet_connection_msg
                    else -> R.string.unknown_error_msg
                }
            )
        )
        if (state.error is DatabaseException) binding.noDataMsgTv.visibility = View.VISIBLE
        binding.swipeRefreshLayout.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}