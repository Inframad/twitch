package com.example.twitchapp.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun<T> Fragment.bindAction(flow: ViewModelStateFlowSingleEvent<T?>, result: (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest { event ->
                event.getContentIfNotHandled()?.let {
                    result(it)
                }
            }
        }
    }
}