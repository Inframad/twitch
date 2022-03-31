package com.example.appreview

import android.content.Context
import android.text.Editable
import com.example.common.livedata.BaseViewModelLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AppReviewViewModel @Inject constructor(
    @ApplicationContext context: Context
) : BaseViewModelLiveData(context) {

    val goBackCommand = Command()

    fun onSendReviewButtonClicked(review: Editable, rating: Float) {
        showToast(getString(R.string.scr_app_review_lbl_review_is_sent, review.toString(), rating))
        goBackCommand.setValue(Unit)
    }
}