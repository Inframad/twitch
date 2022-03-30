package com.example.common.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData

fun <T> Fragment.bindActionLiveData(liveData: LiveData<T>, action: (T) -> Unit) {
    liveData.observe(viewLifecycleOwner) {
        action(it)
    }
}