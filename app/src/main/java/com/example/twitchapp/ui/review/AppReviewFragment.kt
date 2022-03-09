package com.example.twitchapp.ui.review

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.twitchapp.R
import com.example.twitchapp.databinding.FragmentAppReviewBinding
import com.example.twitchapp.ui.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppReviewFragment : Fragment(R.layout.fragment_app_review) {

    private val binding: FragmentAppReviewBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        binding.apply {
            sendReviewButton.setOnClickListener {
                context?.showToast(
                    String.format(
                        getString(R.string.review_is_sent_msg),
                        appReviewEditText.text,
                        appRatingBar.rating.toString()
                    )
                )
                findNavController().popBackStack()
            }
        }
    }

}