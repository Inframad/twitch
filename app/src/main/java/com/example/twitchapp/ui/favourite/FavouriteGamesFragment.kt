package com.example.twitchapp.ui.favourite

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.twitchapp.R
import com.example.twitchapp.common.BaseFragment
import com.example.twitchapp.common.bindAction
import com.example.twitchapp.databinding.FragmentFavouriteGamesBinding
import com.example.twitchapp.model.game.Game
import com.example.twitchapp.ui.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteGamesFragment : BaseFragment<FavouriteGamesViewModel>(R.layout.fragment_favourite_games) {

    private val viewBinding: FragmentFavouriteGamesBinding by viewBinding()
    override val viewModel: FavouriteGamesViewModel by viewModels()

    private var adapter: FavouriteGamesAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        bindViewModel()
    }

    private fun initViews() {
        adapter = FavouriteGamesAdapter()
        viewBinding.recyclerView.adapter = adapter
        viewBinding.noDataTextView.text = getString(R.string.no_saved_data_msg)
    }

    override fun bindViewModel() {
        bindAction(viewModel.uiState) { uiState ->
            when (uiState) {
                is UiState.Loaded -> showFavouriteGamesList(uiState.data)
                is UiState.Error -> showError(uiState.msg)
                UiState.Loading -> showLoading()
                UiState.Empty -> showNoDataPlaceholder()
            }
        }
    }

    private fun showLoading() {
        viewBinding.progressBarLayout.isVisible = true
        viewBinding.errorLayout.isVisible = false
        viewBinding.noDataLayout.isVisible = false
    }

    private fun showNoDataPlaceholder() {
        viewBinding.progressBarLayout.isVisible = false
        viewBinding.errorLayout.isVisible = false
        viewBinding.noDataLayout.isVisible = true
    }

    private fun showError(msg: String) {
        viewBinding.progressBarLayout.isVisible = false
        viewBinding.errorLayout.isVisible = true
        viewBinding.noDataLayout.isVisible = false
        viewBinding.errorTextView.text = msg
    }

    private fun showFavouriteGamesList(games: List<Game>) {
        viewBinding.apply {
            progressBarLayout.isVisible = false
            viewBinding.errorLayout.isVisible = false
            viewBinding.noDataLayout.isVisible = false
        }

        adapter?.submitList(games)
    }
}