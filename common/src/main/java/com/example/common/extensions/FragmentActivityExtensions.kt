package com.example.common.extensions

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData

fun <T> FragmentActivity.bindActionLiveData(liveData: LiveData<T>, action: (T) -> Unit) {
    liveData.observe(this) {
        action(it)
    }
}