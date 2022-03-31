package com.example.appreview

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appreview.databinding.FragmentAppReviewBinding
import com.example.common.extensions.bindActionLiveData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppReviewFragment : com.example.common.livedata.BaseFragmentLiveData<AppReviewViewModel>(R.layout.fragment_app_review) {

    override val viewModel: AppReviewViewModel by viewModels()
    private val viewBinding: FragmentAppReviewBinding by viewBinding()

    override fun bindViewModel() {
        super.bindViewModel()
        bindActionLiveData(viewModel.goBackCommand) {
            findNavController().popBackStack()
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