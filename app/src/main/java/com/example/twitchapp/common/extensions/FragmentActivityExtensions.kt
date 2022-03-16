package com.example.twitchapp.common.extensions

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.twitchapp.common.ViewModelStateFlowSingleEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun <T> FragmentActivity.bindCommandAction(flow: ViewModelStateFlowSingleEvent<T?>, action: (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest { event ->
                event.getContentIfNotHandled()?.let {
                    action(it)
                }
            }
        }
    }
}