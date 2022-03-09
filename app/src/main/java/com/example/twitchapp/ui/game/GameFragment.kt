package com.example.twitchapp.ui.game

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.twitchapp.R
import com.example.twitchapp.databinding.FragmentGameBinding
import com.example.twitchapp.ui.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameFragment : Fragment(R.layout.fragment_game) {

    private val viewBinding: FragmentGameBinding by viewBinding()
    private val viewModel: GameViewModel by viewModels()

    private var circularProgressDrawable: CircularProgressDrawable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindData()
        initViews()
        initAnim()
        bindViewModel()
    }

    private fun initViews() {
        viewBinding.noDataTextView.text = getString(R.string.no_saved_data_msg)
    }

    private fun initAnim() {
        circularProgressDrawable = CircularProgressDrawable(requireContext())
        circularProgressDrawable?.let {
            it.strokeWidth = 5f
            it.centerRadius = 30f
            it.start()
        }
    }

    private fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.gameScreenModel.collect { uiState ->
                    when (uiState) {
                        is UiState.Loaded -> showGame(uiState.data)
                        is UiState.Error -> showError(uiState.msg)
                        UiState.Loading -> showLoading()
                        UiState.Empty -> showNoDataPlaceholder()
                    }
                }
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
            Glide.with(root)
                .load(data.imageUrl)
                .placeholder(circularProgressDrawable)
                .error(R.drawable.image_error_placeholder)
                .into(gameImageView)
        }
    }

    private fun bindData() {
        arguments?.let { viewModel.bindData(it) }
    }
}