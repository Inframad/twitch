package com.example.twitchapp.ui.notification

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.twitchapp.R
import com.example.twitchapp.common.BaseFragment
import com.example.twitchapp.common.extensions.bindAction
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
        viewBinding.recyclerView.adapter = adapter
        viewBinding.noDataTextView.text = getString(R.string.scr_any_lbl_no_notifications)
    }

    override fun bindViewModel() {
        super.bindViewModel()
        bindAction(viewModel.uiState) { uiState ->
            when (uiState) {
                is UiState.Loaded -> showFavouriteGamesList(
                    uiState.data.map {
                        TwitchNotificationPresentation.fromModel(it, getString(R.string.scr_any_date_time_pattern))
                    })
                is UiState.Error -> showError(uiState.msg)
                UiState.Loading -> showLoading()
                UiState.Empty -> showNoDataPlaceholder()
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