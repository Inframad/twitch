package com.example.favourite

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.common.extensions.bindActionLiveData
import com.example.favourite.databinding.FragmentSimpleListBinding
import com.example.twitchapp.model.game.Game
import com.example.twitchapp.model.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteGamesFragment :
    com.example.common.livedata.BaseFragmentLiveData<FavouriteGamesViewModel>(R.layout.fragment_simple_list) {

    private val viewBinding: FragmentSimpleListBinding by viewBinding()
    override val viewModel: FavouriteGamesViewModel by viewModels()

    private var adapter: FavouriteGamesAdapter? = null

    override fun initViews() {
        adapter = FavouriteGamesAdapter()
        viewBinding.apply {
            recyclerView.layoutManager = GridLayoutManager(context, 2)
            recyclerView.adapter = adapter
            noDataTextView.text = getString(R.string.scr_favourite_games_no_favourite_games)
        }
    }

    override fun bindViewModel() {
        super.bindViewModel()
        bindActionLiveData(viewModel.uiState) { uiState ->
            when (uiState) {
                is UiState.Loaded -> showFavouriteGamesList(uiState.data)
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

    private fun showFavouriteGamesList(games: List<Game>) {
        viewBinding.apply {
            progressBarLayout.isVisible = false
            viewBinding.errorLayout.isVisible = false
            viewBinding.noDataLayout.isVisible = false
        }
        adapter?.submitList(games)
    }
}