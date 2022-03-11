package com.example.twitchapp.ui.review

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.twitchapp.R
import com.example.twitchapp.databinding.FragmentAppReviewBinding
import com.example.twitchapp.ui.BaseFragment
import com.example.twitchapp.ui.Navigator
import com.example.twitchapp.ui.util.bindCommandAction
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppReviewFragment : BaseFragment<AppReviewViewModel>(R.layout.fragment_app_review) {

    override val viewModel: AppReviewViewModel by viewModels()
    private val viewBinding: FragmentAppReviewBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        bindViewModel()
    }

    override fun bindViewModel() {
        super.bindViewModel()
        bindCommandAction(viewModel.goBackCommand) {
            Navigator.goBack(this@AppReviewFragment)
        }
    }

    private fun initViews() {
        viewBinding.apply {
            sendReviewButton.setOnClickListener {
                viewModel.onSendReviewButtonClicked(
                    appReviewEditText.text,
                    appRatingBar.rating
                )
            }
        }
    }

}