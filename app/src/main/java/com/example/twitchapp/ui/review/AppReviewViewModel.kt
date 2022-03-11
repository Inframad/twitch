package com.example.twitchapp.ui.review

import android.content.Context
import android.text.Editable
import com.example.twitchapp.R
import com.example.twitchapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AppReviewViewModel @Inject constructor(
    @ApplicationContext context: Context
) : BaseViewModel(context) {

    fun onSendReviewButtonClicked(review: Editable, rating: Float) {
        showToast(getString(R.string.review_is_sent_msg, review.toString(), rating))
    }
}