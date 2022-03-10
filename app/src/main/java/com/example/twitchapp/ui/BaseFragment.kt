package com.example.twitchapp.ui

import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseFragment<TViewModel : BaseViewModel>(@LayoutRes lId: Int) :
    Fragment(lId) {

    protected abstract val viewModel: TViewModel

    protected open fun bindViewModel() {
        with(viewModel) {

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    showToastCommand.collectLatest {
                        it.getContentIfNotHandled()?.let { content -> Toast.makeText(requireContext(), content, Toast.LENGTH_SHORT).show()}
                    }
                }
            }
        }
    }
}