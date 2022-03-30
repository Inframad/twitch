package com.example.twitchapp.ui.review

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.twitchapp.R
import com.example.common.extensions.bindActionLiveData
import com.example.common.livedata.BaseFragmentLiveData
import com.example.twitchapp.databinding.FragmentAppReviewBinding
import com.example.twitchapp.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppReviewFragment : com.example.common.livedata.BaseFragmentLiveData<AppReviewViewModel>(R.layout.fragment_app_review) {

    override val viewModel: AppReviewViewModel by viewModels()
    private val viewBinding: FragmentAppReviewBinding by viewBinding()

    override fun bindViewModel() {
        super.bindViewModel()
        bindActionLiveData(viewModel.goBackCommand) {
            Navigator.goBack(this@AppReviewFragment)
        }
    }

    override fun initViews() {
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