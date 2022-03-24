package com.example.twitchapp.ui.notification

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.twitchapp.R
import com.example.twitchapp.common.BaseFragment
import com.example.twitchapp.common.extensions.bindAction
import com.example.twitchapp.common.extensions.bindCommandAction
import com.example.twitchapp.databinding.FragmentSimpleListBinding
import com.example.twitchapp.ui.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsListFragment
    : BaseFragment<NotificationsListViewModel>(R.layout.fragment_simple_list) {

    private val viewBinding: FragmentSimpleListBinding by viewBinding()
    override val viewModel: NotificationsListViewModel by viewModels()

    private var adapter: NotificationsAdapter? = null

    override fun initViews() {
        super.initViews()
        adapter = NotificationsAdapter()
        viewBinding.apply {
            recyclerView.adapter = adapter
            noDataTextView.text = getString(R.string.scr_any_lbl_no_notifications)
            floatingActionButton.setOnClickListener {
                viewModel.onFloatingActionButtonClicked()
            }
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    viewModel.onRecyclerViewScrollStateChanged(recyclerView.canScrollVertically(-1))
                }
            })
        }
    }

    override fun bindViewModel() {
        super.bindViewModel()
        with(viewModel) {
            viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
                when (uiState) {
                    is UiState.Loaded -> showFavouriteGamesList(
                        uiState.data
                    )
                    is UiState.Error -> showError(uiState.msg)
                    UiState.Loading -> showLoading()
                    UiState.Empty -> showNoDataPlaceholder()
                }
            }
            bindAction(toggleFabVisibilityCommand) {
                viewBinding.floatingActionButton.isVisible = it
            }
            bindCommandAction(scrollUpCommand) {
                viewBinding.recyclerView.smoothScrollToPosition(0)
            }
            bindCommandAction(sendScrollStateCommand) {
                onSendScrollStateCommand(
                    (viewBinding.recyclerView.layoutManager as LinearLayoutManager)
                        .findFirstCompletelyVisibleItemPosition(),
                    viewBinding.recyclerView.canScrollVertically(1)
                )
            }
        }
    }

    private fun showLoading() {
        viewBinding.apply {
            progressBarLayout.isVisible = true
            errorLayout.isVisible = false
            noDataLayout.isVisible = false
        }
    }

    private fun showNoDataPlaceholder() {
        viewBinding.apply {
            progressBarLayout.isVisible = false
            errorLayout.isVisible = false
            noDataLayout.isVisible = true
        }
    }

    private fun showError(msg: String) {
        viewBinding.apply {
            progressBarLayout.isVisible = false
            errorLayout.isVisible = true
            noDataLayout.isVisible = false
            errorTextView.text = msg
        }
    }

    private fun showFavouriteGamesList(notifications: List<TwitchNotificationPresentation>) {
        viewBinding.apply {
            progressBarLayout.isVisible = false
            viewBinding.errorLayout.isVisible = false
            viewBinding.noDataLayout.isVisible = false
        }
        adapter?.submitList(notifications)
    }
}