package com.example.twitchapp.ui.favourite

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.twitchapp.R
import com.example.twitchapp.data.model.game.Game
import com.example.twitchapp.databinding.FragmentFavouriteGamesBinding
import com.example.twitchapp.ui.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavouriteGamesFragment : Fragment(R.layout.fragment_favourite_games) {

    private val viewBinding: FragmentFavouriteGamesBinding by viewBinding()
    private val viewModel: FavouriteGamesViewModel by viewModels()

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

    private fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is UiState.Loaded -> showFavouriteGamesList(uiState.data)
                        is UiState.Error -> showError(uiState.msg)
                        UiState.Loading -> showLoading()
                        UiState.Empty -> showNoDataPlaceholder()
                    }
                }
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