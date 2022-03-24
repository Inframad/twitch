package com.example.twitchapp.ui.game

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.twitchapp.R
import com.example.twitchapp.common.extensions.bindAction
import com.example.twitchapp.common.extensions.glideImage
import com.example.twitchapp.common.extensions.setTintColor
import com.example.twitchapp.common.flow.BaseFragment
import com.example.twitchapp.databinding.FragmentGameBinding
import com.example.twitchapp.ui.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameFragment : BaseFragment<GameViewModel>(R.layout.fragment_game) {

    private val viewBinding: FragmentGameBinding by viewBinding()
    override val viewModel: GameViewModel by viewModels()
    private val navArgs: GameFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun initViews() {
        viewBinding.noDataTextView.text =
            getString(R.string.src_streams_lbl_no_saved_data_and_internet)
        viewBinding.favouriteGameImageButton.setOnClickListener {
            viewModel.favouriteGameImageButtonClicked()
        }
    }

    override fun bindViewModel() {
        super.bindViewModel()
        with(viewModel) {
            bindAction(uiState) {
                when (it) {
                    is UiState.Loaded -> showGame(it.data)
                    is UiState.Error -> showError(it.msg)
                    UiState.Loading -> showLoading()
                    UiState.Empty -> showNoDataPlaceholder()
                }
            }
            bindAction(toggleFavourite) { color ->
                viewBinding.favouriteGameImageButton.setTintColor(color)
            }
        }
    }

    private fun showError(msg: String) {
        viewBinding.progressBarLayout.isVisible = false
        viewBinding.errorLayout.isVisible = true
        viewBinding.noDataLayout.isVisible = false
        viewBinding.errorTextView.text = msg
    }

    private fun showNoDataPlaceholder() {
        viewBinding.progressBarLayout.isVisible = false
        viewBinding.errorLayout.isVisible = false
        viewBinding.noDataLayout.isVisible = true
    }

    private fun showLoading() {
        viewBinding.progressBarLayout.isVisible = true
        viewBinding.errorLayout.isVisible = false
        viewBinding.noDataLayout.isVisible = false
    }

    private fun showGame(data: GameScreenModel) {
        viewBinding.apply {
            progressBarLayout.isVisible = false
            viewBinding.errorLayout.isVisible = false
            viewBinding.noDataLayout.isVisible = false

            gameNameTextView.text = data.name
            streamerNameTextView.text = data.streamerName
            viewersCountTextView.text = data.viewersCount
            gameImageView.glideImage(data.imageUrl)
        }
    }

    private fun init() {
        viewModel.init(navArgs.stream, navArgs.notification)
    }
}